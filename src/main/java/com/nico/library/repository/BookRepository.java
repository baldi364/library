package com.nico.library.repository;

import com.nico.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>
{
    @Query("SELECT b " +
            "FROM Book b " +
            "WHERE LOWER(b.genre) = LOWER(:genre)")
    List<Book> getBookByGenreIgnoreCase(String genre);

    Optional<Book> findByISBN(String ISBN);
}
