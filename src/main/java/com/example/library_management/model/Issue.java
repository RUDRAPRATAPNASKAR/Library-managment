package com.example.library_management.model;

import jakarta.persistence.*;

@Entity
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We store ONLY bookId (not Book object)
    private Long bookId;

    private String issuedTo;

    //Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }
}
