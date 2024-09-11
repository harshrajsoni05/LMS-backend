package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.dto.IssuanceOutDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.service.impl.IssuanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nucleusteq.backend.dto.IssuanceDTO;

import java.util.List;

@RestController
@RequestMapping("/api/issuances")
public class IssuancesController {

    @Autowired
    private IssuanceServiceImpl issuanceService;

    @GetMapping
    public Page<IssuanceOutDTO> getAllIssuances(@RequestParam(required = false)String search, Pageable pageable) {
        return issuanceService.getAllIssuances(search,pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(@PathVariable int id) {
        return issuanceService.getIssuanceById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addIssuance(@RequestBody IssuanceDTO issuanceDTO) {
        return issuanceService.addIssuance(issuanceDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateIssuance(@PathVariable int id, @RequestBody IssuanceDTO issuanceDTO) {
        return issuanceService.updateIssuance(id, issuanceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO>deleteIssuance(@PathVariable int id) {
        return issuanceService.deleteIssuance(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<IssuanceOutDTO>> getIssuancesByUserId(@PathVariable int userId, Pageable pageable) {
        Page<IssuanceOutDTO> issuances = issuanceService.getIssuancesByUserId(userId, pageable);
        return ResponseEntity.ok(issuances);
    }

    @GetMapping("/book/{BookId}")
    public ResponseEntity<Page<IssuanceOutDTO>> getIssuancesbyBookId(@PathVariable int BookId, Pageable pageable) {
        Page<IssuanceOutDTO> issuances = issuanceService.getIssuancesbyBookId(BookId, pageable);
        return ResponseEntity.ok(issuances);
    }



}
