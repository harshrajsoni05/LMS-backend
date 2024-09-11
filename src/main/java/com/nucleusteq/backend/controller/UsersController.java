package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.dto.UserDTO;
import com.nucleusteq.backend.dto.UserOutDTO;
import com.nucleusteq.backend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public Page<UserOutDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role) {
        return userService.getAllUsers(page, size, search, role);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutDTO> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }



    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserDTO usersDTO){

        String response = userService.createUser(usersDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public Page<UserOutDTO> searchByPhoneNumber(
            @RequestParam String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size) {
        return userService.searchByPhoneNumber(phoneNumber, page, size);
    }


}