package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.dto.CountsDTO;
import com.nucleusteq.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/counts")
    public CountsDTO getCounts() {
        return dashboardService.getDashboardCounts();
    }
}

