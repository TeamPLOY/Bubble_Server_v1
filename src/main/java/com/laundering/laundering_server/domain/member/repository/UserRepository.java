package com.laundering.laundering_server.domain.member.repository;


import com.laundering.laundering_server.domain.member.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
}

