package com.nico.library.dto.mapper;

import com.nico.library.dto.response.user.UserBookResponse;
import com.nico.library.entity.UserBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserBookMapper {
    @Mapping(source = "userBookId.book.bookId", target = "id")
    @Mapping(source = "userBookId.book.title", target = "title")
    @Mapping(source = "userBookId.book.author", target = "author")
    @Mapping(source = "userBookId.book.plot", target = "plot")
    @Mapping(source = "userBookId.book.genre", target = "genre")
    @Mapping(source = "userBookId.book.ISBN", target = "ISBN")
    @Mapping(source = "addDate", target = "addDate")
    @Mapping(source = "readCount", target = "readCount")
    UserBookResponse asResponse(UserBook userBook);
    List<UserBookResponse> asResponseList(List<UserBook> userBook);
}
