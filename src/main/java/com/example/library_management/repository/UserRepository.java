package com.example.library_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library_management.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
