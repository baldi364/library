package com.nico.library.dto.mapper;

import com.nico.library.dto.request.book.BookRequest;
import com.nico.library.dto.response.book.BookResponse;
import com.nico.library.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book asEntity (BookResponse response);
    @Mapping(source = "bookId", target = "id")
    BookResponse asResponse (Book book);
    List<BookResponse> asResponseList(List<Book> books);
    @Mapping(target = "bookId", ignore = true)
    void updateBookFromRequest(BookRequest request, @MappingTarget Book book);
}
