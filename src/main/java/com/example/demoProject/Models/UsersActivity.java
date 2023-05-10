package com.example.demoProject.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "UsersActivity")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String url;
    String message;
    @CreationTimestamp
    LocalDateTime localDatetime;
}
