package com.nucleusteq.backend.config;

import com.nucleusteq.backend.jwt.AuthTokenFilter;
import com.nucleusteq.backend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.nucleusteq.backend.jwt.AuthEntryPointJwt;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public  class SecurityConfig  {


    @Autowired
    private UserServiceImpl usersServiceImp;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return  new AuthTokenFilter();
    }
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                //Authentication
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/register").hasRole("ADMIN")
                                .requestMatchers("/api/login/currentuser").permitAll()

                                //Category
                                .requestMatchers("/api/categories").hasRole("ADMIN")
                                .requestMatchers("/api/categories", "/api/categories/**").hasRole("ADMIN")


                                //Book routes
                                .requestMatchers("/api/books/recent").permitAll()
                                .requestMatchers( "/api/books/**").hasRole("ADMIN")


                                //User routes
                                .requestMatchers( "/api/users/**").hasRole("ADMIN")
                                .requestMatchers( "/api/users").hasRole("ADMIN")

                                //Issuance routes
                                .requestMatchers("/api/issuances/user/**").permitAll()
                                .requestMatchers("/api/issuances/**").hasRole("ADMIN")



                                .requestMatchers("/**").permitAll()

                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler) // Custom unauthorized handler
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(usersServiceImp);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }


}