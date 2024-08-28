package com.nucleusteq.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class IssuanceDTO {
    private int id;
    private int user_id;
    private int book_id;
    private Timestamp issue_date;
    private Timestamp return_date;
    private String status;
    private String issuance_type;

}
