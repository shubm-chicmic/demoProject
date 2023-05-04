package com.example.demoProject.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Users",uniqueConstraints={@UniqueConstraint(columnNames={"email", "phoneNo"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String uuid;
    String firstName;
    String lastName;
    @Email(
            message = "Invalid Email Enter !"
    )
    String email;
    String phoneNo;
    String password;
    String city;
    String imageUrl;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    Set<UsersRoles> usersRoles;







}
