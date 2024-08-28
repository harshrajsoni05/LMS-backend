package com.nucleusteq.backend.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public Page<BookDTO> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size )
    {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.getAllBooks(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        return bookService.addBook(bookDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable int id) {
        return bookService.deleteBook(id);
    }
}
