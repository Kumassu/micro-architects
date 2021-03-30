package song.pan.toolkit.common.schedule;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Getter
@Setter
public class PeriodicJob<T> extends ScheduleJob<T> {

    private long period;

    public PeriodicJob(String name, long period, Callable<T> task) {
        super(name, task);
        this.period = period;
    }

    @Override
    protected void updateNextFire() {
        super.updateNextFire();
        setNextFire(new Date(System.currentTimeMillis() + period));
    }

    @Override
    protected void update(ScheduleJob<T> job) {
        if (job instanceof PeriodicJob) {
            setPeriod(((PeriodicJob<T>) job).getPeriod());
        }
    }

    @Override
    protected Trigger getTrigger() {
        return new PeriodicTrigger(period);
    }

}
