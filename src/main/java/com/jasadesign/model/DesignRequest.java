package com.jasadesign.model;

import com.jasadesign.config.AppConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DesignRequest {

    private int id;
    private int userId;
    private String title;
    private String designType;
    private String description;
    private String color;
    private String size;
    private String referenceLink;
    private LocalDate deadline;
    private String status;
    private LocalDateTime createdAt;

    // Overloading 1: constructor kosong (dipakai DAO saat membaca dari database)
    public DesignRequest() {
        this.status = AppConfig.STATUS_MENUNGGU;
    }

    // Overloading 2: constructor untuk pesanan baru dari form
    public DesignRequest(int userId, String title, String designType, String description,
                         String color, String size, String referenceLink, LocalDate deadline) {
        this();
        this.userId = userId;
        this.title = title;
        this.designType = designType;
        this.description = description;
        this.color = color;
        this.size = size;
        this.referenceLink = referenceLink;
        this.deadline = deadline;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDesignType() { return designType; }
    public void setDesignType(String designType) { this.designType = designType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getReferenceLink() { return referenceLink; }
    public void setReferenceLink(String referenceLink) { this.referenceLink = referenceLink; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

