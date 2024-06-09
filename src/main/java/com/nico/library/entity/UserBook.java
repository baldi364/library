package com.nico.library.entity;

import com.nico.library.entity.common.Creation;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "user_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBook extends Creation
{

    @EmbeddedId
    private UserBookId userBookId;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int readCount;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBook userBook = (UserBook) o;
        return Objects.equals(userBookId, userBook.userBookId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userBookId);
    }
}
