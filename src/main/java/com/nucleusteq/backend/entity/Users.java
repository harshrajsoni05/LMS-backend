package com.nucleusteq.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String number;
    private String role;
    private String email;
    private String password;

    // Manual setter methods with chaining
    public Users setName(String name) {
        this.name = name;
        return this;
    }

    public Users setNumber(String number) {
        this.number = number;
        return this;
    }

    public Users setRole(String role) {
        this.role = role;
        return this;
    }

    public Users setEmail(String email) {
        this.email = email;
        return this;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }
}
