package com.qi.startup.repository;

import com.qi.startup.model.Project;
import com.qi.startup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
    List<Project> findByStatus(Project.ProjectStatus status);
    List<Project> findByIndustry(String industry);

    @Query("SELECT p FROM Project p WHERE p.status = 'APPROVED' AND " +
           "(:industry IS NULL OR p.industry = :industry) AND " +
           "(:stage IS NULL OR p.stage = :stage)")
    List<Project> findApprovedProjects(
        @Param("industry") String industry,
        @Param("stage") String stage
    );

    long countByStatus(Project.ProjectStatus status);
}
