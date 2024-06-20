package com.nico.library.controller;

import com.nico.library.dto.response.user.UserBookResponse;
import com.nico.library.service.implementation.UserBookServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<UserBookResponse>addUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("bookId") @Min(1) int bookId)
    {
        UserBookResponse response = userBookServiceImpl.addUserBook(userDetails, bookId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/delete_user_book/{bookId}")
    public ResponseEntity<?> deleteUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("bookId") @Min(1) int bookId)
    {
        userBookServiceImpl.deleteUserBook(userDetails, bookId);
        return new ResponseEntity<>(String.format("Book with id '%d' successfully deleted from %s's library", bookId, userDetails.getUsername()), OK);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/update_book_read_count/{bookId}")
    public ResponseEntity<UserBookResponse> updateBookReadCount(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("bookId") @Min(1) int bookId,
            @RequestParam @Valid @Min(value = 0, message = "readCount must be positive and a valid number") int readCount)
    {
        UserBookResponse response = userBookServiceImpl.updateBookReadCount(userDetails, bookId, readCount);
        return ResponseEntity.ok(response);
    }


}
