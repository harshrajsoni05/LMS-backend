package com.nucleusteq.backend.dto;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class UserOutDTO {
    private int id;
    private String name;
    private String number;
    private String role;
    private String email;

}