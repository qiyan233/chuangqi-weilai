package com.qi.startup.controller;

import com.qi.startup.model.Application;
import com.qi.startup.model.Project;
import com.qi.startup.model.User;
import com.qi.startup.service.ApplicationService;
import com.qi.startup.repository.UserRepository;
import com.qi.startup.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final UserRepository userRepository;

    public AdminController(ProjectService projectService,
                           ApplicationService applicationService,
                           UserRepository userRepository) {
        this.projectService = projectService;
        this.applicationService = applicationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", projectService.getTotalProjectCount());
        stats.put("pendingProjects", projectService.getPendingProjectCount());
        stats.put("approvedProjects", projectService.getApprovedProjectCount());
        stats.put("rejectedProjects", projectService.getRejectedProjectCount());
        stats.put("totalApplications", applicationService.getTotalApplicationCount());
        stats.put("pendingApplications", applicationService.getPendingApplicationCount());
        stats.put("approvedApplications", applicationService.getApprovedApplicationCount());
        stats.put("rejectedApplications", applicationService.getRejectedApplicationCount());
        stats.put("totalUsers", userRepository.count());
        stats.put("entrepreneurCount", userRepository.countByRole(User.UserRole.ENTREPRENEUR));
        stats.put("investorCount", userRepository.countByRole(User.UserRole.INVESTOR));
        stats.put("adminCount", userRepository.countByRole(User.UserRole.ADMIN));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Map<String, Object>>> getProjects(
            @RequestParam(required = false) String status) {
        List<Map<String, Object>> projects = projectService.getAllProjects(status).stream()
            .map(this::toProjectRow)
            .toList();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/projects/{id}/review")
    public ResponseEntity<Map<String, Object>> reviewProject(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewNotes) {
        Project project = projectService.reviewProject(id, status, reviewNotes);
        return ResponseEntity.ok(toProjectRow(project));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Map<String, Object>>> getApplications(
            @RequestParam(required = false) String status) {
        List<Application> applications = status == null || status.isBlank()
            ? applicationService.getAllApplications()
            : applicationService.getApplicationsByStatus(Application.ApplicationStatus.valueOf(status));
        return ResponseEntity.ok(applications.stream().map(this::toApplicationRow).toList());
    }

    @PutMapping("/applications/{id}/review")
    public ResponseEntity<Map<String, Object>> reviewApplication(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewNotes,
            HttpServletRequest request) {
        Long reviewerId = (Long) request.getAttribute("userId");
        Application application = applicationService.reviewApplication(id, status, reviewNotes, reviewerId);
        return ResponseEntity.ok(toApplicationRow(application));
    }

    private Map<String, Object> toProjectRow(Project project) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", project.getId());
        row.put("name", project.getName());
        row.put("industry", project.getIndustry());
        row.put("stage", project.getStage());
        row.put("description", project.getDescription());
        row.put("fundingRound", project.getFundingRound());
        row.put("fundingAmount", project.getFundingAmount());
        row.put("status", project.getStatus());
        row.put("createdAt", project.getCreatedAt());
        row.put("ownerName", project.getUser().getRealName() != null ? project.getUser().getRealName() : project.getUser().getUsername());
        row.put("ownerEmail", project.getUser().getEmail());
        return row;
    }

    private Map<String, Object> toApplicationRow(Application application) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", application.getId());
        row.put("status", application.getStatus());
        row.put("teamMembers", application.getTeamMembers());
        row.put("equityStructure", application.getEquityStructure());
        row.put("teamTags", application.getTeamTags());
        row.put("reviewNotes", application.getReviewNotes());
        row.put("reviewedAt", application.getReviewedAt());
        row.put("createdAt", application.getCreatedAt());
        row.put("applicantName", application.getUser().getRealName() != null ? application.getUser().getRealName() : application.getUser().getUsername());
        row.put("applicantEmail", application.getUser().getEmail());
        row.put("projectId", application.getProject() != null ? application.getProject().getId() : null);
        row.put("projectName", application.getProject() != null ? application.getProject().getName() : null);
        return row;
    }
}
