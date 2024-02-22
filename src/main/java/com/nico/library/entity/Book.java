package com.nico.library.entity;

import com.nico.library.entity.common.Creation;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book extends Creation
{
    /*
    CREATE TABLE IF NOT EXISTS book(
book_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
title VARCHAR(50) NOT NULL,
author VARCHAR(50) NOT NULL,
plot TEXT,
genre VARCHAR(20) NOT NULL,
ISBN VARCHAR(13) NOT NULL UNIQUE,
add_date DATETIME NOT NULL,
delete_date DATETIME NULL
);
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String author;

    private String plot;

    @Column(length = 20, nullable = false)
    private String genre;

    @Column(name = "ISBN", length = 13, nullable = false, unique = true)
    private String ISBN;

    @OneToMany(mappedBy = "userBookId.book")
    private Set<UserBook> userBooks = new HashSet<>();

    public Book(String title, String author, String plot, String genre, String ISBN)
    {
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.genre = genre;
        this.ISBN = ISBN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
