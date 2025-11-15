package com.taskapp.backend.repository;

import com.taskapp.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository provides: save, findAll, findById, deleteById, etc.
}