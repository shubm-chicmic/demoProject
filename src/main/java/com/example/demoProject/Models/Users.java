package com.example.demoProject.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users",uniqueConstraints={@UniqueConstraint(columnNames={"email", "phoneNo"})})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

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

    Boolean isSuspend;
    Boolean isDelete;
    Boolean isEmailVerify;

}
