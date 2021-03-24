package song.pan.toolkit.web.rest.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static song.pan.toolkit.web.rest.common.CacheHolder.CACHE;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Book")
public class BookController {


    @GetMapping("/v1/books")
    @ApiOperation("Retrieve all books")
    @TimeLog
    @Trace
    public Collection<Book> getBooks() {
        return CACHE.asMap().values().stream().filter(v -> v instanceof Book).map(v -> (Book) v).collect(Collectors.toList());
    }


    @GetMapping("/v1/books/{name}")
    @ApiOperation("Retrieve book by name")
    @TimeLog
    @Trace
    public Book getBook(@PathVariable("name") String name) throws ExecutionException {
        return (Book) CACHE.get(name, () -> {
            throw new ResourceNotFoundException("not found : " + name);
        });
    }


    @PostMapping("/v1/books")
    @ApiOperation("Add book")
    @TimeLog
    @Trace
    public Book addBook(@RequestBody Book book) {
        if (null != CACHE.getIfPresent(book.getName())) {
            throw new IllegalParameterException("already exists: " + book.getName());
        }
        CACHE.put(book.getName(), book);
        return book;
    }


    @PutMapping("/v1/books")
    @ApiOperation("Update book")
    @TimeLog
    @Trace
    public Book updateBook(@RequestBody Book book) {
        if (null == CACHE.getIfPresent(book.getName())) {
            throw new IllegalParameterException("Not exist: " + book.getName());
        }
        CACHE.put(book.getName(), book);
        return book;
    }


    @Getter
    @Setter
    public static class PatchBook {
        private Author author;
        private Date publishDate;
    }


    @PatchMapping("/v1/books/{name}")
    @ApiOperation("Update book")
    @TimeLog
    @Trace
    public Book patchBook(@PathVariable String name, @RequestBody PatchBook book) {
        Object object = CACHE.getIfPresent(name);
        if (null == object || !(object instanceof Book)) {
            throw new IllegalParameterException("Not exist: " + name);
        }

        Book bookCache = (Book) object;

        if (book.author != null) {
            bookCache.setAuthor(book.author);
        }
        if (book.publishDate != null) {
            bookCache.setPublishDate(book.publishDate);
        }
        return bookCache;
    }


    @DeleteMapping("/v1/books/{name}")
    @ApiOperation("Update book")
    @TimeLog
    @Trace
    public Book deleteBook(@RequestBody Book book) {
        if (null == CACHE.getIfPresent(book.getName())) {
            throw new IllegalParameterException("Not exist: " + book.getName());
        }
        CACHE.invalidate(book.getName());
        return book;
    }

}
