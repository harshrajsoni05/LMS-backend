package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.service.IssuanceService;
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
    private IssuanceService issuanceService;

    @GetMapping
    public Page<IssuanceDTO> getAllIssuances(Pageable pageable) {
        return issuanceService.getAllIssuances(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssuanceDTO> getIssuanceById(@PathVariable int id) {
        return issuanceService.getIssuanceById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addIssuance(@RequestBody IssuanceDTO issuanceDTO) {
        return issuanceService.addIssuance(issuanceDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssuanceDTO> updateIssuance(@PathVariable int id, @RequestBody IssuanceDTO issuanceDTO) {
        return issuanceService.updateIssuance(id, issuanceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteIssuance(@PathVariable int id) {
        return issuanceService.deleteIssuance(id);
    }

}
