package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.dto.IssuanceDTO;
import com.nucleusteq.backend.dto.IssuanceOutDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Issuance;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.mapper.IssuanceMapper;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.UserRepository;
import com.nucleusteq.backend.service.IIssuanceService;
import com.nucleusteq.backend.service.ISMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IssuanceServiceImpl implements IIssuanceService {

    private final IssuanceRepository issuanceRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IssuanceMapper issuanceMapper;
    private final ISMSService ismsService;


    @Autowired
    public IssuanceServiceImpl(ISMSService ismsService,IssuanceRepository issuanceRepository, BookRepository bookRepository, UserRepository userRepository, IssuanceMapper issuanceMapper) {
        this.issuanceRepository = issuanceRepository;
        this.ismsService = ismsService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.issuanceMapper = issuanceMapper;
    }

    public Page<IssuanceOutDTO> getAllIssuances(String search, Pageable pageable) {
        Page<Issuance> issuances;

        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());

        if (search != null && !search.isEmpty()) {
            issuances = issuanceRepository.findByUserNameOrBookTitle(search, sortedByIdDesc);
        } else {
            issuances = issuanceRepository.findAll(sortedByIdDesc);
        }

        return issuances.map(issuanceMapper::toIssuanceOutDTO);
    }



    @Override
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(int id) {
        return issuanceRepository.findById(id)
                .map(issuance -> ResponseEntity.ok(issuanceMapper.toIssuanceOutDTO(issuance)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ResponseDTO> addIssuance(IssuanceDTO issuanceDTO) {
        Books book = bookRepository.findById(issuanceDTO.getBook_id())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(issuanceDTO.getBook_id())));
        Users user = userRepository.findById(issuanceDTO.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(issuanceDTO.getUser_id())));

        if (!book.isAvailable()) {
            throw new ResourceNotFoundException("Book","quantity","zero");
        }

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        Issuance issuance = issuanceMapper.toIssuance(issuanceDTO);
        issuanceRepository.save(issuance);

        LocalDate issueDate = issuance.getIssue_date().toLocalDateTime().toLocalDate();
        LocalDate returnDate = issuance.getReturn_date().toLocalDateTime().toLocalDate();

        String message = String.format("\nYou have issued the book '%s'\n" +
                        "author '%s'\n" +
                        "From %s\n" +
                        "To %s",
                issuance.getBook().getTitle(),
                issuance.getBook().getAuthor(),
                issueDate,
                returnDate);

        if (issuance.getIssuance_type().equals("In House")) {
            System.out.println("message for Issuance" + message);
//            ismsService.sendSms(issuance.getUser().getNumber(), message);
        }

        String responseMessage = "Issuance added successfully for user '" + user.getName() + "' with book '" + book.getTitle() + "'";
        ResponseDTO response = new ResponseDTO("success", responseMessage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDTO> updateIssuance(int id, IssuanceDTO issuanceDTO) {
        return issuanceRepository.findById(id)
                .map(existingIssuance -> {
                    String previousStatus = existingIssuance.getStatus();

                    existingIssuance.setReturn_date(issuanceDTO.getReturn_date());
                    existingIssuance.setStatus(issuanceDTO.getStatus());
                    existingIssuance.setIssuance_type(issuanceDTO.getIssuance_type());

                    issuanceRepository.save(existingIssuance);

                    if ("Returned".equals(existingIssuance.getStatus()) && !"Returned".equals(previousStatus)) {
                        Books book = existingIssuance.getBook();
                        book.setQuantity(book.getQuantity() + 1);
                        bookRepository.save(book);
                    }

                    String message = "Issuance for user '" + existingIssuance.getUser().getName() +
                            "' updated successfully with book '" + existingIssuance.getBook().getTitle() + "'";

                    ResponseDTO response = new ResponseDTO("success", message);
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Issuance", "id", String.valueOf(id)));
    }



    @Override
    public ResponseEntity<ResponseDTO> deleteIssuance(int id) {
        return issuanceRepository.findById(id)
                .map(issuance -> {
                    Books book = bookRepository.findById(issuance.getBook_id())
                            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(issuance.getBook_id())));

                    bookRepository.save(book);

                    issuanceRepository.delete(issuance);
                    String message = "Issuance for user '" + issuance.getUser().getName() +
                            "' deleted successfully with book '" + issuance.getBook().getTitle() + "'";                    ResponseDTO response = new ResponseDTO("success", message);

                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Issuance", "id", String.valueOf(id)));
    }

    @Override
    public Page<IssuanceOutDTO> getIssuancesByUserId(int userId, Pageable pageable) {
        Page<Issuance> issuancesPage = issuanceRepository.findByUserId(userId, pageable);
        return issuancesPage.map(issuanceMapper::toIssuanceOutDTO);
    }

    @Override
    public Page<IssuanceOutDTO> getIssuancesbyBookId(int BookId, Pageable pageable) {
        Page<Issuance> issuancesPage = issuanceRepository.findByBookId(BookId, pageable);
        return issuancesPage.map(issuanceMapper::toIssuanceOutDTO);
    }
}
