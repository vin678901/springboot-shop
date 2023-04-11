package com.example.springbootshop.repository;

import com.example.springbootshop.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<Members, Long> {
    Members findByEmail(String email);
}
