package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private boolean rented;

    public Book() {}

    public Book(String title, String author, boolean rented) {
        this.title = title;
        this.author = author;
        this.rented = rented;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isRented() { return rented; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setRented(boolean rented) { this.rented = rented; }
}
