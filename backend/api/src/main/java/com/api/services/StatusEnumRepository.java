package com.api.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.models.StatusEnum;

public interface StatusEnumRepository extends JpaRepository<StatusEnum, Long>, RepositoryPostgres {
}