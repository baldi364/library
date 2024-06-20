package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.UserBookMapper;
import com.nico.library.entity.Book;
import com.nico.library.entity.User;
import com.nico.library.entity.UserBook;
import com.nico.library.exceptions.custom.BookAlreadyPresentException;
import com.nico.library.exceptions.custom.EmptyListException;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
import com.nico.library.dto.response.user.UserBookResponse;
import com.nico.library.repository.BookRepository;
import com.nico.library.repository.UserBookRepository;
import com.nico.library.repository.UserRepository;
import com.nico.library.service.UserBookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final UserBookMapper userBookMapper;

    /**
     * Questo metodo aggiunge un libro alla libreria di un utente.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @param bookId      L'ID del libro da aggiungere alla libreria.
     * @return Una UserBookResponse mappata, con il libro aggiunto
     * @throws ResourceNotFoundException Se l'utente o il libro specificato non esistono nel sistema.
     */
    public UserBookResponse addUserBook(UserDetails userDetails, int bookId) {

        String username = userDetails.getUsername();

        //Mi ricavo l'utente autenticato e il libro
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        //Verifico che il libro non sia già presente nella libreria dell'utente
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);
        if (optionalUserBook.isPresent()) {
            throw new BookAlreadyPresentException(bookId, username);
        }

        UserBook userBook = userBookMapper.asEntity(user, book);

        //Setto l'addDate e salvo
        userBook.setAddDate(LocalDateTime.now());
        userBookRepository.save(userBook);

        return userBookMapper.asResponse(userBook);
    }

    /**
     * Restituisce la lista dei libri associati all'utente autenticato.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @return La lista dei libri associati all'uente
     * @throws ResourceNotFoundException Se l'utente non esiste nel sistema o non ha libri associati.
     */
    public List<UserBookResponse> getUserBooks(UserDetails userDetails) {

        //Trovo prima l'utente
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        //Tramite una query trovo solo i libri con delete_date a null associati ad un utente
        List<UserBook> userBookList = userBookRepository.findActiveBooksByUser(user);

        if (userBookList.isEmpty()) {
            throw new EmptyListException("books", username);
        }

        return userBookMapper.asResponseList(userBookList);
    }

    /**
     * Elimina un libro dalla libreria dell'utente autenticato.
     *
     * @param userDetails Le informazioni dell'utente autenticato.
     * @param bookId      L'ID del libro da eliminare dalla libreria dell'utente.
     * @throws ResourceNotFoundException Se l'utente non esiste nel sistema o se il libro non è presente nella libreria dell'utente.
     */
    public void deleteUserBook(UserDetails userDetails, int bookId) {

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Recupero il libro dall'ID fornito e verifico se è presente nella libreria dell'utente
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        // Se non è presente restituisco un messaggio di errore
        if (optionalUserBook.isEmpty()) {
            throw new ResourceNotFoundException("Book", "id", bookId, username);
        }

        // Altrimenti imposto la data di eliminazione e salvo
        UserBook userBook = optionalUserBook.get();
        userBook.setDeleteDate(LocalDateTime.now());
        userBookRepository.save(userBook);
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
    public UserBookResponse updateBookReadCount(UserDetails userDetails, int bookId, int readCount) {

        // Recupero l'utente autenticato dal repository degli utenti
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Recupero il libro dall'ID fornito e verifico se è presente nella libreria dell'utente
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        // Se non è presente restituisco un messaggio di errore
        if (optionalUserBook.isEmpty()) {
            throw new ResourceNotFoundException("Book", "id", bookId, username);
        }

        UserBook userBook = optionalUserBook.get();
        userBook.setReadCount(readCount);
        userBookRepository.save(userBook);

        return userBookMapper.asResponse(userBook);
    }
}