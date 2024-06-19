package com.nico.library.repository;

import com.nico.library.entity.User;
import com.nico.library.entity.UserBook;
import com.nico.library.entity.UserBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, UserBookId>
{
    @Query("SELECT ub " +
           "FROM UserBook ub " +
           "WHERE ub.userBookId.user = :user " +
           "AND ub.deleteDate IS NULL " +
           "ORDER BY ub.userBookId.book.bookId ASC")
    List<UserBook> findActiveBooksByUser(User user);

    @Query("SELECT ub " +
           "FROM UserBook ub " +
           "WHERE ub.userBookId.book.bookId = :bookId " +
           "AND ub.userBookId.user = :user " +
           "AND ub.deleteDate IS NULL")
    Optional<UserBook> getBookByIdAndUsername(int bookId, User user);
}
