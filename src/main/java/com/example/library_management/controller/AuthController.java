package com.example.library_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;

import java.util.Optional;

import com.example.library_management.model.User;
import com.example.library_management.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    //  Login page
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    //  Signup page
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // REGISTER USER
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "signup";
        }

        //normalize email
        String email = user.getEmail().toLowerCase();

        // CHECK DUPLICATE EMAIL
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "signup";
        }

        user.setEmail(email);
        user.setRole("USER");   // DEFAULT ROLE
        userRepository.save(user);

        return "redirect:/";
    }

    //LOGIN
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<User> optionalUser =
                userRepository.findByEmail(email.toLowerCase());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                session.setAttribute("loggedUser", user);
                return "redirect:/books";
            }
        }

        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    //LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
