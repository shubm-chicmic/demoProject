package com.example.demoProject.Configuration;



import com.example.demoProject.Dto.EmailDto;
import com.example.demoProject.Filters.Authentication;
import com.example.demoProject.Filters.AuthorizationFilter;
import com.example.demoProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    @Autowired
    EmailDto emailDto;

//    private final UserDetailsService userDetailsService;
//
//    public SecurityConfig(UserService userService, UserDetailsService userDetailsService) {
//        this.userService = userService;
//        this.userDetailsService = userDetailsService;
//    }

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        //Authentication Entry point!!!
//
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        return authProvider;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //   System.out.println(ANSI_CYAN + "security " + "1" );

       Authentication filter = new Authentication(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),userService);
        filter.setFilterProcessesUrl("/login");

        http.csrf().disable();

//        http.authorizeHttpRequests().requestMatchers("/abcd").permitAll();
//        http.authorizeHttpRequests().requestMatchers("/public/rider/register").permitAll();

//
        http.authorizeHttpRequests().anyRequest().permitAll();
        http.addFilter(filter);
        http.addFilterBefore(new AuthorizationFilter(userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//       // return new BCryptPasswordEncoder(10);
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}