package com.nico.library.controller;

import com.nico.library.dto.request.book.BookRequest;
import com.nico.library.dto.response.book.BookResponse;
import com.nico.library.service.implementation.BookServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Validated
public class BookController
{
    private final BookServiceImpl bookServiceImpl;

    //Vedere tutti i libri disponibili
    @GetMapping("/get-books")
    public ResponseEntity<List<BookResponse>> getAllBooksAvailable()
    {
        List<BookResponse> responses = bookServiceImpl.getAvailableBooks();
        return ResponseEntity.ok(responses);
    }

    //Trovare un libro tramite l'id
    @GetMapping("/get-book-by-id/{bookId}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("bookId") @Min(1) int bookId)
    {
        BookResponse response = bookServiceImpl.getBookById(bookId);
        return ResponseEntity.ok(response);
    }

    //Trovare un libro per genere
    @GetMapping("/get-book-by-genre/{genre}")
    public ResponseEntity<List<BookResponse>> getBookByGenre(@PathVariable("genre") @NotBlank @Length(max = 20) String genre)
    {
        List<BookResponse> responses = bookServiceImpl.getBookByGenre(genre);
        return ResponseEntity.ok(responses);
    }

    //aggiungere un libro
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-book")
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid BookRequest request) {
        BookResponse response = bookServiceImpl.addBook(request);
        return new ResponseEntity<>(response, CREATED);
    }

    //aggiornare libro per id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-book/{bookId}")
    public ResponseEntity<BookResponse> updateBookById(
            @RequestBody @Valid BookRequest request,
            @PathVariable("bookId") @Min(1) int bookId) {
        BookResponse response = bookServiceImpl.updateBookById(request, bookId);
        return ResponseEntity.ok(response);
    }

    //aggiornare determinati campi del libro con PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/update-book-field/{bookId}")
    public ResponseEntity<BookResponse> updateBookFieldById(@PathVariable("bookId") @Min(1) int bookId,
                                                 @RequestBody @Valid BookRequest request)
    {
        BookResponse response = bookServiceImpl.updateBookById(request, bookId);
        return ResponseEntity.ok(response);
    }



    //eliminare un libro tramite id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("delete-book/{bookId}")
    public ResponseEntity<String> deleteBookById(@PathVariable("bookId") @Min(1) int bookId)
    {
        bookServiceImpl.deleteBookById(bookId);
        return new ResponseEntity<>("Book with id " + bookId + " successfully deleted!", HttpStatus.OK);
    }
}
