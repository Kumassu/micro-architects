package song.pan.toolkit.web.rest.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import song.pan.toolkit.web.rest.domain.Author;
import song.pan.toolkit.web.rest.domain.Book;
import song.pan.toolkit.web.rest.exception.IllegalParameterException;
import song.pan.toolkit.web.rest.exception.ResourceNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Book")
public class BookController {

    public static final Cache<String, Book> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build();


    @GetMapping("/v1/books")
    @ApiOperation("Retrieve all books")
    public Collection<Book> getServers() {
        return CACHE.asMap().values();
    }


    @GetMapping("/v1/books/{name}")
    @ApiOperation("Retrieve book by name")
    public Book getBook(@PathVariable("name") String name) throws ExecutionException {
        return CACHE.get(name, () -> {
            throw new ResourceNotFoundException("not found : " + name);
        });
    }


    @PostMapping("/v1/books")
    @ApiOperation("Add book")
    public Book addBook(@RequestBody Book book) {
        if (null != CACHE.getIfPresent(book.getName())) {
            throw new IllegalParameterException("already exists: " + book.getName());
        }
        CACHE.put(book.getName(), book);
        return book;
    }


    @PutMapping("/v1/books")
    @ApiOperation("Update book")
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
    public Book patchBook(@PathVariable String name, @RequestBody PatchBook book) {
        Book bookCache = CACHE.getIfPresent(name);
        if (null == bookCache) {
            throw new IllegalParameterException("Not exist: " + name);
        }
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
    public Book deleteBook(@RequestBody Book book) {
        if (null == CACHE.getIfPresent(book.getName())) {
            throw new IllegalParameterException("Not exist: " + book.getName());
        }
        CACHE.invalidate(book.getName());
        return book;
    }

}
