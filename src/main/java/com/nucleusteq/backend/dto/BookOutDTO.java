package com.nucleusteq.backend.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BookOutDTO {
    private int id;
    private String title;
    private String author;
    private int category_id;
    private int quantity;
    private String imageURL;
    private CategoryDTO category;
}

