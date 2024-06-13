package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.BookMapper;
import com.nico.library.entity.Book;
import com.nico.library.exceptions.custom.EmptyListException;
import com.nico.library.exceptions.custom.BadRequestException;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
import com.nico.library.dto.request.book.BookRequest;
import com.nico.library.dto.response.book.BookResponse;
import com.nico.library.repository.BookRepository;
import com.nico.library.service.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Il metodo restituisce i libri disponibili all'interno della libreria.
     * Trova i libri tramite il metodo findAll() della Repository, che vengono inseriti in una List.
     *
     * @return la lista dei libri disponibili
     */
    public List<BookResponse> getAvailableBooks() {
        //ottengo prima la lista di libri
        List<Book> bookList = bookRepository.findAll();

        //se la lista è vuota, lancio un messaggio di errore
        if(bookList.isEmpty()) {
            throw new EmptyListException("books");
        }

        // Utilizzo il mapper per convertire l'entità Book in un DTO BookResponse
        return bookMapper.asResponseList(bookList);
    }


    /**
     * Questo metodo restituisce il libro trovato per id.
     * Vengono utilizzati gli Optional per la gestione eventualmente del libro non trovato.
     * @param bookId l'id del libro
     * @return il libro trovato per ID
     */
    public BookResponse getBookById(int bookId)
    {
        //utilizzo optional per gestire una query che potrebbe ritornarmi un oggetto Book null
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        return bookMapper.asResponse(book);
    }

    /**
     * Questo metodo restituisce una lista di libri trovati per genere.
     * Viene creata una lista di BookResponse popolata attraverso una query JPQL e restituita in formato JSON.
     * @param genre l'attributo per cui cercare
     * @return la lista di BookResponse in base al genere
     */
    public List<BookResponse> getBookByGenre(String genre)
    {
        //Creo una lista di BookResponse di libri trovati per genere tramite una query.
        List<Book> bookByGenreIgnoreCase = bookRepository.getBookByGenreIgnoreCase(genre);

        //Se la lista è vuota significa che non ho trovato nessun libro e lancio un messaggio di errore.
        if(bookByGenreIgnoreCase.isEmpty())
        {
            throw new ResourceNotFoundException("Books", "genre", genre);
        }

        //Altrimenti restituisco la lista di libri trovati per genere utilizzando il mapper
        return bookMapper.asResponseList(bookByGenreIgnoreCase);
    }

    /**
     * Questo metodo prende come parametro una BookRequest con i parametri da aggiornare e l'ID del libro corrispondente.
     * Una volta trovato il libro, aggiorna tutti gli attributi del libro
     * @param request l'oggetto BookRequest contenente le informazioni del libro.
     * @param bookId
     * @return una ResponseEntity che conferma l'aggiornamento del libro.
     */
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

        //Controllo prima che il libro esista tramite una query per ID.
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        //Utilizzo uno switch per gestire l'attributo da modificare
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

    /**
     * Questo metodo permette di aggiungere un nuovo libro al DB principale.
     * Viene semplicemente creato un nuovo libro, i cui attributi vengono recuperati dalla BookRequest.
     * @param request Un oggetto BookRequest contenente le informazioni del libro
     * @return una ResponseEntity di successo
     */

    public ResponseEntity<?> addBook(BookRequest request) {
        //Verifico prima che un libro con lo stesso codice ISBN non sia già presente
        if (bookRepository.findByISBN(request.getISBN()).isPresent()) {
            throw new BadRequestException(String.format("Book with isbn '%s' already present!", request.getISBN()));
        }

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

    /**
     * Questo metodo prende come parametro l'id del libro da eliminare, controlla che l'id del libro esista
     * e procede con l'eliminazione dello stesso.
     * @param bookId l'id del libro da cancellare
     * @return ResponseEntity di successo.
     */
    @Transactional
    public ResponseEntity<?> deleteBookById(int bookId)
    {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        bookRepository.deleteById(bookId);

        return new ResponseEntity<>("Book with id " + book.getBookId() + " successfully deleted!", HttpStatus.OK);
    }
}
