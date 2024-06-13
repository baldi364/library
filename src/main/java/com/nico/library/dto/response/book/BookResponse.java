package com.nico.library.dto.response.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String plot;
    private String genre;
    private String ISBN;
}
