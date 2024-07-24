package com.nico.library.controller;

import com.nico.library.dto.response.user.UserBookResponse;
import com.nico.library.service.implementation.UserBookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "GET endpoint to retrieve all books possessed by a user",
            description = "Retrieves all books present in user's library. If no books are found it throws a EmptyListException",
            responses = {
                    @ApiResponse(
                            description = "Owned books successfully found",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = UserBookResponse.class))
                    ),
                    @ApiResponse(
                            description = "No owned books found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "No user found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/get_user_books")
    public ResponseEntity<List<UserBookResponse>> getUserBooks(@AuthenticationPrincipal UserDetails userDetails)
    {
        List<UserBookResponse> response = userBookServiceImpl.getUserBooks(userDetails);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "POST endpoint to add a book to user's library",
            description = "Add a new book to the user library. If book is already present, it throws a BookAlreadyPresentException",
            responses = {
                    @ApiResponse(
                            description = "Book successfully added",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserBookResponse.class))
                    ),
                    @ApiResponse(
                            description = "Book already present for this user",
                            responseCode = "400",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "No user found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "No book found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/add_user_book/{bookId}")
    public ResponseEntity<UserBookResponse>addUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("bookId") @Min(1) int bookId)
    {
        UserBookResponse response = userBookServiceImpl.addUserBook(userDetails, bookId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "DELETE endpoint to delete a book from user's library",
            description = "Delete an existing book from the user library.",
            responses = {
                    @ApiResponse(
                            description = "Book successfully deleted",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "No user found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "No book found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/delete_user_book/{bookId}")
    public ResponseEntity<?> deleteUserBook(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("bookId") @Min(1) int bookId)
    {
        userBookServiceImpl.deleteUserBook(userDetails, bookId);
        return new ResponseEntity<>(String.format("Book with id '%d' successfully deleted from %s's library", bookId, userDetails.getUsername()), OK);
    }

    @Operation(
            summary = "PUT endpoint to update the read count of a specific book",
            description = "Updates the read count of a specific book in the authenticated user's library. This endpoint requires the user to have the ROLE_MEMBER authority.",
            responses = {
                    @ApiResponse(
                            description = "Book read count successfully updated",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserBookResponse.class))
                    ),
                    @ApiResponse(
                            description = "No user found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "No book found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
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