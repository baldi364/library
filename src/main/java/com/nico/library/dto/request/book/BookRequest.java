package com.nico.library.dto.request.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest
{
    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String author;

    private String plot;

    @Column(length = 20, nullable = false)
    private String genre;

    @JsonProperty("ISBN")
    @Column(name = "ISBN", length = 13, nullable = false, unique = true)
    private String ISBN;
}
