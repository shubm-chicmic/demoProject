package com.example.demoProject.Repository;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByEmail(String email);

    @Query(
            "Select user From Users user Where user.email = :email OR user.phoneNo = :phoneNo"
    )
    Users findUsersByEmailorPhoneNo(@Param("email") String email, @Param("phoneNo") String phoneNo);

    @Transactional
    @Modifying
    @Query(
            value = "Update Users us Set us.firstName = :firstName, us.lastName = :lastName, us.phoneNo = :phoneNo Where us.email = :email"
    )
    void updateUserByEmail(@Param("email") String email, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("phoneNo") String phoneNo);//, @Param("lName") String lName, @Param("phone") String phone);
    Users findUsersById(@Param("id") int id);
    @Transactional
    @Modifying
    @Query(
            value = "Update Users us Set us.isSuspend = ?2 Where us.id = ?1"
    )
    void changeUserSuspendStatus(int id,Boolean status);
    @Transactional
    @Modifying
    @Query(
            value = "Update Users us Set us.isDelete = ?2 Where us.id = ?1"
    )
    void changeUserDeleteStatus(int id,Boolean status);
    @Query("select us from Users us where us.isDelete = false")
    Page<Users> findAllUsers(Pageable pageable);
    @Query("select us from Users us where us.isDelete = false And " +
            "(us.firstName like concat('%' , :target , '%') or us.email like concat('%',:target,'%'))")
    Page<Users> findAllUsers(Pageable pageable, @Param("target") String target);
    @Transactional
    @Modifying
    @Query(
            value = "Update Users us Set us.isEmailVerify = ?2 Where us.email = ?1"
    )
    void setUsersIsEmailVerifyByEmail(String email, Boolean isEmailVerify);
    @Query(
          "select count(us) from Users us where us.isSuspend = false and us.isDelete = false"
    )
    Integer getTotalActiveUsers();
    @Query(
            "select count(us) from Users us where us.isDelete = true"
    )
    Integer getTotalSoftDeletedUsers();


//

//    @Query(
//            "Select Users From Users"
//    )
//    List<Users> findAllDrivers();
}
