package com.example.demoProject.Dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data

public class EmailDto {
    String email;
    String password;

    public EmailDto() {
        System.out.println("\u001B[33m" +"bean created" + "\u001B[0m");
    }
}
