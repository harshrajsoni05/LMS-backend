package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.dto.BookOutDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBookService {

    public List<Books> getBooks();

    Page<BookOutDTO> getAllBooks(int page, int size, String search);

    ResponseEntity<BookOutDTO> getBookById(int id);

    ResponseEntity<ResponseDTO> addBook(BookDTO bookDTO);

    ResponseEntity<ResponseDTO> updateBook(int id, BookDTO bookDTO);

    ResponseEntity<ResponseDTO> deleteBook(int id);

    List<BookOutDTO> findBookSuggestions(String query);



}
