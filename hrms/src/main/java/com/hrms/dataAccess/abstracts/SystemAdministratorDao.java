package com.hrms.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entities.concretes.SystemAdministrator;

@Repository
public interface SystemAdministratorDao extends JpaRepository<SystemAdministrator, Integer> {

}