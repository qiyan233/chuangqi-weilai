package com.qi.startup.controller;

import com.qi.startup.model.Project;
import com.qi.startup.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/approved")
    public ResponseEntity<List<Project>> getApprovedProjects(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String stage) {
        List<Project> projects = projectService.getApprovedProjects(industry, stage);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        // 公开接口只返回已审核通过的项目
        if (project.getStatus() != Project.ProjectStatus.APPROVED) {
            throw new RuntimeException("项目不存在或未审核通过");
        }
        return ResponseEntity.ok(project);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Project>> getMyProjects(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Project> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(HttpServletRequest request, @RequestBody Project project) {
        Long userId = (Long) request.getAttribute("userId");
        Project created = projectService.createProject(userId, project);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody Project project) {
        Long userId = (Long) request.getAttribute("userId");
        Project updated = projectService.updateProject(userId, id, project);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Project> submitProject(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        Project submitted = projectService.submitProject(userId, id);
        return ResponseEntity.ok(submitted);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        projectService.deleteProject(userId, id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "项目已删除");
        return ResponseEntity.ok(response);
    }

    // 注意：管理员审核接口已移至 AdminController (/api/admin/projects/{id}/review)
}
