package com.nucleusteq.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private int id;
    private String name;
    private String description;
}
