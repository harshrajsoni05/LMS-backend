package com.nucleusteq.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountsDTO {
    private int booksCount;
    private int categoriesCount;
    private int issuancesCount;
    private int usersCount;


    public CountsDTO(int booksCount, int categoriesCount, int issuancesCount, int usersCount) {
        this.booksCount = booksCount;
        this.categoriesCount = categoriesCount;
        this.issuancesCount = issuancesCount;
        this.usersCount = usersCount;
    }
}