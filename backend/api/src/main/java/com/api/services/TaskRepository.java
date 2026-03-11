package com.api.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, RepositoryPostgres {
}