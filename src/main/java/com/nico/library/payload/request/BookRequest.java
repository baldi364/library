package com.nico.library.payload.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import javax.xml.transform.Source;

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

    @Column(name = "ISBN", length = 13, nullable = false, unique = true)
    private String ISBN;
}
