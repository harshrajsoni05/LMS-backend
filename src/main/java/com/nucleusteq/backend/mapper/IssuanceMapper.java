package com.nucleusteq.backend.mapper;

import com.nucleusteq.backend.dto.IssuanceDTO;
import com.nucleusteq.backend.dto.IssuanceOutDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Issuance;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IssuanceMapper {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public IssuanceMapper(BookRepository bookRepository, UserRepository userRepository, UserMapper userMapper) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public IssuanceOutDTO toIssuanceOutDTO(Issuance issuance) {
        if (issuance == null) {
            return null;
        }

        IssuanceOutDTO issuanceDTO = new IssuanceOutDTO();
        issuanceDTO.setId(issuance.getId());
        issuanceDTO.setUser_id(issuance.getUser_id());
        issuanceDTO.setBook_id(issuance.getBook_id());
        issuanceDTO.setIssue_date(issuance.getIssue_date());
        issuanceDTO.setReturn_date(issuance.getReturn_date());
        issuanceDTO.setStatus(issuance.getStatus());
        issuanceDTO.setIssuance_type(issuance.getIssuance_type());

        if (issuance.getBook() != null) {
            issuanceDTO.setBooks(BookMapper.mapToBookDTO(issuance.getBook()));
        }

        if (issuance.getUser() != null) {
            issuanceDTO.setUsers(userMapper.toUserDTO(issuance.getUser()));
        }

        return issuanceDTO;
    }

    public Issuance toIssuance(IssuanceDTO issuanceDTO) {
        if (issuanceDTO == null) {
            return null;
        }

        Issuance issuance = new Issuance();
        issuance.setId(issuanceDTO.getId());
        issuance.setUser_id(issuanceDTO.getUser_id());
        issuance.setBook_id(issuanceDTO.getBook_id());
        issuance.setIssue_date(issuanceDTO.getIssue_date());
        issuance.setReturn_date(issuanceDTO.getReturn_date());
        issuance.setStatus(issuanceDTO.getStatus());
        issuance.setIssuance_type(issuanceDTO.getIssuance_type());

        Books book = bookRepository.findById(issuanceDTO.getBook_id())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(issuanceDTO.getBook_id())));
        Users user = userRepository.findById(issuanceDTO.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(issuanceDTO.getUser_id())));

        issuance.setBook(book);
        issuance.setUser(user);

        return issuance;
    }
}
