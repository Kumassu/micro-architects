package song.pan.toolkit.web.rest.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import song.pan.toolkit.web.rest.domain.Book;

import java.util.concurrent.TimeUnit;

/**
 * @author Song Pan
 * @version 1.0.0
 */
public abstract class CacheHolder {

    public static final Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

}
