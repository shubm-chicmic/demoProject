package com.example.demoProject.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Roles")//,uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String roleName;

    @OneToMany(mappedBy = "roles" ,fetch = FetchType.EAGER)
    Set<UsersRoles> usersRoles;



}
