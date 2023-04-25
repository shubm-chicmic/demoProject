package com.example.demoProject.Service;

import com.example.demoProject.Models.UserOtp;
import com.example.demoProject.Repository.UserOtpRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OtpService {

    @Autowired
    UserOtpRepo userOtpRepo;
    public OtpService(){

    }

    public int generateOTP(String email){
        Random random = new Random();
        int otp = 100000 + random.nextInt(999999);
        log.info("\u001B[33m" + "control" + "\u001B[0m");
        UserOtp userOtp = userOtpRepo.findByEmail(email);
        if(userOtp != null) {
            userOtp = UserOtp.builder().id(userOtp.getId()).email(email).otp(Integer.toString(otp)).build();
        }else {
            userOtp = UserOtp.builder().email(email).otp(Integer.toString(otp)).build();
        }
        userOtpRepo.save(userOtp);
        return otp;
    }

    public int getOtp(String email){
        try{
            return Integer.parseInt(userOtpRepo.findByEmail(email).getOtp());
        }catch (Exception e){
            return 0;
        }
    }

    public void clearOTP(String email){
        int id = userOtpRepo.findByEmail(email).getId();
        userOtpRepo.deleteById(id);
    }


}
