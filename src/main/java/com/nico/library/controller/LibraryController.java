package com.nico.library.controller;

import com.nico.library.service.UserBookService;
import jakarta.validation.constraints.Min;
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
    @GetMapping("/get_user_books")
    public ResponseEntity<?> getUserBooks(@AuthenticationPrincipal UserDetails userDetails)
    {
        return userBookService.getUserBooks(userDetails);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/add_user_book/{bookId}")
    public ResponseEntity<?>addUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("bookId") @Min(1) int bookId)
    {
        return userBookService.addUserBook(userDetails, bookId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/delete_user_book/{bookId}")
    public ResponseEntity<?> deleteUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("bookId") @Min(1) int bookId)
    {
        return userBookService.deleteUserBook(userDetails, bookId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/update_book_read_count/{bookId}")
    public ResponseEntity<?> updateBookReadCount(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable("bookId") @Min(1) int bookId,
                                                 @RequestParam @Min(0) int readCount)
    {
        return userBookService.updateBookReadCount(userDetails, bookId, readCount);
    }


}
