package com.example.demoProject.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Vehicle")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    double rate;

    String imageUrl;


    @OneToMany(mappedBy = "vehicle",fetch = FetchType.EAGER)
    List<UsersRoles> usersRoles;

}
