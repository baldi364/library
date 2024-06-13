package com.nico.library.service;

import com.nico.library.dto.response.book.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> getAvailableBooks();
}
