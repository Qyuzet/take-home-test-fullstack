package com.taskapp.backend.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String status;

    @Column(name = "task_type")
    private String type; // Bug, Feature, Documentation

    @Column(name = "priority")
    private String priority; // High, Medium, Low

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    public Task(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "pending";
        this.type = "Feature";
        this.priority = "Medium";
    }


    public Long getId() { return id; }
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title){this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description){this.description = description;}

    public String getStatus() {return status;}
    public void setStatus(String status){this.status = status;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt){this.updatedAt = updatedAt;}

    public String getType() {return type;}
    public void setType(String type){this.type = type;}

    public String getPriority() {return priority;}
    public void setPriority(String priority){this.priority = priority;}

}
