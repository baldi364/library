package com.nico.library.controller;

import com.nico.library.service.UserBookService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
@Validated
public class LibraryController
{
    private final UserBookService userBookService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/get-user_books")
    public ResponseEntity<?> getUserBooks(@AuthenticationPrincipal UserDetails userDetails)
    {
        return userBookService.getUserBooks(userDetails);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/add-user_book")
    public ResponseEntity addUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                      @Min(1) int bookId)
    {
        return userBookService.addUserBook(userDetails, bookId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/delete-user_book")
    public ResponseEntity<?> deleteUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                        @Min(1) int bookId)
    {
        return userBookService.deleteUserBook(userDetails, bookId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/update-book-read-count")
    public ResponseEntity<?> updateBookReadCount(@AuthenticationPrincipal UserDetails userDetails,
                                                 @Min(1) int bookId,
                                                 @RequestParam @Min(0) int readCount)
    {
        return userBookService.updateBookReadCount(userDetails, bookId, readCount);
    }


}
