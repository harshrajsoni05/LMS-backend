package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.dto.CountsDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.CategoryRepository;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.repository.UserRepository;
import com.nucleusteq.backend.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IssuanceRepository issuanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CountsDTO getDashboardCounts() {

        int totalBooks = (int) bookRepository.count();
        int categoriesCount = (int) categoryRepository.count();
        int issuancesCount = (int) issuanceRepository.countIssuedIssuances();
        int usersCount = (int) userRepository.count();

        return new CountsDTO(totalBooks, categoriesCount, issuancesCount, usersCount);
    }
}
