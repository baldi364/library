package com.nico.library.repository;

import com.nico.library.entity.Book;
import com.nico.library.payload.response.BookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>
{
    @Query("SELECT new com.nico.library.payload.response.BookResponse(" +
            "b.bookId, " +
            "b.title, " +
            "b.author, " +
            "b.plot, " +
            "b.genre, " +
            "b.ISBN) " +
            "FROM Book b " +
            "WHERE LOWER(b.genre) = LOWER(:genre)")
    List<BookResponse> getBookByGenreIgnoreCase(String genre);
}
