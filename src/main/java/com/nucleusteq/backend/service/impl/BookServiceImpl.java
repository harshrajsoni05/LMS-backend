package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.dto.BookOutDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Category;
import com.nucleusteq.backend.entity.Users;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.exception.ResourceAlreadyExistsException;
import com.nucleusteq.backend.mapper.BookMapper;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.CategoryRepository;
import com.nucleusteq.backend.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Books> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Page<BookOutDTO> getAllBooks(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Books> books;

        if (search != null && !search.isEmpty()) {
            books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }

        return books.map(BookMapper::mapToBookOutDTO);
    }

    public List<BookOutDTO> getRecentBooks(int limit) {
        return bookRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id")))
                .getContent()
                .stream()
                .map(BookMapper::mapToBookOutDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<BookOutDTO> getBookById(int id) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));

        return ResponseEntity.ok(BookMapper.mapToBookOutDTO(book));
    }

    @Override
    public ResponseEntity<ResponseDTO> addBook(BookDTO bookDTO) {

        List<Books> booksWithSameName = bookRepository.findByTitleContainingIgnoreCase(bookDTO.getTitle());

        if (!booksWithSameName.isEmpty()) {
            throw new ResourceAlreadyExistsException("A book with the title '" + bookDTO.getTitle() + "' already exists.");
        }
        Books book = BookMapper.mapToBook(bookDTO, categoryRepository);
        Books savedBook = bookRepository.save(book);

        String message = "Book '" + book.getTitle() + "' added successfully";
        ResponseDTO response = new ResponseDTO("success", message);

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<ResponseDTO> updateBook(int id, BookDTO bookDTO) {
        Books existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));

        List<Books> booksWithSameName = bookRepository.findByTitleContainingIgnoreCase(bookDTO.getTitle());

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setQuantity(bookDTO.getQuantity());
        existingBook.setImageURL(bookDTO.getImageURL());

        Category category = categoryRepository.findById(bookDTO.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(bookDTO.getCategory_id())));
        existingBook.setCategory(category);

        bookRepository.save(existingBook);

        String message = "Book '" + existingBook.getTitle() + "' updated successfully";
        ResponseDTO response = new ResponseDTO("success", message);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<ResponseDTO> deleteBook(int id) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));

        bookRepository.delete(book);

        String message = "Book '" + book.getTitle() +"'  deleted successfully!";
        ResponseDTO response = new ResponseDTO("success",message);
        return ResponseEntity.ok(response);
    }

    @Override
    public List<BookOutDTO> findBookSuggestions(String query) {
        List<Books> books = bookRepository.findByTitleContainingIgnoreCase(query);

        return books.stream()
                .map(BookMapper::mapToBookOutDTO)
                .collect(Collectors.toList());
    }
}
