package com.nico.library.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserBookResponse
{
    @JsonProperty(index = 1)
    private Long id;

    @JsonProperty(index = 2)
    private String title;

    @JsonProperty(index = 3)
    private String author;

    @JsonProperty(index = 4)
    private String plot;

    @JsonProperty(index = 5)
    private String genre;

    @JsonProperty(index = 6)
    private String ISBN;

    @JsonProperty(index = 7)
    private LocalDateTime addDate;

    @JsonProperty(index = 8)
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
