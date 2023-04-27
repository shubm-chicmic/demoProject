package com.example.demoProject.Dto;


import com.example.demoProject.Models.UsersActivity;
import com.example.demoProject.Repository.UserLoggingRepo;
import com.example.demoProject.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Aspect
@Component

public class UserLogging {
    @Autowired
    UserService userService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserLoggingRepo userLoggingRepo;


    @Pointcut("execution(* com.example.AdminPanel.Controller.*.*(..))")
    public void forLogging(){};
    @Before("forLogging()")
    public void beforeCall(JoinPoint joinPoint) {
        MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
        String url = "";
        Message message = (methodSignature.getMethod().getAnnotation(Message.class));
        String value = "";
        if(message != null) {
            value = message.value();
        }
          Object[] obj = joinPoint.getArgs();
          log.info(value);
//        String value2 = String.valueOf(methodSignature.getMethod().getAnnotation(Data.class));
          for (Object tempArg : obj) {
              if(tempArg instanceof HttpServletRequest){
                  HttpServletRequest request = (HttpServletRequest)  tempArg;
                  url = request.getServletPath();
                  String id  = request.getParameter("id");
                  if(id != null) {
                      String getUserUrl =  "/getUserById/" + id;
                      log.info(getUserUrl);
                      UserDto userDto = restTemplate.getForObject(getUserUrl, UserDto.class);


                      value += userDto.getEmail();
                  }
              }
          }
          UsersActivity usersActivity = UsersActivity.builder().message(value).url(url).build();
          userLoggingRepo.save(usersActivity);

//        log.info(value + " " + value2);

    }
}

