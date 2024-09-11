package com.nucleusteq.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    private int id;
    private String title;
    private String author;
    private int category_id;
    private int quantity;
    private String imageURL;
}
