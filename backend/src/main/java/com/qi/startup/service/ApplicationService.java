package com.qi.startup.service;

import com.qi.startup.model.Application;
import com.qi.startup.model.User;
import com.qi.startup.repository.ApplicationRepository;
import com.qi.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public List<Application> getApplicationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        return applicationRepository.findByUser(user);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByStatus(Application.ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }

    @Transactional
    public Application createApplication(Long userId, Application application) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        application.setUser(user);
        application.setStatus(Application.ApplicationStatus.PENDING);
        return applicationRepository.save(application);
    }

    @Transactional
    public Application reviewApplication(Long applicationId, String status, String reviewNotes, Long reviewerId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("申请不存在"));

        application.setStatus(Application.ApplicationStatus.valueOf(status));
        application.setReviewNotes(reviewNotes);
        application.setReviewedBy(reviewerId);
        application.setReviewedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public long getPendingApplicationCount() {
        return applicationRepository.countByStatus(Application.ApplicationStatus.PENDING);
    }
}
