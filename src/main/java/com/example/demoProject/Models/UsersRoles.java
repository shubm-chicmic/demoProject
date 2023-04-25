package com.example.demoProject.Models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "UsersRoles")
@Data
@Builder
public class UsersRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String Roles;

}
