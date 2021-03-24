package song.pan.toolkit.web.rest.common;

import song.pan.toolkit.web.rest.domain.TracedMethod;

import java.util.List;

/**
 * @author Song Pan
 * @version 1.0.0
 */
public abstract class ThreadCache {



    /**
     * An unique id for current thread's current task
     */
    public static final ThreadLocal<String> TASK_ID = new ThreadLocal<>();


}
