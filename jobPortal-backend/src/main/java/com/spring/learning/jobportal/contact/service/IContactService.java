package com.spring.learning.jobportal.contact.service;

import com.spring.learning.jobportal.dto.ContactRequestDto;
import com.spring.learning.jobportal.dto.ContactResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IContactService {
    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> fetchNewContactMsgs();


    List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir);

    Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(int pageNumber, int pageSize, String sortBy, String sortDir);

    boolean closeContactMsg(Long aLong, String closedMessage);
}
