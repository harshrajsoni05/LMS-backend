package com.nucleusteq.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private String number;
    private String role;
    private String email;
    private String password;
}
