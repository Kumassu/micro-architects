package song.pan.toolkit.common.schedule;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Getter
@Setter
public class CronJob<T> extends ScheduleJob<T> {


    private String cron;


    public CronJob(String name, String cron, Callable<T> task) {
        super(name, task);
        this.cron = cron;
    }

    @Override
    protected void updateNextFire() {
        super.updateNextFire();
        setNextFire(new CronSequenceGenerator(cron).next(new Date()));
    }

    @Override
    protected Trigger getTrigger() {
        return new CronTrigger(cron);
    }

    @Override
    protected void update(ScheduleJob<T> job) {
        if (job instanceof CronJob) {
            setCron(((CronJob<T>) job).getCron());
        }
    }
}
