package com.nico.library.controller;

import com.nico.library.dto.response.user.UserBookResponse;
import com.nico.library.service.implementation.UserBookServiceImpl;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
@Validated
public class LibraryController
{
    private final UserBookServiceImpl userBookServiceImpl;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/get_user_books")
    public ResponseEntity<List<UserBookResponse>> getUserBooks(@AuthenticationPrincipal UserDetails userDetails)
    {
        List<UserBookResponse> response = userBookServiceImpl.getUserBooks(userDetails);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/add_user_book/{bookId}")
    public ResponseEntity<?>addUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("bookId") @Min(1) int bookId)
    {
        return userBookServiceImpl.addUserBook(userDetails, bookId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/delete_user_book/{bookId}")
    public ResponseEntity<?> deleteUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("bookId") @Min(1) int bookId)
    {
        return userBookServiceImpl.deleteUserBook(userDetails, bookId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/update_book_read_count/{bookId}")
    public ResponseEntity<?> updateBookReadCount(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable("bookId") @Min(1) int bookId,
                                                 @RequestParam @Min(0) int readCount)
    {
        return userBookServiceImpl.updateBookReadCount(userDetails, bookId, readCount);
    }


}
