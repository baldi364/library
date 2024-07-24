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
     * Adds a book to the library of an authenticated user.
     *
     * <p>
     * This method retrieves the authenticated user and the specified book by their IDs.
     * If either the user or the book is not found, a {@link ResourceNotFoundException} is thrown.
     * It also checks if the book is already present in the user's library and, if so, throws a {@link BookAlreadyPresentException}.
     * The method then maps the user and book to a {@link UserBook} entity, sets the addition date, saves the entity, and returns a {@link UserBookResponse}.
     * </p>
     *
     * @param userDetails the details of the authenticated user.
     * @param bookId the ID of the book to add to the user's library.
     * @return a mapped {@link UserBookResponse} containing the added book.
     * @throws ResourceNotFoundException if the user or the specified book does not exist in the system.
     * @throws BookAlreadyPresentException if the book is already present in the user's library.
     */
    public UserBookResponse addUserBook(UserDetails userDetails, int bookId) {

        String username = userDetails.getUsername();

        //Retrieve authenticated user and book
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        //Check if book is already present in user's library
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);
        if (optionalUserBook.isPresent()) {
            throw new BookAlreadyPresentException(bookId, username);
        }

        UserBook userBook = userBookMapper.asEntity(user, book);

        //Set the addDate and save
        userBook.setAddDate(LocalDateTime.now());
        userBookRepository.save(userBook);

        return userBookMapper.asResponse(userBook);
    }

    /**
     * Returns the list of books associated with the authenticated user.
     *
     * <p>
     * This method fetches the user by their username from the authenticated user details,
     * and then retrieves the list of active books associated with that user.
     * If the user is not found or has no associated books, it throws appropriate exceptions.
     * </p>
     *
     * @param userDetails the details of the authenticated user.
     * @return the list of books associated with the user.
     * @throws ResourceNotFoundException if the user does not exist in the system.
     * @throws EmptyListException if the user has no associated books.
     */
    public List<UserBookResponse> getUserBooks(UserDetails userDetails) {

        // Retrieve the user by their username from the authenticated user details
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve only the active books associated with the user using a custom query
        List<UserBook> userBookList = userBookRepository.findActiveBooksByUser(user);

        if (userBookList.isEmpty()) {
            throw new EmptyListException("books", username);
        }

        // Map the list of UserBook entities to UserBookResponse DTOs and return
        return userBookMapper.asResponseList(userBookList);
    }

    /**
     * Deletes a book from the library of the authenticated user.
     *
     * <p>
     * This method retrieves the authenticated user and checks if the specified book is present in the user's library.
     * If the user or the book is not found, a {@link ResourceNotFoundException} is thrown.
     * If the book is found, the deletion date is set, and the book is marked as deleted.
     * </p>
     *
     * @param userDetails the details of the authenticated user.
     * @param bookId the ID of the book to be removed from the user's library.
     * @throws ResourceNotFoundException if the user does not exist in the system or if the book is not present in the user's library.
     */
    public void deleteUserBook(UserDetails userDetails, int bookId) {

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve the book by its ID and check if it is present in the user's library
        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        // If the book is not present, throw a ResourceNotFoundException
        if (optionalUserBook.isEmpty()) {
            throw new ResourceNotFoundException("Book", "id", bookId, username);
        }

        // If the book is found, set the deletion date and mark it as deleted
        UserBook userBook = optionalUserBook.get();
        userBook.setDeleteDate(LocalDateTime.now());
        userBookRepository.save(userBook);
    }

    /**
     * Updates the read count of a specific book in the authenticated user's library.
     *
     * <p>
     * This method retrieves the authenticated user and checks if the specified book is present in the user's library.
     * If the user or the book is not found, a {@link ResourceNotFoundException} is thrown.
     * If the book is found, the read count is updated and the changes are saved.
     * </p>
     *
     * @param userDetails the details of the authenticated user.
     * @param bookId the ID of the book to update.
     * @param readCount the new read count for the book.
     * @return a {@link UserBookResponse} confirming the update of the book's read count, if successful.
     * @throws ResourceNotFoundException if the user does not exist in the system or if the book is not present in the user's library.
     */
    @Transactional
    public UserBookResponse updateBookReadCount(UserDetails userDetails, int bookId, int readCount) {

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Optional<UserBook> optionalUserBook = userBookRepository.getBookByIdAndUsername(bookId, user);

        if (optionalUserBook.isEmpty()) {
            throw new ResourceNotFoundException("Book", "id", bookId, username);
        }

        // Update the read count for the book and save the changes
        UserBook userBook = optionalUserBook.get();
        userBook.setReadCount(readCount);
        userBookRepository.save(userBook);

        return userBookMapper.asResponse(userBook);
    }
}