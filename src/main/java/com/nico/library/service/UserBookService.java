package com.nico.library.service;

import com.nico.library.entity.Book;
import com.nico.library.entity.User;
import com.nico.library.entity.UserBook;
import com.nico.library.entity.UserBookId;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
import com.nico.library.payload.response.UserBookResponse;
import com.nico.library.repository.BookRepository;
import com.nico.library.repository.UserBookRepository;
import com.nico.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBookService
{
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;

    /**
     * Questo metodo ggiunge un libro alla libreria di un utente.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @param bookId      L'ID del libro da aggiungere alla libreria.
     * @return Una ResponseEntity contenente un messaggio di conferma dell'aggiunta del libro alla libreria dell'utente.
     * @throws ResourceNotFoundException Se l'utente o il libro specificato non esistono nel sistema.
     */
    public ResponseEntity<?> addUserBook(UserDetails userDetails, int bookId)
    {
        //Mi ricavo l'utente autenticato e lo cerco nel repository
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        //poi il libro
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        //Verifico che il libro non sia già presente nella libreria dell'utente
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);
        if(optionalUserBook.isPresent())
        {
            return new ResponseEntity<>("Book with id " + bookId + " already present in " + user.getUsername() + "'s library", HttpStatus.BAD_REQUEST);
        }

        //Creo entrambi gli UserBook
        UserBookId userBookId = new UserBookId(user, book);
        UserBook userBook = new UserBook(userBookId, 0);

        //Setto l'addDate e salvo
        userBook.setAddDate(LocalDateTime.now());
        userBookRepository.save(userBook);

        return new ResponseEntity<>("Book with id " + bookId + " added successfully to user " + username, HttpStatus.CREATED);
    }

    /**
     * Restituisce la lista dei libri associati all'utente autenticato.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @return Una ResponseEntity contenente la lista dei libri associati all'utente, se presenti.
     * @throws ResourceNotFoundException Se l'utente non esiste nel sistema o non ha libri associati.
     */
    public ResponseEntity<?> getUserBooks(UserDetails userDetails)
    {
        //Trovo prima l'utente
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

       //Tramite una query trovo solo i libri con delete_date a null associati ad un utente
       List<UserBook> userBookList = userBookRepository.findActiveBooksByUser(user);
       List<UserBookResponse> userBookResponseList = new ArrayList<>();

        // Se l'utente ha libri associati, popolo la lista di risposta con le informazioni dei libri
       if(!userBookList.isEmpty())
       {
            for(UserBook userBook : userBookList)
            {
                Book book = userBook.getUserBookId().getBook();
                UserBookResponse userBookResponse = new UserBookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPlot(),
                        book.getGenre(),
                        book.getISBN(),
                        userBook.getAddDate(),
                        userBook.getReadCount()
                );
                userBookResponseList.add(userBookResponse);
            }
       }
       else
       {
           // Se l'utente non ha libri associati, restituisce una ResponseEntity con un messaggio di errore
           return new ResponseEntity<>("No books available for user " + username, HttpStatus.NOT_FOUND);
       }
        return new ResponseEntity<>(userBookResponseList, HttpStatus.OK);
    }

    /**
     * Elimina un libro dalla libreria dell'utente autenticato.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @param bookId      L'ID del libro da eliminare dalla libreria dell'utente.
     * @return Una ResponseEntity che conferma l'eliminazione del libro dalla libreria dell'utente, se avvenuta con successo.
     * @throws ResourceNotFoundException Se l'utente non esiste nel sistema o se il libro non è presente nella libreria dell'utente.
     */
    public ResponseEntity<?> deleteUserBook(UserDetails userDetails, int bookId)
    {
        // Recupero l'utente autenticato dal repository degli utenti
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", username));

        // Recupero il libro dall'ID fornito e verifico se è presente nella libreria dell'utente
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        // Se il libro è presente nella libreria dell'utente, imposto la data di eliminazione e salvo
        if (optionalUserBook.isPresent())
        {
            UserBook userBook = optionalUserBook.get();
            userBook.setDeleteDate(LocalDateTime.now());
            userBookRepository.save(userBook);
        }
        else
        {
            // Se il libro non è presente nella libreria dell'utente, restituisco una ResponseEntity con un messaggio di errore
            return new ResponseEntity<>("No book with id " + bookId + " found for user " + username, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Book with id " + bookId + " successfully deleted from " + username + "'s library", HttpStatus.OK);
    }

    /**
     * Aggiorna il numero di volte che un determinato libro è stato letto nella libreria dell'utente autenticato.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @param bookId      L'ID del libro da aggiornare.
     * @param readCount   Il nuovo numero di volte che il libro è stato letto.
     * @return Una ResponseEntity che conferma l'aggiornamento del conteggio delle letture del libro, se avvenuto con successo.
     * @throws ResourceNotFoundException Se l'utente non esiste nel sistema o se il libro non è presente nella libreria dell'utente.
     */
    @Transactional
    public ResponseEntity<?> updateBookReadCount(UserDetails userDetails, int bookId, int readCount)
    {
        // Recupero l'utente autenticato dal repository degli utenti
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", username));

        // Recupero il libro dall'ID fornito e verifico se è presente nella libreria dell'utente
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        // Se il libro è presente nella libreria dell'utente, aggiorno il conteggio delle letture e salvo
        if(optionalUserBook.isPresent())
        {
            UserBook userBook = optionalUserBook.get();
            userBook.setReadCount(readCount);
            userBookRepository.save(userBook);
        }
        else
        {
            // Se il libro non è presente nella libreria dell'utente, restituisco una ResponseEntity con un messaggio di errore
            return new ResponseEntity<>("No book with id " + bookId + " found for user " + username, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Read count of book with id " + bookId + " updated with value " + readCount, HttpStatus.OK);
    }
}
