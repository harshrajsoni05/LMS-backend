package com.nucleusteq.backend.exception;

import com.nucleusteq.backend.dto.ResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGlobalException(Exception exception,
                                                             WebRequest webRequest) {
        ResponseDTO errorResponseDto = new ResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                       WebRequest webRequest) {
        ResponseDTO errorResponseDto = new ResponseDTO(
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception,
                                                                            WebRequest webRequest) {
        ResponseDTO errorResponseDto = new ResponseDTO(
                HttpStatus.CONFLICT.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                             WebRequest webRequest) {
        String detailedMessage = exception.getMostSpecificCause().getMessage();

        if (detailedMessage.contains("books_category_id_fkey")) {
            ResponseDTO errorResponseDto = new ResponseDTO(
                    HttpStatus.CONFLICT.toString(),
                    "Cannot delete category. It is referenced by one or more books."
            );
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        }

        if (detailedMessage.contains("issuances_user_id_fkey")) {
            ResponseDTO errorResponseDto = new ResponseDTO(
                    HttpStatus.CONFLICT.toString(),
                    "Cannot delete user. The user has Issuance."
            );
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        }

        if (detailedMessage.contains("issuances_book_id_fkey")) {
            ResponseDTO errorResponseDto = new ResponseDTO(
                    HttpStatus.CONFLICT.toString(),
                    "Cannot delete book. The book has Issuance."
            );
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        }

        ResponseDTO errorResponseDto = new ResponseDTO(
                HttpStatus.CONFLICT.toString(),
                "Data integrity violation: " + exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

}
