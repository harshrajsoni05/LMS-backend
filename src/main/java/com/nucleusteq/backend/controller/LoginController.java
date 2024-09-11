package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.dto.LoginDTO;
import com.nucleusteq.backend.dto.UserDTO;
import com.nucleusteq.backend.dto.UserOutDTO;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.jwt.JwtUtils;
import com.nucleusteq.backend.jwt.LoginResponse;
import com.nucleusteq.backend.mapper.UserMapper;
import com.nucleusteq.backend.service.IUserService;
import com.nucleusteq.backend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        try {
            String decodedPassword = decodeBase64(loginDTO.getPassword());

            Authentication authentication = authenticate(loginDTO.getUsernameOrPhoneNumber(), decodedPassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Users user = userService.getByUserName(userDetails.getUsername());

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
            LoginResponse response = new LoginResponse(jwtToken, userDetails.getUsername(), user.getRole(), user.getId() ,user.getName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid credentials");
            errorResponse.put("status", false);
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    private Authentication authenticate(String usernameOrPhoneNumber, String password) throws Exception {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrPhoneNumber, password)
        );
    }

    private String decodeBase64(String encodedPassword) {
        return new String(Base64.getDecoder().decode(encodedPassword), StandardCharsets.UTF_8);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<LoginResponse> currentUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);
        Users user = userService.getByUserName(userName);

        LoginResponse loginResponse = new LoginResponse(
                jwtToken,
                user.getEmail(),
                user.getRole(),
                user.getId(),
                user.getName()
        );

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
}
