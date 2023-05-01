package com.example.demoProject.Repository;

import com.example.demoProject.Models.PasswordReset;
import com.example.demoProject.Models.UserUuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepo extends JpaRepository<PasswordReset, Integer> {
    PasswordReset getPasswordResetByUuid(String uuid);
    PasswordReset getPasswordResetByEmail(String email);
    int deleteByUuid(String uuid);
    void deleteByEmail(String email);
}
