package com.example.demoProject.Models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "UserUuid")
@Data
public class UserUuid {
    String uuid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String email;
}
