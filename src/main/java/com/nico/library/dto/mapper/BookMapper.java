package com.nico.library.dto.mapper;

import com.nico.library.dto.response.book.BookResponse;
import com.nico.library.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book asEntity (BookResponse response);
    @Mapping(source = "bookId", target = "id")
    BookResponse asResponse (Book book);
    List<BookResponse> asResponseList(List<Book> books);
}
