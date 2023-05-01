package com.example.demoProject.Repository;

import com.example.demoProject.Models.UserUuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUuidRepository extends JpaRepository<UserUuid, Integer> {
    UserUuid getUserTokenByUuid(String uuid);
    UserUuid getUserUuidByEmail(String email);
    int deleteByUuid(String uuid);
}
