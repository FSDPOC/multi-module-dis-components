package com.db.persistence.service;

import com.db.persistence.bean.Book;
import com.db.persistence.repo.BookRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired BookRepository bookRepository;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @Cacheable(cacheNames = "bookCache", key = "'book'+#isbn")
    public Book findByIsbn(String isbn) {
        Optional<Book> retBook = Optional.ofNullable(bookRepository.findByIsbn(isbn));
        if (retBook.isPresent()) return retBook.get();
        else return null;
    }

    public Book findById(Long id) {
        Optional<Book> retBook = bookRepository.findById(id);
        if (retBook.isPresent()) return retBook.get();
        else return null;
    }

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @Cacheable(cacheNames = "bookCacheList", key = "'book_'+#title")
    public List<Book> findByTitleContaining(String title) {
        List<Book> bookList = bookRepository.findByTitleContaining(title);
        return bookList;
    }

    public Book saveBook(Book book) {

        Book bookSaved = bookRepository.save(book);
        return bookSaved;
    }
}
