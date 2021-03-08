package com.db.persistence.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book")
@Cacheable
// @ApiModel(description = "Book service details ")
public class Book implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String title;

  @Column(unique = true)
  private String isbn;

  public Book() {}

  public Book(String title, String isbn) {
    this.title = title;
    this.isbn = isbn;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }
}
