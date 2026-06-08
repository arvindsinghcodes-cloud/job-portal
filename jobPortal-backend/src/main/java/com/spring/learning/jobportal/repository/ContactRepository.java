package com.spring.learning.jobportal.repository;

import com.spring.learning.jobportal.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findContactByStatus(String contactStatus);

    List<Contact> findContactByStatusOrderByCreatedAtAsc(String contactStatus);

    List<Contact> findContactsByStatus(String newMessage, Sort sort);

    Page<Contact> findContactsByStatus(String newMessage, Pageable pageable);

    @Modifying
    int updateStatusById(@Param("status") String status, @Param("id") Long id, @Param("updatedBy") String updatedBy);
}