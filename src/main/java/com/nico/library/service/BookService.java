package com.nico.library.service;

import com.nico.library.entity.Book;
import com.nico.library.exception.ResourceNotFoundException;
import com.nico.library.payload.request.BookRequest;
import com.nico.library.payload.response.BookIdResponse;
import com.nico.library.payload.response.BookResponse;
import com.nico.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService
{
    private final BookRepository bookRepository;

    /**
     * Il metodo restituisce i libri disponibili all'interno della libreria.
     * Trova i libri tramite il metodo findAll() della Repository, che vengono inseriti in una List.
     * Viene creata un'altra List di BookResponse che viene popolata, attraverso un for each, con i libri trovati nella lista precedente
     * @return la lista dei libri disponibili
     */
    public ResponseEntity<?> getAvailableBooks()
    {
        List<Book> bookList = bookRepository.findAll(); //ottengo prima la lista di libri
        List<BookResponse> bookResponseList = new ArrayList<>();
        if(!bookList.isEmpty()) //se la lista non è vuota, procedo
        {
            for(Book book : bookList)
            {
                BookResponse bookResponse = new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPlot(),
                        book.getGenre(),
                        book.getISBN()
                );
                bookResponseList.add(bookResponse);
            }
        }
        else
        {
            return new ResponseEntity<>("There are no books available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookResponseList, HttpStatus.OK);
    }


    /**
     * Questo metodo restituisce il libro trovato per id.
     * Vengono utilizzati gli Optional per la gestione eventualmente del libro non trovato.
     * @param bookId
     * @return il libro trovato per ID
     */
    public ResponseEntity<?> getBookById(int bookId)
    {
        Optional<Book> optionalBook = bookRepository.findById(bookId); //utilizzo optional per gestire una query che potrebbe dare null
        if(optionalBook.isEmpty())
        {
            return new ResponseEntity<>("There is no book with id " + bookId, HttpStatus.NOT_FOUND);
        }

        Book book = optionalBook.get();
        BookResponse bookResponse = new BookResponse(
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPlot(),
                    book.getGenre(),
                    book.getISBN()
        );
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    /**
     * Questo metodo restituisce una lista di libri trovati per genere.
     * Viene creata una lista di BookResponse popolata attraverso una query JPQL e restituita in formato JSON.
     * @param genre
     * @return la lista di Book in base al genere
     */
    public ResponseEntity<?> getBookByGenre(String genre)
    {
        List<BookResponse> bookResponseList = bookRepository.getBookByGenre(genre);
        if(bookResponseList.isEmpty())
        {
            return new ResponseEntity<>("There are no books with genre " + genre, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookResponseList, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateBookById(BookRequest request, int bookId)
    {
        //trovo prima il libro per id, mi assicuro che esista altrimenti lancio una ResourceNotFoundException
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        //una volta trovato setto i parametri da modificare
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPlot(request.getPlot());
        book.setGenre(request.getGenre());
        book.setISBN(request.getISBN());

        return new ResponseEntity<>("Book with id " + book.getBookId() + " successfully update", HttpStatus.OK);
    }

    /**
     * Questo metodo permette all'utente di poter modificare un unico attributo tra quelli disponibili.
     * Viene prima verificato che il libro effettivamente esista tramite un semplice findById() e, tramite uno switch, viene controllato quale attributo
     * l'utente desidera aggiornare
     * @param bookId
     * @param fieldToUpdate
     * @param title
     * @param author
     * @param plot
     * @param genre
     * @param ISBN
     * @return ResponseEntity di successo
     */
    @Transactional
    public ResponseEntity<?> updateBookFieldById(int bookId,
                                                 String fieldToUpdate,
                                                 String title,
                                                 String author,
                                                 String plot,
                                                 String genre,
                                                 String ISBN)
    {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        switch(fieldToUpdate.toLowerCase())
        {
            case "title" -> book.setTitle(title);
            case "author" -> book.setAuthor(author);
            case "plot" -> book.setPlot(plot);
            case "genre" -> book.setGenre(genre);
            case "isbn" -> book.setISBN(ISBN);
            default -> {
                return new ResponseEntity<>("Invalid field to update: " + fieldToUpdate, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Book with id " + book.getBookId() + " and field " + fieldToUpdate + " successfully update!", HttpStatus.OK);
    }

    public ResponseEntity<?> addBook(BookRequest request)
    {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getPlot(),
                request.getGenre(),
                request.getISBN()
        );

        bookRepository.save(book);
        return new ResponseEntity<>("Book with title \"" + request.getTitle() + "\" successfully added!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteBookById(int bookId)
    {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        bookRepository.deleteById(bookId);

        return new ResponseEntity<>("Book with id " + book.getBookId() + " successfully deleted!", HttpStatus.OK);
    }


//    @Transactional
//    public ResponseEntity<?> updateBookTitleById(String title, int bookId)
//    {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));
//
//        book.setTitle(title);
//
//        return new ResponseEntity<>("Book with new title \"" + title + "\" successfully update", HttpStatus.OK);
//    }
//
//    @Transactional
//    public ResponseEntity<?> updateBookAuthorById(String author, int bookId)
//    {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));
//
//        book.setAuthor(author);
//
//        return new ResponseEntity<>("Book with new author \"" + author + "\" successfully update", HttpStatus.OK);
//    }
//
//    @Transactional
//    public ResponseEntity<?> updateBookPlotById(String plot, int bookId)
//    {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));
//
//        book.setPlot(plot);
//
//        return new ResponseEntity<>("Book with new plot \"" + plot + "\" successfully update", HttpStatus.OK);
//    }
//
//    @Transactional
//    public ResponseEntity<?> updateBookGenreById(String genre, int bookId)
//    {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));
//
//        book.setGenre(genre);
//
//        return new ResponseEntity<>("Book with new genre \"" + genre + "\" successfully update", HttpStatus.OK);
//    }
//
//    @Transactional
//    public ResponseEntity<?> updateBookIsbnById(String ISBN, int bookId)
//    {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));
//
//        book.setISBN(ISBN);
//
//        return new ResponseEntity<>("Book with new ISBN " + ISBN + " successfully update", HttpStatus.OK);
//    }


}
