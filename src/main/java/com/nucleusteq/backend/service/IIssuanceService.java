package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.dto.IssuanceDTO;
import com.nucleusteq.backend.dto.IssuanceOutDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IIssuanceService {

    Page<IssuanceOutDTO> getAllIssuances(String search ,Pageable pageable);

    ResponseEntity<IssuanceOutDTO> getIssuanceById(int id);

    ResponseEntity<ResponseDTO> addIssuance(IssuanceDTO issuanceDTO);

    ResponseEntity<ResponseDTO> updateIssuance(int id, IssuanceDTO issuanceDTO);

    ResponseEntity<ResponseDTO> deleteIssuance(int id);

    Page<IssuanceOutDTO> getIssuancesByUserId(int userId, Pageable pageable);

    Page<IssuanceOutDTO> getIssuancesbyBookId(int BookId, Pageable pageable) ;
}
