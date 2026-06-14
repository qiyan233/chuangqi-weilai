package com.qi.startup.repository;

import com.qi.startup.model.Application;
import com.qi.startup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByStatus(Application.ApplicationStatus status);
    long countByStatus(Application.ApplicationStatus status);
}
