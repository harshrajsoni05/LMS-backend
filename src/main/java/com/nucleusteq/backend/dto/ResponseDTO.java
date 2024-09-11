package com.nucleusteq.backend.dto;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @RequiredArgsConstructor
public class ResponseDTO {

    private String status;

    private String message;

}
