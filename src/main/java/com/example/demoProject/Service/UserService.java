package com.example.demoProject.Service;

import com.example.demoProject.Dto.UserDto;
import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UserUuid;
import com.example.demoProject.Repository.RoleRepository;
import com.example.demoProject.Repository.UserRepository;
import com.example.demoProject.Repository.UserRoleRepository;
import com.example.demoProject.Repository.UserUuidRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UserRepository userrepo;
    @Autowired
    RolesService rolesService;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserUuidRepository uuidRepo;
    @Value("${image.path}")
    String imagePath;

    // REGISTER WORK
    public void addAdmin(Users user) {
        String role = "ADMIN";
        userrepo.save(user);
        int userId = userrepo.findByEmail(user.getEmail()).getId();
        int roleId = userRoleRepository.findIdByRoles(role);
        Roles roles = Roles.builder()
                .roleId(roleId)
                .userId(userId)
                .build();
        roleRepository.save(roles);
    }
    public void addDriver(Users user) {
        int userId= userrepo.save(user).getId();
        String role = "DRIVER";
//        int userId = userrepo.findByEmail(user.getEmail()).getId();
        int roleId = userRoleRepository.findIdByRoles(role);
        Roles roles = Roles.builder()
                .roleId(roleId)
                .userId(userId)
                .build();
        roleRepository.save(roles);

    }

    public void addRider(Users user) {
        userrepo.save(user);
        String role = "RIDER";
        int userId = userrepo.findByEmail(user.getEmail()).getId();
        int roleId = userRoleRepository.findIdByRoles(role);
        Roles roles = Roles.builder()
                .roleId(roleId)
                .userId(userId)
                .build();
        roleRepository.save(roles);
    }
    public void addUser(Users users) {
        userrepo.save(users);
    }
    /*
    public void addUser(UserDto dto) {
        System.out.println("inside adduser");
        users2 user=users2.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        userrepo.save(user);
        System.out.println("inside adduser line 59");
        rolesService.setRoles(userrepo.findByEmail(dto.getEmail()), dto.getRoles());

    }

     */
    //UPDATE WORK
    public Users updateUserByEmail(String email, UserDto userDto, String fileName) {
        userDto.setImageUrl(imagePath + fileName);

        userrepo.updateUserByEmail(email, userDto);

        Users users = userrepo.findByEmail(email);

        return users;
    }
    public void updateDriver(Users users) {
        System.out.println("inside updateDriver " + users);
        int userId = userrepo.findByEmail(users.getEmail()).getId();
        users.setId(userId);
        userrepo.save(users);
        //userrepo.updateUserByEmail(users.getEmail(), users.getFirstName(), users.getLastName(), users.getPhoneNo());//, users.getLastName(), users.getPhoneNo());
        System.out.println("inside updateDrive second ///// " + userrepo.findByEmail(users.getEmail()).getFirstName());

    }
    public Users getUserByEmail(String Email) {
        System.out.println("userservice " + " 5 " + Email);
        return userrepo.findByEmail(Email);


    }
    public Users getUserById(int id) {
        System.out.println("userservice " + " 6 " + id);
        return userrepo.findUsersById(id);


    }

    public Users getUsersByEmailorPhoneNo(String email, String phoneNo) {
        System.out.println("email = " + email);
        return userrepo.findUsersByEmailorPhoneNo(email, phoneNo);
    }

    public List<Users> getAllDrivers() {
        String role = "DRIVER";
        int roleId = userRoleRepository.findIdByRoles(role);
        System.out.println("RoleId = " + roleId);
        List<Integer> userIdList = roleRepository.findUserIdByRoleId(roleId);
        List<Users> usersList = new ArrayList<>();
        for(Integer id : userIdList){
            usersList.add(userrepo.findUsersById(id));
        }
        return usersList ;
    }

    public List<Users> getAllUsers(int pageNumber, int pageSize, String target, String sortBy, int order) {
        Pageable pageable;
        if(order == 1){
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, sortBy);

        }
        Page<Users> pageUsers;
        if(target.equals("null")){
            System.out.println("\u001B[44m" + " inside no search " + target+ "\u001B[0m");
             pageUsers = userrepo.findAllUsers(pageable);
        }else {
            System.out.println("\u001B[44m" + " inside search " + target +"\u001B[0m");
             pageUsers = userrepo.findAllUsers(pageable, target);

        }
        List<Users> usersList = pageUsers.getContent();

        return usersList ;
    }

    //Suspend User
    public void suspendUserById(int id) {
        Boolean status = userrepo.findUsersById(id).getIsSuspend();
        status = !status;
        userrepo.changeUserSuspendStatus(id,status);

    }
    public void softDeleteUserById(int id) {
        Boolean status = userrepo.findUsersById(id).getIsDelete();
        status = !status;
        userrepo.changeUserDeleteStatus(id,status);
    }

    // Token work

    public UserUuid CreateToken(UserUuid uuidEntity){
        System.out.println(uuidEntity);
        if (uuidRepo==null) System.out.println("null");
        // return uuidEntity;

        return uuidRepo.save(uuidEntity);
    }
    public UserUuid getUserUuidByEmail(String email){
       return uuidRepo.getUserUuidByEmail(email);
    }

    // Email Verify
    public void setUsersEmailVerifyTrue(String email) {
        userrepo.setUsersIsEmailVerifyByEmail(email, true);
    }



    public UserUuid getToken(String uuid) {
        return uuidRepo.getUserTokenByUuid(uuid);
    }
    @Transactional
    public int deleteToken(String uuid) {
        return  uuidRepo.deleteByUuid(uuid);
    }

    public List<Integer> findRoleIdByUserId(int userId) {
        return roleRepository.findRoleIdByUserId(userId);
    }

    // Count Values
    public Integer getTotalActiveUsers() {
        return userrepo.getTotalActiveUsers();
    }
    public Integer getTotalSoftDeletedUsers() {
        return userrepo.getTotalSoftDeletedUsers();
    }

    public String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


}
