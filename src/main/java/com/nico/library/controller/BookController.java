package com.nico.library.controller;

import com.nico.library.dto.request.book.BookRequest;
import com.nico.library.dto.response.book.BookResponse;
import com.nico.library.service.implementation.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Validated
public class BookController {
    private final BookServiceImpl bookServiceImpl;

    @Operation(
            summary = "GET endpoint to retrieve all books",
            description = "Retrieves all available books. If no books are found, throws an EmptyListException.",
            responses = {
                    @ApiResponse(
                            description = "Books successfully found",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            description = "No books found",
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
    @GetMapping("/get-books")
    public ResponseEntity<List<BookResponse>> getAllBooksAvailable() {
        List<BookResponse> responses = bookServiceImpl.getAvailableBooks();
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "GET endpoint to retrieve a book by ID",
            description = "Retrieves a specified book by its unique identifier. If no book is found, throws a ResourceNotFoundException.",
            responses = {
                    @ApiResponse(
                            description = "Book successfully found",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))
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
    @GetMapping("/get-book-by-id/{bookId}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable("bookId") @Min(1) int bookId) {
        BookResponse response = bookServiceImpl.getBookById(bookId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "GET endpoint to retrieve a list of books by genre",
            description = "Retrieves a specified books by their genre. If no book is found, throws a ResourceNotFoundException.",
            responses = {
                    @ApiResponse(
                            description = "Books successfully found",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            description = "No books found",
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
    @GetMapping("/get-book-by-genre/{genre}")
    public ResponseEntity<List<BookResponse>> getBookByGenre(
            @PathVariable("genre") @NotBlank @Length(max = 20) String genre) {
        List<BookResponse> responses = bookServiceImpl.getBookByGenre(genre);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "POST endpoint to add a new book",
            description = "Add a new book to the library. ISBN must contains 13 characters and only digits. This action can only be performed by an admin.",
            responses = {
                    @ApiResponse(
                            description = "Book successfully added",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request - Error with ISBN code",
                            responseCode = "400",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-book")
    public ResponseEntity<BookResponse> addBook(
            @RequestBody @Valid BookRequest request) {
        BookResponse response = bookServiceImpl.addBook(request);
        return new ResponseEntity<>(response, CREATED);
    }

    @Operation(
            summary = "PUT endpoint to update a book by its id",
            description = "Updates existing book by its ID in the library",
            responses = {
                    @ApiResponse(
                            description = "Book successfully updated",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            description = "Book not found",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-book/{bookId}")
    public ResponseEntity<BookResponse> updateBookById(
            @RequestBody @Valid BookRequest request,
            @PathVariable("bookId") @Min(1) int bookId) {
        BookResponse response = bookServiceImpl.updateBookById(request, bookId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "PATCH endpoint to update a book0s filed by its id",
            description = "Updates an existing book by its ID in the library. This action can only be performed by an admin.",
            responses = {
                    @ApiResponse(
                            description = "Book successfully updated",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            description = "Book not found",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/update-book-field/{bookId}")
    public ResponseEntity<BookResponse> updateBookFieldById(
            @PathVariable("bookId") @Min(1) int bookId,
            @RequestBody @Valid BookRequest request) {
        BookResponse response = bookServiceImpl.updateBookById(request, bookId);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "DELETE endpoint to delete a book by its id",
            description = "Delete an existing book by its ID from the library",
            responses = {
                    @ApiResponse(
                            description = "Book successfully deleted",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Book not found",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("delete-book/{bookId}")
    public ResponseEntity<String> deleteBookById(
            @PathVariable("bookId") @Min(1) int bookId) {
        bookServiceImpl.deleteBookById(bookId);
        return new ResponseEntity<>("Book with id " + bookId + " successfully deleted!", OK);
    }
}
