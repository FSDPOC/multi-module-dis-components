package com.db.persistence.repo;

import com.db.persistence.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

  Book findByIsbn(String isbn);

  List<Book> findByTitleContaining(String title);
}
