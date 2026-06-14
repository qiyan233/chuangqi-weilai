package com.qi.startup.controller;

import com.qi.startup.model.Application;
import com.qi.startup.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/my")
    public ResponseEntity<List<Application>> getMyApplications(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Application> applications = applicationService.getApplicationsByUserId(userId);
        return ResponseEntity.ok(applications);
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(HttpServletRequest request, @RequestBody Application application) {
        Long userId = (Long) request.getAttribute("userId");
        Application created = applicationService.createApplication(userId, application);
        return ResponseEntity.ok(created);
    }

    // 管理员接口
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(
            @RequestParam(required = false) String status) {
        List<Application> applications;
        if (status != null) {
            applications = applicationService.getApplicationsByStatus(Application.ApplicationStatus.valueOf(status));
        } else {
            applications = applicationService.getAllApplications();
        }
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<Application> reviewApplication(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewNotes) {
        Long reviewerId = (Long) request.getAttribute("userId");
        Application reviewed = applicationService.reviewApplication(id, status, reviewNotes, reviewerId);
        return ResponseEntity.ok(reviewed);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
