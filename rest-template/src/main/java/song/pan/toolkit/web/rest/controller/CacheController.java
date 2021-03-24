package song.pan.toolkit.web.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import song.pan.toolkit.web.rest.annotation.TimeLog;
import song.pan.toolkit.web.rest.annotation.Trace;
import song.pan.toolkit.web.rest.domain.Author;
import song.pan.toolkit.web.rest.domain.Book;
import song.pan.toolkit.web.rest.exception.IllegalParameterException;
import song.pan.toolkit.web.rest.exception.ResourceNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static song.pan.toolkit.web.rest.common.CacheHolder.CACHE;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Cache")
public class CacheController {


    @GetMapping("/v1/caches")
    @ApiOperation("Retrieve all cache")
    @TimeLog
    public Map<String, Object> getCaches() {
        return CACHE.asMap();
    }

}
