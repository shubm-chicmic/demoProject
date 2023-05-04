package com.example.demoProject.Repository;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {

    public Roles findByRoleName(String roleName);





}
