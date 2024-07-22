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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Retrieves all available books.
     *
     * <p>
     * This method finds all available books through a findAll() query and returns a List of {@link BookResponse}.
     * If no books are found, throws a {@link EmptyListException}.
     * </p>
     * @return a List of {@link BookResponse}
     * @throws EmptyListException if no books are found.
     */
    public List<BookResponse> getAvailableBooks() {

        List<Book> bookList = bookRepository.findAll();

        if (bookList.isEmpty()) {
            throw new EmptyListException("books");
        }

        return bookMapper.asResponseList(bookList);
    }


    /**
     * Returns a specified book.
     *
     *<p>
     *This method retrieves a book by its unique identifier from the repository.
     *If no book is found, it throws a {@link ResourceNotFoundException}.
     *</p>
     *
     * @param bookId the unique identifier of the book you want to find.
     * @return  a {@link BookResponse} representing the found book.
     * @throws ResourceNotFoundException if no book with the specified ID is found.
     */
    public BookResponse getBookById(int bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        return bookMapper.asResponse(book);
    }

    /**
     * Returns a list of books of a specific genre.
     *
     *<p>
     *This method retrieves a list of books by its genre from the repository.
     *If no book is found, the method throws a {@link ResourceNotFoundException}.
     *</p>
     *
     * @param genre the specified genre of the books you want to find.
     * @return  a {@link BookResponse} representing the found books.
     * @throws ResourceNotFoundException if no book with the specified genre is found.
     */
    public List<BookResponse> getBookByGenre(String genre) {

        //A list of BookResponse found by genre ignoring case
        List<Book> bookByGenreIgnoreCase = bookRepository.getBookByGenreIgnoreCase(genre);

        if (bookByGenreIgnoreCase.isEmpty()) {
            throw new ResourceNotFoundException("Books", "genre", genre);
        }

        return bookMapper.asResponseList(bookByGenreIgnoreCase);
    }

    /**
     * Updates a book's attributes based on the provided {@link BookRequest} and book ID.
     * <p>
     * This method finds the book by its unique identifier (bookId) and updates
     * its attributes with the values provided in the {@link BookRequest}.
     * If the book is not found, a {@link ResourceNotFoundException} is thrown.
     * </p>
     *
     * @param request the {@link BookRequest} object containing the book's updated information.
     * @param bookId the unique identifier of the book to be updated.
     * @return a {@link BookResponse} object containing the updated book information.
     * @throws ResourceNotFoundException if no book with the specified ID is found.
     */
    @Transactional
    public BookResponse updateBookById(BookRequest request, int bookId) {

        //find book by id
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        // once found, use the mapper to update the existing book's attributes
        bookMapper.updateBookFromRequest(request, book);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.asResponse(updatedBook);
    }


    /**
     * Adds a new book to the repository.
     *
     * <p>
     * This method first checks if the provided ISBN is at least 13 characters long and contains only numeric digits.
     * If the ISBN is invalid, a {@link BadRequestException} is thrown. It then checks
     * if a book with the same ISBN already exists in the repository. If it does, a
     * {@link BadRequestException} is thrown. If both checks pass, the book is saved
     * to the repository and a {@link BookResponse} is returned.
     * </p>
     *
     * @param request the {@link BookRequest} containing the details of the book to be added.
     * @return a {@link BookResponse} containing the details of the added book.
     * @throws BadRequestException if the ISBN is invalid or if a book with the same ISBN already exists.
     */
    public BookResponse addBook(BookRequest request) {

        if(!isValidISBN(request.getISBN())){
            throw new BadRequestException(String.format("Invalid ISBN '%s': it must be at least 13 digits long and contain only digits.", request.getISBN()));
        }

        if (bookRepository.findByISBN(request.getISBN()).isPresent()) {
            throw new BadRequestException(String.format("Book with isbn '%s' already present!", request.getISBN()));
        }

        Book createdBook = bookMapper.asEntity(request);
        bookRepository.save(createdBook);

        return bookMapper.asResponse(createdBook);
    }

    /**
     * Deletes a book based on its unique identifier (bookId).
     * <p>
     * This method finds the book by its unique identifier and deletes it from the repository.
     * If the book is not found, a {@link ResourceNotFoundException} is thrown.
     * </p>
     *
     * @param bookId the unique identifier of the book to be deleted.
     * @throws ResourceNotFoundException if no book with the specified ID is found.
     */
    @Transactional
    public void deleteBookById(int bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));

        bookRepository.deleteById(bookId);
    }

    /**
     * Verifies that the given ISBN is at least 13 characters long and contains only numeric digits.
     *
     * @param ISBN the ISBN string to validate.
     * @return true if the ISBN is at least 13 characters long and contains only digits, false otherwise.
     */
    public boolean isValidISBN(String ISBN){
        if(ISBN.length() < 13){
            return false;
        }
        return ISBN.chars().allMatch(Character::isDigit);
    }
}