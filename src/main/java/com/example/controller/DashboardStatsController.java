package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DashboardStats;
import com.example.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardStatsController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getDashboardStats(@RequestHeader("userId") String userId) {
        try {
            DashboardStats stats = dashboardService.getDashboardStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Handle errors here
        }
    }
    
    @GetMapping("/email-counts")
    public ResponseEntity<DashboardStats> getEmailCountStats(@RequestHeader("userId") String userId) {
        try {
            DashboardStats stats = dashboardService.getEmailCountStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Handle errors here
        }
    }
    
    
}
