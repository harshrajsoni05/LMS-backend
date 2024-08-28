package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.IssuanceDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Issuance;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IssuanceServiceImpl {

    @Autowired
    private IssuanceRepository issuanceRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<IssuanceDTO> getAllIssuances(Pageable pageable) {
        return issuanceRepository
                .findAll(pageable)
                .map(this::convertToDTO);

    }

    public ResponseEntity<IssuanceDTO> getIssuanceById(int id) {
        return issuanceRepository.findById(id)
                .map(issuance -> ResponseEntity.ok(convertToDTO(issuance)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> addIssuance(IssuanceDTO issuanceDTO) {
        Optional<Books> bookOpt = bookRepository.findById(issuanceDTO.getBook_id());
        Optional<Users> userOpt = userRepository.findById(issuanceDTO.getUser_id());

        if (bookOpt.isPresent() && userOpt.isPresent()) {
            Books book = bookOpt.get();
            if (book.isAvailable()) {
                book.setQuantity(book.getQuantity() - 1); // Decrement book quantity
                bookRepository.save(book);

                Issuance issuance = convertToEntity(issuanceDTO);
                issuanceRepository.save(issuance);

                return ResponseEntity.ok("Issuance added successfully with ID " + issuance.getId());
            } else {
                return ResponseEntity.badRequest().body("Book is not available.");
            }
        } else {
            return ResponseEntity.badRequest().body("Book or User not found.");
        }
    }



public ResponseEntity<IssuanceDTO> updateIssuance(int id, IssuanceDTO issuanceDTO) {
    return issuanceRepository.findById(id)
            .map(existingIssuance -> {
                existingIssuance.setReturn_date(issuanceDTO.getReturn_date());
                existingIssuance.setStatus(issuanceDTO.getStatus());
                existingIssuance.setIssuance_type(issuanceDTO.getIssuance_type());

                issuanceRepository.save(existingIssuance);

                return ResponseEntity.ok(convertToDTO(existingIssuance));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteIssuance(int id) {
        return issuanceRepository.findById(id)
                .map(issuance -> {
                    Books book = bookRepository.findById(issuance.getBook_id())
                            .orElseThrow(() -> new ResourceNotFoundException("Book Not Found with ID " + issuance.getBook_id()));

                    // Increment book quantity to reflect the return
                    book.setQuantity(book.getQuantity() + 1);
                    bookRepository.save(book);

                    issuanceRepository.delete(issuance);
                    return ResponseEntity.ok("Issuance deleted successfully with ID: " + id);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    //MAPPING FOR THE APPLICATION

    private IssuanceDTO convertToDTO(Issuance issuance) {
        IssuanceDTO issuanceDTO = new IssuanceDTO();
        issuanceDTO.setId(issuance.getId());
        issuanceDTO.setUser_id(issuance.getUser_id());
        issuanceDTO.setBook_id(issuance.getBook_id());
        issuanceDTO.setIssue_date(issuance.getIssue_date());
        issuanceDTO.setReturn_date(issuance.getReturn_date());
        issuanceDTO.setStatus(issuance.getStatus());
        issuanceDTO.setIssuance_type(issuance.getIssuance_type());
        return issuanceDTO;
    }

    private Issuance convertToEntity(IssuanceDTO issuanceDTO) {
        Issuance issuance = new Issuance();
        issuance.setUser_id(issuanceDTO.getUser_id());
        issuance.setBook_id(issuanceDTO.getBook_id());
        issuance.setIssue_date(issuanceDTO.getIssue_date());
        issuance.setReturn_date(issuanceDTO.getReturn_date());
        issuance.setStatus(issuanceDTO.getStatus());
        issuance.setIssuance_type(issuanceDTO.getIssuance_type());
        return issuance;
    }
}
