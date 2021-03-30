package song.pan.toolkit.common.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.util.ErrorHandler;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Song Pan
 * @version 1.0.0
 */
@Getter
@Setter
@Slf4j
public abstract class ScheduleJob<T> implements Runnable {

    private String name;

    private Callable<T> task;

    public ScheduleJob(String name, Callable<T> task) {
        this.name = name;
        this.task = task;
    }


    private ScheduledFuture<T> future;

    private ErrorHandleStrategy errorHandleStrategy = ErrorHandleStrategy.STOP;

    private JobStatus status = JobStatus.STOPPED;

    private long count;

    private Date nextFire;

    private Date firstFire;

    private Date lastFire;

    private Throwable error;

    private T result;

    protected void updateNextFire() {
        this.lastFire = this.nextFire;
    }

    protected void reset() {
        firstFire = null;
        nextFire = null;
        lastFire = null;
        count = 0;
        error = null;
        result = null;
    }


    protected abstract void update(ScheduleJob<T> job);


    @JsonIgnore
    protected abstract Trigger getTrigger();


    @JsonIgnore
    protected ErrorHandler getErrorHandler() {
        return e -> log.error("Error", e);
    }


    @Override
    public void run() {
        status = JobStatus.RUNNING;
        if (firstFire == null) {
            firstFire = new Date();
        }
        updateNextFire();
        count++;
        try {
            if (task != null) {
                result = task.call();
            }
        } catch (Exception e) {
            getErrorHandler().handleError(e);
            switch (errorHandleStrategy) {
                case STOP:
                    status = JobStatus.STOPPED;
                    error = e;
                    Optional.ofNullable(future).ifPresent(f -> f.cancel(Boolean.TRUE));
                    return;
                case THROW:
                    status = JobStatus.ERROR;
                    error = e;
                    Optional.ofNullable(future).ifPresent(f -> f.cancel(Boolean.TRUE));
                    // throw customized exception here
                    throw new RuntimeException(e);
                case RESTART:
                default:
            }
        }
        status = JobStatus.READY;
    }


    public enum ErrorHandleStrategy {
        /**
         * stop the job and will not start again,
         * {@link ScheduleJob#status} set to {@link JobStatus#STOPPED}
         */
        STOP,

        /**
         * ignore error and rerun {@link ScheduleJob#task}
         */
        RESTART,

        /**
         * throw exception, delegate to the caller,
         * {@link ScheduleJob#status} set to {@link JobStatus#ERROR}
         */
        THROW
    }


    public enum JobStatus {
        /**
         * waiting for next timing
         */
        READY,

        /**
         * executing {@link ScheduleJob#task}
         */
        RUNNING,

        /**
         * not scheduled
         */
        STOPPED,

        /**
         * error occurred
         */
        ERROR
    }

}
