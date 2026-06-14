package com.qi.startup.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String industry;

    @Column(length = 20)
    private String stage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "pain_point", columnDefinition = "TEXT")
    private String painPoint;

    @Column(name = "business_model", columnDefinition = "TEXT")
    private String businessModel;

    @Column(name = "core_tech", columnDefinition = "TEXT")
    private String coreTech;

    @Column(name = "target_market", columnDefinition = "TEXT")
    private String targetMarket;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "user_count")
    private Integer userCount;

    @Column(name = "dau_mau", length = 50)
    private String dauMau;

    @Column(length = 50)
    private String mrr;

    @Column(length = 50)
    private String arr;

    @Column(name = "funding_round", length = 20)
    private String fundingRound;

    @Column(name = "funding_amount", length = 50)
    private String fundingAmount;

    @Column(length = 50)
    private String valuation;

    @Column(name = "equity_percent", length = 20)
    private String equityPercent;

    @Column(name = "fund_usage", columnDefinition = "TEXT")
    private String fundUsage;

    @Column(name = "bp_file_url")
    private String bpFileUrl;

    @Column(name = "financial_model_url")
    private String financialModelUrl;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.DRAFT;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ProjectStatus {
        DRAFT,
        PENDING,
        APPROVED,
        REJECTED
    }
}
