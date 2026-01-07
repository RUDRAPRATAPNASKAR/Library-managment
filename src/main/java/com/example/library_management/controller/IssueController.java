package com.example.library_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.library_management.model.Book;
import com.example.library_management.model.Issue;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.IssueRepository;

@Controller
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private BookRepository bookRepository;

    //OPEN ISSUE PAGE-MAPPING
    @GetMapping("/new")
    public String issuePage(@RequestParam Long bookId, Model model) {

        Book book = bookRepository.findById(bookId).orElse(null);

        if (book == null || book.getQuantity() <= 0) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        return "issue-book";
    }

    //SAVE ISSUE + DECREASE QUANTITY
    @PostMapping("/save")
    public String saveIssue(@RequestParam Long bookId,
                            @RequestParam String issuedTo) {

        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null && book.getQuantity() > 0) {
            // decrease quantity
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);

            // save issue
            Issue issue = new Issue();
            issue.setBookId(bookId);
            issue.setIssuedTo(issuedTo);
            issueRepository.save(issue);
        }

        return "redirect:/books";
    }

    //VIEW ISSUED BOOKS
    @GetMapping
    public String viewIssuedBooks(Model model) {
        model.addAttribute("issues", issueRepository.findAll());
        return "issued-books";
    }

    //RETURN BOOK (A9)
    @GetMapping("/return/{issueId}")
    public String returnBook(@PathVariable Long issueId) {

        Issue issue = issueRepository.findById(issueId).orElse(null);

        if (issue != null) {
            Book book = bookRepository.findById(issue.getBookId()).orElse(null);

            if (book != null) {
                book.setQuantity(book.getQuantity() + 1);
                bookRepository.save(book);
            }

            issueRepository.deleteById(issueId);
        }

        return "redirect:/issue";
    }
}
