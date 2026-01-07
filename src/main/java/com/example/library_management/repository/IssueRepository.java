package com.example.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.library_management.model.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
