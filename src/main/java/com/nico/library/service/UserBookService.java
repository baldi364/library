package com.nico.library.service;

import com.nico.library.dto.response.user.UserBookResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserBookService {
    UserBookResponse addUserBook(UserDetails userDetails, int bookId);
    List<UserBookResponse> getUserBooks(UserDetails userDetails);
    void deleteUserBook(UserDetails userDetails, int bookId);
    UserBookResponse updateBookReadCount(UserDetails userDetails, int bookId, int readCount);
}
