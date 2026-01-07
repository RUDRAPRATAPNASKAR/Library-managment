package com.example.library_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;

import com.example.library_management.model.Book;
import com.example.library_management.model.User;
import com.example.library_management.repository.BookRepository;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // ADMIN CHECK
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        return user != null && "ADMIN".equals(user.getRole());
    }

    // ADD BOOK PAGE (ADMIN ONLY)
    @GetMapping("/add")
    public String addBookPage(HttpSession session, Model model) {

        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        Book book = new Book();
        book.setQuantity(1);
        model.addAttribute("book", book);

        return "add-book";
    }

    // SAVE BOOK (ADMIN ONLY)
    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        if (result.hasErrors()) {
            return "add-book";
        }

        bookRepository.save(book);
        return "redirect:/books";
    }

    // VIEW BOOKS (ALL USERS)
    @GetMapping
    public String viewBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "books";
    }

    // DELETE BOOK (ADMIN ONLY)
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}
