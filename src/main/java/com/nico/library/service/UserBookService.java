package com.nico.library.service;

import com.nico.library.entity.Book;
import com.nico.library.entity.User;
import com.nico.library.entity.UserBook;
import com.nico.library.entity.UserBookId;
import com.nico.library.exception.ResourceNotFoundException;
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

    public ResponseEntity<?> addUserBook(UserDetails userDetails, int bookId)
    {
        //Mi ricavo l'utente
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        //e poi il libro
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        //Creo entrambi gli UserBook
        UserBookId userBookId = new UserBookId(user, book);
        UserBook userBook = new UserBook(userBookId, 0);

        //Setto l'addDate e salvo
        userBook.setAddDate(LocalDateTime.now());
        userBookRepository.save(userBook);
        return new ResponseEntity<>("Book with id " + bookId + " added successfully to user " + username, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getUserBooks(UserDetails userDetails)
    {
        //Trovo prima l'utente
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

       //Tramite una query trovo solo i libri con delete_date a null legati ad un utente
       List<UserBook> userBookList = userBookRepository.findActiveBooksByUser(user);
       List<UserBookResponse> userBookResponseList = new ArrayList<>();

       //Se trovo gli utenti, popolo la lista di risposta
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
           return new ResponseEntity<>("No books available for user " + username, HttpStatus.NOT_FOUND);
       }
        return new ResponseEntity<>(userBookResponseList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUserBook(UserDetails userDetails, int bookId)
    {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", username));

        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        if (optionalUserBook.isPresent())
        {
            UserBook userBook = optionalUserBook.get();
            userBook.setDeleteDate(LocalDateTime.now());
            userBookRepository.save(userBook);
        }
        else
        {
            return new ResponseEntity<>("No book with id " + bookId + " found for user " + username, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Book with id " + bookId + " successfully deleted from " + username + "'s library", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateBookReadCount(UserDetails userDetails, int bookId, int readCount)
    {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", username));

        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        if(optionalUserBook.isPresent())
        {
            UserBook userBook = optionalUserBook.get();
            userBook.setReadCount(readCount);
            userBookRepository.save(userBook);
        }
        else
        {
            return new ResponseEntity<>("No book with id " + bookId + " found for user " + username, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Read count of book with id " + bookId + " updated with value " + readCount, HttpStatus.OK);
    }
}
