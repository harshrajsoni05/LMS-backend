package com.nucleusteq.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Data@NoArgsConstructor
@Table(name="Issuances")
public class Issuance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int user_id;
    private int book_id;
    private Timestamp issue_date;
    private Timestamp return_date;
    private String status;
    private String issuance_type;
}
