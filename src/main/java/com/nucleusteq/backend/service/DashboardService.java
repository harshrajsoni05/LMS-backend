package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.CountsDTO;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.CategoryRepository;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IssuanceRepository issuanceRepository;

    @Autowired
    private UserRepository userRepository;

    public CountsDTO getDashboardCounts() {
        int booksCount = (int) bookRepository.count();
        int categoriesCount = (int) categoryRepository.count();
        int issuancesCount = (int) issuanceRepository.count();
        int usersCount = (int) userRepository.count();

        return new CountsDTO(booksCount, categoriesCount, issuancesCount, usersCount);
    }
}
