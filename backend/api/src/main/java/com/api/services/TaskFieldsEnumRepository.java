package com.api.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.models.TaskFieldsEnum;

public interface TaskFieldsEnumRepository extends JpaRepository<TaskFieldsEnum, Long>, RepositoryPostgres {
}