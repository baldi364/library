package com.nico.library.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserBookResponse
{
    private Long id;
    private String title;
    private String author;
    private String plot;
    private String genre;
    private String ISBN;
    private LocalDateTime addDate;
    private int readCount;

    public UserBookResponse(Long id, String title, String author, String plot, String genre, String ISBN, LocalDateTime addDate, int readCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.genre = genre;
        this.ISBN = ISBN;
        this.addDate = addDate;
        this.readCount = readCount;
    }
}
