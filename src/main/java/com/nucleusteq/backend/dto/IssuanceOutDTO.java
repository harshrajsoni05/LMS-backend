package com.nucleusteq.backend.dto;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Table(name="Issuances")
public class IssuanceOutDTO {
    private int id;
    private int user_id;
    private int book_id;
    private Timestamp issue_date;
    private Timestamp return_date;
    private String status;
    private String issuance_type;

    private BookDTO books;
    private UserDTO users;

}
