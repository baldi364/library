package com.nico.library.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BookResponse
{
    private Long id;
    private String title;
    private String author;
    private String plot;
    private String genre;
    private String ISBN;

    public BookResponse(Long id, String title, String author, String plot, String genre, String ISBN) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.genre = genre;
        this.ISBN = ISBN;
    }
}
