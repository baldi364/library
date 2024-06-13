package com.nico.library.repository;

import com.nico.library.entity.Book;
import com.nico.library.dto.response.book.BookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>
{
    @Query("SELECT new com.nico.library.dto.response.book.BookResponse(" +
            "b.bookId, " +
            "b.title, " +
            "b.author, " +
            "b.plot, " +
            "b.genre, " +
            "b.ISBN) " +
            "FROM Book b " +
            "WHERE LOWER(b.genre) = LOWER(:genre)")
    List<BookResponse> getBookByGenreIgnoreCase(String genre);

    Optional<Book> findByISBN(String ISBN);
}
