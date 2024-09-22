package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.dto.UserDTO;
import com.nucleusteq.backend.dto.UserOutDTO;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.exception.ResourceAlreadyExistsException;
import com.nucleusteq.backend.mapper.UserMapper;
import com.nucleusteq.backend.repository.UserRepository;
import com.nucleusteq.backend.service.ISMSService;
import com.nucleusteq.backend.service.IUserService;
import com.nucleusteq.backend.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ISMSService ismsService;
    public UserServiceImpl(ISMSService ismsService) {
        this.ismsService = ismsService;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent() ||
                userRepository.findByNumber(userDTO.getNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with this email or phone number already exists.");
        }

        Users user = userMapper.toUser(userDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);

        return "User added successfully with ID: " + savedUser.getId();
    }

    @Override
    public Page<UserOutDTO> getAllUsers(int page, int size, String search, String role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Users> users;

        if (search != null && !search.isEmpty() && role != null && !role.isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCaseAndRole(search, role, pageable);
        } else if (search != null && !search.isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (role != null && !role.isEmpty()) {
            users = userRepository.findByRole(role, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(userMapper::toUserOutDTO);
    }

    @Override
    public ResponseEntity<UserOutDTO> getUserById(int id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", String.valueOf(id)));
        return ResponseEntity.ok(userMapper.toUserOutDTO(user));
    }

    @Override
    public ResponseEntity<ResponseDTO> addUser(UserDTO userDTO) {
        Optional<Users> userWithSameNumber = userRepository.findByNumber(userDTO.getNumber());
        Optional<Users> userWithSameEmail = userRepository.findByEmail(userDTO.getEmail());

        if (userWithSameNumber.isPresent() || userWithSameEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("User with this email or phone number already exists.");
        }

        Users user = userMapper.toUser(userDTO);
        String randomPassword = PasswordGenerator.generatePassword(10);
        user.setPassword(encoder.encode(randomPassword));
        Users savedUser = userRepository.save(user);

        String message = String.format("\nWelcome %s\n" +
                        "Registered Successfully! Welcome to ShelfHive\n" +
                        "These are your login credentials\n" +
                        "Username: %s (OR) %s\n" +
                        "Password: %s",
                savedUser.getName(),
                savedUser.getNumber(),
                savedUser.getEmail(),
                randomPassword);

        ismsService.sendSms(savedUser.getNumber(), message);

        String ResponseMessage = "User '" + savedUser.getName() + "' registered successfully";
        ResponseDTO response = new ResponseDTO("success", ResponseMessage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDTO> updateUser(int id, UserDTO userDTO) {

        Optional<Users> userWithSameNumber = userRepository.findByNumber(userDTO.getNumber());
        if (userWithSameNumber.isPresent() && userWithSameNumber.get().getId() != id) {
            throw new ResourceAlreadyExistsException("User with this phone number already exists.");
        }

        Optional<Users> userWithSameEmail = userRepository.findByEmail(userDTO.getEmail());
        if (userWithSameEmail.isPresent() && userWithSameEmail.get().getId() != id) {
            throw new ResourceAlreadyExistsException("User with this email already exists.");
        }

        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", String.valueOf(id)));

        existingUser.setName(userDTO.getName());
        existingUser.setNumber(userDTO.getNumber());
        existingUser.setRole(userDTO.getRole());
        existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(userDTO.getPassword()));
        }

        userRepository.save(existingUser);

        String message = "User '" + existingUser.getName() + "' updated successfully";
        ResponseDTO response = new ResponseDTO("success", message);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<ResponseDTO> deleteUser(int id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", String.valueOf(id)));
        userRepository.delete(user);

        String message = "User '" + user.getName() + "' deleted successfully";
        ResponseDTO response = new ResponseDTO("success", message);

        return ResponseEntity.ok(response);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNumber) throws UsernameNotFoundException {
        Optional<Users> userOptional;

        if (usernameOrPhoneNumber.contains("@")) {
            userOptional = userRepository.findByEmail(usernameOrPhoneNumber);
        } else {
            userOptional = userRepository.findByNumber(usernameOrPhoneNumber);
        }

        Users user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + usernameOrPhoneNumber));

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public Users getByUserName(String username) {
        Optional<Users> userOptional = username.contains("@")
                ? userRepository.findByEmail(username)
                : userRepository.findByNumber(username);

        return userOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + username));
    }

    @Override
    public Page<UserOutDTO> searchByPhoneNumber(String phoneNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Users> usersPage = userRepository.findByNumberContaining(phoneNumber, pageable);

        return usersPage.map(user -> userMapper.toUserOutDTO(user));
    }
}
