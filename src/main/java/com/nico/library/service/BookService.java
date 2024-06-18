package com.nico.library.service;

import com.nico.library.dto.request.book.BookRequest;
import com.nico.library.dto.response.book.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> getAvailableBooks();
    BookResponse getBookById(int bookId);
    List<BookResponse> getBookByGenre(String genre);
    BookResponse updateBookById(BookRequest request, int bookId);
    BookResponse addBook(BookRequest request);
    void deleteBookById(int bookId);
}
