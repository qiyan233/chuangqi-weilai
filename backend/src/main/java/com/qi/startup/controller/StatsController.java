package com.qi.startup.controller;

import com.qi.startup.service.ApplicationService;
import com.qi.startup.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("approvedProjects", projectService.getApprovedProjectCount());
        stats.put("pendingApplications", applicationService.getPendingApplicationCount());
        stats.put("totalFunding", "5.2亿"); // 示例数据
        stats.put("partnerInvestors", 50);
        stats.put("mentors", 30);
        stats.put("jobsCreated", 1200);
        return ResponseEntity.ok(stats);
    }
}
