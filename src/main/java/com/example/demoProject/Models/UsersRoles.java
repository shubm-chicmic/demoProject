package com.example.demoProject.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UsersRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    Boolean isSuspend;
    Boolean isDelete;

    Boolean isEmailVerify;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "roles_id")
    Roles roles;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "users_id")
    Users users;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

}
