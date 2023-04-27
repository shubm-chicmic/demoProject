package com.example.demoProject.Dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    int id;
    String firstName;
    String lastName;
    String phone;
    String email;
    String imageUrl = "";
    String city;
    String password;
    String role;
    Boolean isSuspend;
    Boolean isDelete;

}

