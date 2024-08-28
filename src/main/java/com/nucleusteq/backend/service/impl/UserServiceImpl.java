package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.UserDTO;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public String createUser(UserDTO usersDTO) {

        Users user = convertToEntity(usersDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);

        return "User added successfully with ID: " + savedUser.getId();

    }
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(this::convertToDTO);

    }

    public ResponseEntity<UserDTO> getUserById(int id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {
        Users user = convertToEntity(userDTO);
        System.out.println(userDTO.getPassword());
        Users savedUser = userRepository.save(user);
        return ResponseEntity.ok(convertToDTO(savedUser));
    }

    public ResponseEntity<UserDTO> updateUser(int id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setNumber(userDTO.getNumber());
                    existingUser.setRole(userDTO.getRole());
                    existingUser.setEmail(userDTO.getEmail());

                    existingUser.setPassword(userDTO.getPassword());
                    userRepository.save(existingUser);
                    return ResponseEntity.ok(convertToDTO(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteUser(int id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private UserDTO convertToDTO(Users user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setNumber(user.getNumber());
        userDTO.setRole(user.getRole());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNumber) throws UsernameNotFoundException {

        Optional<Users> userOptional;

        if(usernameOrPhoneNumber.contains("@")) {
            userOptional = userRepository.findByEmail(usernameOrPhoneNumber);
        }
        else {
            userOptional = userRepository.findByNumber(usernameOrPhoneNumber);
        }
        Users userInfo = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + usernameOrPhoneNumber));

//        List<GrantedAuthority> authorities = userInfo.getRole().stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//                .collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+userInfo.getRole()));

        return new User(userInfo.getEmail(), userInfo.getPassword(), grantedAuthorities);
    }
    public Users getByUserName(String name) {
        Users user;
        if (name.contains("@")) {
//            email
            user = userRepository.findByEmail(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name)
            );
        } else {
//            mobile
            user = userRepository.findByNumber(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name)
            );
        }

        return user;
    }
    private Users convertToEntity(UserDTO userDTO) {
        Users user = new Users();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setNumber(userDTO.getNumber());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
