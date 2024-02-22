package com.nico.library.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookIdResponse
{
    private int bookId;
    private String title;
    private String author;
    private String plot;
    private String genre;
    private String ISBN;
}
