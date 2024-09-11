package com.nucleusteq.backend.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String jwtToken;
    private String email;
    private String role;
    private  int id;
    private String name;


}