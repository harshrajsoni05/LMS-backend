package com.nucleusteq.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import lombok.NoArgsConstructor;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@Entity
@Table(name="Issuances")
public class Issuance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "book_id")
    private int book_id;

    private Timestamp issue_date;

    private Timestamp return_date;
    private String status;
    private String issuance_type;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Books book;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users user;
}
