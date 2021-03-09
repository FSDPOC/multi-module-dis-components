package com.db.persistence.repo;

import com.db.persistence.bean.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);

    List<Book> findByTitleContaining(String title);
}
