package com.api.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.models.AssigneeEnum;

public interface AssigneeEnumRepository extends JpaRepository<AssigneeEnum, Long>, RepositoryPostgres {
}