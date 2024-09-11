package com.nucleusteq.backend.controller;
import com.nucleusteq.backend.dto.BookOutDTO;
import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.service.impl.BookServiceImpl;
import org.springframework.data.domain.Page;
import com.nucleusteq.backend.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController  {

    @Autowired
    private BookServiceImpl bookService;

    @GetMapping("/all")
    public List<Books> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping
    public Page<BookOutDTO> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "7") int size,
                                            @RequestParam(required = false) String search) {
        return bookService.getAllBooks(page, size, search);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<BookOutDTO>> getRecentBooks() {
        List<BookOutDTO> recentBooks = bookService.getRecentBooks(5);
        return ResponseEntity.ok(recentBooks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookOutDTO> getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addBook(@RequestBody BookDTO bookDTO) {
        return bookService.addBook(bookDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteBook(@PathVariable int id) {
        return bookService.deleteBook(id);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<BookOutDTO>> getBookSuggestions(@RequestParam String query) {
        List<BookOutDTO> suggestions = bookService.findBookSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
}
