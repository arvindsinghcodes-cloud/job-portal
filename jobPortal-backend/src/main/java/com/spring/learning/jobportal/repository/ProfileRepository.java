package com.spring.learning.jobportal.repository;

import com.spring.learning.jobportal.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}