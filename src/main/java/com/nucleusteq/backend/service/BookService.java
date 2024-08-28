package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Method to get paginated list of books
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::convertToDTO);  // Converts each Books entity to a BookDTO
    }

    public ResponseEntity<BookDTO> getBookById(int id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok(convertToDTO(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<BookDTO> addBook(BookDTO bookDTO) {
        Books book = convertToEntity(bookDTO);
        Books savedBook = bookRepository.save(book);
        return ResponseEntity.ok(convertToDTO(savedBook));
    }

    public ResponseEntity<BookDTO> updateBook(int id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookDTO.getTitle());
                    existingBook.setAuthor(bookDTO.getAuthor());
                    existingBook.setCategory_id(bookDTO.getCategory_id());
                    existingBook.setQuantity(bookDTO.getQuantity());
                    existingBook.setImageURL(bookDTO.getImageURL());

                    bookRepository.save(existingBook);
                    return ResponseEntity.ok(convertToDTO(existingBook));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteBook(int id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.<Void>ok().build();  // Explicitly specify the type here
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Convert Books entity to BookDTO
    private BookDTO convertToDTO(Books book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setCategory_id(book.getCategory_id());
        bookDTO.setQuantity(book.getQuantity());
        bookDTO.setImageURL(book.getImageURL());
        return bookDTO;
    }

    // Convert BookDTO to Books entity
    private Books convertToEntity(BookDTO bookDTO) {
        Books book = new Books();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory_id(bookDTO.getCategory_id());
        book.setQuantity(bookDTO.getQuantity());
        book.setImageURL(bookDTO.getImageURL());

        return book;
    }
}
