package com.nico.library.controller;

import com.nico.library.payload.request.BookRequest;
import com.nico.library.service.BookService;
import com.nico.library.service.UserBookService;
import com.nico.library.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Validated
public class BookController
{
    private final BookService bookService;

    //Vedere tutti i libri disponibili
    @GetMapping("/get-books")
    public ResponseEntity<?> getAllBooksAvailable()
    {
        return bookService.getAvailableBooks();
    }

    //Trovare un libro tramite l'id
    @GetMapping("/get-book-by-id/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable @Min(1) int bookId)
    {
        return bookService.getBookById(bookId);
    }

    //Trovare un libro per genere
    @GetMapping("/get-book-by-genre/{genre}")
    public ResponseEntity<?> getBookByGenre(@PathVariable @NotBlank @Length(max = 20) String genre)
    {
        return bookService.getBookByGenre(genre);
    }

    //aggiornare libro per id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-book/{bookId}")
    public ResponseEntity<?> updateBookById(@RequestBody @Valid BookRequest request,
                                            @PathVariable @Min(1) int bookId)
    {
        return bookService.updateBookById(request, bookId);
    }

    //aggiornare determinati campi del libro
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-book-field/{bookId}")
    public ResponseEntity<?> updateBookFieldById(@PathVariable @Min(1) int bookId,
                                                 @RequestParam @NotBlank String fieldToUpdate,
                                                 @RequestParam(required = false) @Length(max = 50) String title,
                                                 @RequestParam(required = false) @Length(max = 50) String author,
                                                 @RequestParam(required = false) String plot,
                                                 @RequestParam(required = false) @Length(max = 20) String genre,
                                                 @RequestParam(required = false) @Length(max = 13) String ISBN)
    {
        return bookService.updateBookFieldById(bookId, fieldToUpdate, title, author, plot, genre, ISBN);
    }

    //aggiungere un libro
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-book")
    public ResponseEntity<?> addBook(@RequestBody @Valid BookRequest request)
    {
        return bookService.addBook(request);
    }

    //eliminare un libro tramite id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("delete-book/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable @Min(1) int bookId)
    {
        return bookService.deleteBookById(bookId);
    }
























//    @PutMapping("/update-book-title/{bookId}")
//    public ResponseEntity<?>updateBookTitleById(@RequestParam @Length(max = 50) @NotBlank String title, int bookId)
//    {
//        return bookService.updateBookTitleById(title, bookId);
//    }
//
//    @PutMapping("/update-book-author/{bookId}")
//    public ResponseEntity<?>updateBookAuthorById(@RequestParam @Length(max = 50) @NotBlank String author, int bookId)
//    {
//        return bookService.updateBookAuthorById(author, bookId);
//    }
//
//    @PutMapping("/update-book-plot/{bookId}")
//    public ResponseEntity<?>updateBookPlotById(String plot, int bookId)
//    {
//        return bookService.updateBookPlotById(plot, bookId);
//    }
//
//    @PutMapping("/update-book-genre/{bookId}")
//    public ResponseEntity<?>updateBookGenreById(@RequestParam @Length(max = 20) @NotBlank String genre, int bookId)
//    {
//        return bookService.updateBookGenreById(genre, bookId);
//    }
//
//    @PutMapping("/update-book-ISBN/{bookId}")
//    public ResponseEntity<?>updateBookIsbnById(@RequestParam @Length(max = 13) @NotBlank String ISBN, int bookId)
//    {
//        return bookService.updateBookIsbnById(ISBN, bookId);
//    }

}
