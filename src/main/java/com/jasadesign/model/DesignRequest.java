package com.jasadesign.model;

import java.time.LocalDate;

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

    // Constructor
    public DesignRequest(
            int id,
            int userId,
            String title,
            String designType,
            String description,
            String color,
            String size,
            String referenceLink,
            LocalDate deadline,
            String status
    ) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.designType = designType;
        this.description = description;
        this.color = color;
        this.size = size;
        this.referenceLink = referenceLink;
        this.deadline = deadline;
        this.status = status;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDesignType() {
        return designType;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setStatus(String status) {
        this.status = status;
    }
}

