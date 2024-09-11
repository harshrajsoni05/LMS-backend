package com.nucleusteq.backend.mapper;

import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.dto.BookOutDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Category;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.repository.CategoryRepository;

public final class BookMapper {

    private BookMapper() {
    }

    public static BookDTO mapToBookDTO(Books book) {
        if (book == null) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setCategory_id(book.getCategory() != null ? book.getCategory().getId() : 0);
        bookDTO.setQuantity(book.getQuantity());
        bookDTO.setImageURL(book.getImageURL());

        return bookDTO;
    }

    public static BookOutDTO mapToBookOutDTO(Books book) {
        if (book == null) {
            return null;
        }

        BookOutDTO bookOutDTO = new BookOutDTO();
        bookOutDTO.setId(book.getId());
        bookOutDTO.setTitle(book.getTitle());
        bookOutDTO.setAuthor(book.getAuthor());
        bookOutDTO.setCategory_id(book.getCategory() != null ? book.getCategory().getId() : 0);
        bookOutDTO.setQuantity(book.getQuantity());
        bookOutDTO.setImageURL(book.getImageURL());
        bookOutDTO.setCategory(CategoryMapper.mapToCategoryDTO(book.getCategory()));

        return bookOutDTO;
    }

    public static Books mapToBook(BookDTO bookDTO, CategoryRepository categoryRepository) {
        Category category = categoryRepository.findById(bookDTO.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(bookDTO.getCategory_id())));

        Books book = new Books();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setQuantity(bookDTO.getQuantity());
        book.setImageURL(bookDTO.getImageURL());
        book.setCategory(category); // Correctly set Category entity

        return book;
    }
}

