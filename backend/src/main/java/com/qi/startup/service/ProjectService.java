package com.qi.startup.service;

import com.qi.startup.model.Project;
import com.qi.startup.model.User;
import com.qi.startup.repository.ProjectRepository;
import com.qi.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<Project> getApprovedProjects(String industry, String stage) {
        return projectRepository.findApprovedProjects(industry, stage);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("项目不存在"));
    }

    public List<Project> getProjectsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        return projectRepository.findByUser(user);
    }

    public List<Project> getAllProjects(String status) {
        if (status == null || status.isBlank()) {
            return projectRepository.findAll();
        }
        return projectRepository.findByStatus(Project.ProjectStatus.valueOf(status));
    }

    @Transactional
    public Project createProject(Long userId, Project project) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        project.setUser(user);
        project.setStatus(Project.ProjectStatus.DRAFT);
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long userId, Long projectId, Project projectDetails) {
        Project project = getProjectById(projectId);

        if (!project.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权修改此项目");
        }

        if (project.getStatus() != Project.ProjectStatus.DRAFT) {
            throw new RuntimeException("只能修改草稿状态的项目");
        }

        // 更新项目字段
        project.setName(projectDetails.getName());
        project.setIndustry(projectDetails.getIndustry());
        project.setStage(projectDetails.getStage());
        project.setDescription(projectDetails.getDescription());
        project.setPainPoint(projectDetails.getPainPoint());
        project.setBusinessModel(projectDetails.getBusinessModel());
        project.setCoreTech(projectDetails.getCoreTech());
        project.setTargetMarket(projectDetails.getTargetMarket());
        project.setDemoUrl(projectDetails.getDemoUrl());
        project.setUserCount(projectDetails.getUserCount());
        project.setDauMau(projectDetails.getDauMau());
        project.setMrr(projectDetails.getMrr());
        project.setArr(projectDetails.getArr());
        project.setFundingRound(projectDetails.getFundingRound());
        project.setFundingAmount(projectDetails.getFundingAmount());
        project.setValuation(projectDetails.getValuation());
        project.setEquityPercent(projectDetails.getEquityPercent());
        project.setFundUsage(projectDetails.getFundUsage());

        return projectRepository.save(project);
    }

    @Transactional
    public Project submitProject(Long userId, Long projectId) {
        Project project = getProjectById(projectId);

        if (!project.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权操作此项目");
        }

        if (project.getStatus() != Project.ProjectStatus.DRAFT) {
            throw new RuntimeException("只能提交草稿状态的项目");
        }

        project.setStatus(Project.ProjectStatus.PENDING);
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        Project project = getProjectById(projectId);

        if (!project.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除此项目");
        }

        if (project.getStatus() != Project.ProjectStatus.DRAFT) {
            throw new RuntimeException("只能删除草稿状态的项目");
        }

        projectRepository.delete(project);
    }

    // 管理员审核项目
    @Transactional
    public Project reviewProject(Long projectId, String status, String reviewNotes) {
        Project project = getProjectById(projectId);
        project.setStatus(Project.ProjectStatus.valueOf(status));
        project.setReviewNotes(reviewNotes);
        project.setReviewedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public long getApprovedProjectCount() {
        return projectRepository.countByStatus(Project.ProjectStatus.APPROVED);
    }

    public long getTotalProjectCount() {
        return projectRepository.count();
    }

    public long getPendingProjectCount() {
        return projectRepository.countByStatus(Project.ProjectStatus.PENDING);
    }

    public long getRejectedProjectCount() {
        return projectRepository.countByStatus(Project.ProjectStatus.REJECTED);
    }
}
