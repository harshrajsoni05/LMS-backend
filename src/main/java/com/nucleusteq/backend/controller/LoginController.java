package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.dto.LoginDTO;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.jwt.JwtUtils;
import com.nucleusteq.backend.jwt.LoginResponse;
import com.nucleusteq.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/auth")
public class LoginController {



    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {

        Authentication authentication;

        try {

            System.out.println("In Comtroller");

            if(isEmail(loginDTO.getUsernameOrPhoneNumber()))
            {
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrPhoneNumber(),loginDTO.getPassword()));
            } else if (isPhoneNumber(loginDTO.getUsernameOrPhoneNumber())) {

                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsernameOrPhoneNumber(),
                                loginDTO.getPassword()
                        )
                );
            }else {

                throw new Exception("Invalid login input format.");

            }


        }catch (Exception e ) {

            Map<String,Object> map =  new HashMap<>();
            map.put("Messsage", "Bad credentials");
            map.put("status",false);
            return  new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);



        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        Users user = userService.getByUserName(username);

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        System.out.println(jwtToken);
        LoginResponse response = new LoginResponse(jwtToken,userDetails.getUsername(), "ROLE_"+user.getRole(), user.getId());

        return ResponseEntity.ok(response);

    }




    private boolean isEmail(String input) {
        // Basic email validation logic
        return input != null && input.contains("@");
    }

    private boolean isPhoneNumber(String input) {
        // Basic phone number validation logic
        return input != null && input.matches("\\d+"); // Simple check for numeric values
    }

}