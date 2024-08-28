package com.nucleusteq.backend.jwt;

import lombok.Data;

@Data
public class LoginRequest {

    private String username; // Can be either email or phone number

    private String password;


}
