package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.dto.UserDTO;
import com.nucleusteq.backend.dto.UserOutDTO;
import com.nucleusteq.backend.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    String createUser(UserDTO usersDTO);

    Page<UserOutDTO> getAllUsers(int page, int size, String search , String role);

    ResponseEntity<UserOutDTO> getUserById(int id);

    ResponseEntity<ResponseDTO> addUser(UserDTO userDTO);

    ResponseEntity<ResponseDTO> updateUser(int id, UserDTO userDTO);

    ResponseEntity<ResponseDTO> deleteUser(int id);

    Users getByUserName(String userName);

    Page<UserOutDTO> searchByPhoneNumber(String phoneNumber, int page, int size);

}

