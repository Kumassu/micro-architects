package song.pan.toolkit.common.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Component
@Slf4j
public class DefaultScheduler {


    private final Map<String, ScheduleJob<Object>> caches;

    private final ThreadPoolTaskScheduler scheduler;

    public DefaultScheduler() {
        caches = new ConcurrentHashMap<>();

        scheduler = new ThreadPoolTaskScheduler();

        // customize
        scheduler.setPoolSize(5);
        scheduler.setErrorHandler(e -> {});
        scheduler.setRemoveOnCancelPolicy(Boolean.TRUE);

        scheduler.initialize();
    }


    public List<ScheduleJob<Object>> getJobs() {
        return new LinkedList<>(caches.values());
    }


    public ScheduleJob<Object> getJob(String name) {
        return caches.get(name);
    }


    public ScheduleJob<Object> addJob(ScheduleJob<Object> job) {
        return addJob(job, Boolean.TRUE);
    }


    public ScheduleJob<Object> addJob(ScheduleJob<Object> job, boolean start) {
        if (caches.containsKey(job.getName())) {
            throw new IllegalArgumentException("already exists: " + job.getName());
        }

        if (start) {
            job.setFuture((ScheduledFuture<Object>) scheduler.schedule(job, job.getTrigger()));
        }

        caches.put(job.getName(), job);
        return job;
    }


    public ScheduleJob<Object> stopJob(String name) {
        if (!caches.containsKey(name)) {
            throw new IllegalArgumentException("not exists: " + name);
        }
        ScheduleJob<Object> job = caches.get(name);
        Optional.ofNullable(job.getFuture()).ifPresent(f -> {
            if (!f.isCancelled()) f.cancel(Boolean.TRUE);
        });
        job.setStatus(ScheduleJob.JobStatus.STOPPED);
        job.setNextFire(null);
        return job;
    }


    public ScheduleJob<Object> restartJob(String name) {
        ScheduleJob<Object> job = stopJob(name);
        job.setFuture((ScheduledFuture<Object>) scheduler.schedule(job, job.getTrigger()));
        job.reset();
        return job;
    }


    public ScheduleJob<Object> updateJob(ScheduleJob<Object> job) {
        ScheduleJob<Object> scheduled = stopJob(job.getName());
        scheduled.update(job);
        job.setFuture((ScheduledFuture<Object>) scheduler.schedule(job, job.getTrigger()));
        job.reset();
        return scheduled;
    }


    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }


    public static void main(String[] args) {
        DefaultScheduler scheduler = new DefaultScheduler();

        // 1. a unique name
        // 2. cron expression
        // 3. define schedule job logic
        // 4. customize error handler
        // 5. set error handle strategy
        CronJob<Object> cronJob = new CronJob<>("test", "0/1 * * * * ?", () -> {
            log.info("{}", new Date());
            // throw new RuntimeException();
            return null;
        }) {
            @Override
            public ErrorHandler getErrorHandler() {
                return e -> {
                    log.info("Error Occurred: {} : {}", e.getClass().getName(), e.getMessage());
                };
            }
        };
        cronJob.setErrorHandleStrategy(ScheduleJob.ErrorHandleStrategy.RESTART);

        scheduler.addJob(cronJob);

        // shutdown at the end
//        scheduler.shutdown();
    }





}