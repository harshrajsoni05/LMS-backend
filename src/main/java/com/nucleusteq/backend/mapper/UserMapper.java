package com.nucleusteq.backend.mapper;

import com.nucleusteq.backend.dto.UserDTO;
import com.nucleusteq.backend.dto.UserOutDTO;
import com.nucleusteq.backend.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserOutDTO toUserOutDTO(Users user) {
        if (user == null) {
            return null;
        }

        UserOutDTO userOutDTO = new UserOutDTO();
        userOutDTO.setId(user.getId());
        userOutDTO.setName(user.getName());
        userOutDTO.setNumber(user.getNumber());
        userOutDTO.setRole(user.getRole());
        userOutDTO.setEmail(user.getEmail());

        return userOutDTO;
    }

    public Users toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        Users user = new Users();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setNumber(userDTO.getNumber());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword()); // Ensure password is handled properly

        return user;
    }

    public UserDTO toUserDTO(Users user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setNumber(user.getNumber());
        userDTO.setRole(user.getRole());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword()); // Handle this carefully in practice

        return userDTO;
    }
}
