//
package com.db.controller;

import com.db.persistence.bean.Book;
import com.db.persistence.service.ApplicationService;
import java.net.URI;
import java.util.List;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/admin")
// @Api(value = "Admin Controller")
public class AdminController {

    @Autowired ApplicationService applicationService;

    @Autowired MessageSource messageSource;

    @GetMapping("/admins")
    // @ApiOperation(value = "test admin path", notes = "testing admin module controller")
    public ResponseEntity getAdmin() {
        return ResponseEntity.ok("Admin service module is active");
    }

    @GetMapping("/book/{title}")
    public List<Book> getBooksByTitleName(@PathVariable String title) {

        List<Book> bookList = applicationService.findByTitleContaining(title);
        // throw new RuntimeException("exception occurred due to system failure");
        return bookList;
    }
    //    @GetMapping("/book/{id}")
    //    public Book getBookByIsbn(@PathVariable Long id) {
    //
    //        Book book = applicationService.findById(id);
    //        return book;
    //    }

    @GetMapping("/book/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {

        Book book = applicationService.findByIsbn(isbn);
        return book;
    }

    @PostMapping("/book")
    public ResponseEntity createBook(@RequestBody Book book) {

        Book bookSaved = applicationService.saveBook(book);

        URI loc =
                ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/{id}")
                        .buildAndExpand(bookSaved.getId())
                        .toUri();

        return ResponseEntity.created(loc).build();
    }

    private static void printStats(Statistics stats, int i) {
        System.out.println("***** " + i + " *****");
        System.out.println("Fetch Count=" + stats.getEntityFetchCount());
        System.out.println("Second Level Hit Count=" + stats.getSecondLevelCacheHitCount());
        System.out.println("Second Level Miss Count=" + stats.getSecondLevelCacheMissCount());
        System.out.println("Second Level Put Count=" + stats.getSecondLevelCachePutCount());
    }
}
