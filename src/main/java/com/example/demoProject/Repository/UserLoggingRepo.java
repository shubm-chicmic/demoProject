package com.example.demoProject.Repository;


import com.example.demoProject.Models.UsersActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoggingRepo extends JpaRepository<UsersActivity, Integer> {
}
