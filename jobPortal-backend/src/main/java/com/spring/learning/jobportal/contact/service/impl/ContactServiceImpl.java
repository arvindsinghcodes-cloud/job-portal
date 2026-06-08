package com.spring.learning.jobportal.contact.service.impl;

import com.spring.learning.jobportal.constants.ApplicationConstants;
import com.spring.learning.jobportal.contact.service.IContactService;
import com.spring.learning.jobportal.dto.ContactRequestDto;
import com.spring.learning.jobportal.dto.ContactResponseDto;
import com.spring.learning.jobportal.entity.Contact;
import com.spring.learning.jobportal.repository.ContactRepository;
import com.spring.learning.jobportal.util.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.learning.jobportal.constants.ApplicationConstants.NEW_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {


    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        boolean result = false;
        Contact contact = contactRepository.save(transformToContactEntity(contactRequestDto));
        if(contact !=null && contact.getId()!=null){
            result = true;
        }
        return result;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
        List<Contact> contacts = contactRepository.findContactByStatusOrderByCreatedAtAsc(NEW_MESSAGE);
        return contacts.stream().map(this::transformToContactResponseDto).collect(Collectors.toList());

    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir) {
        // Create Sort object based on sortBy and sortDir parameters
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        List<Contact> contacts = contactRepository.findContactsByStatus(
                ApplicationConstants.NEW_MESSAGE, sort);
        List<ContactResponseDto> responseDtos = contacts.stream()
                .map(this::transformToContactResponseDto)
                .collect(Collectors.toList());
        return responseDtos;
    }

    @Override
    public Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(int pageNumber, int pageSize, String sortBy, String sortDir) {
            // Create Sort object based on sortBy and sortDir parameters
            Sort sort = sortDir.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            // Create Pageable object with page number, page size, and sorting
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            // Fetch paginated and sorted contacts from repository
            Page<Contact> contactPage = contactRepository.findContactsByStatus(
                    ApplicationConstants.NEW_MESSAGE, pageable);

            // Transform Contact entities to ContactResponseDto
            Page<ContactResponseDto> responseDtoPage = contactPage.map(this::transformToContactResponseDto);
            return responseDtoPage;
    }

    @Override
    @Transactional
    public boolean closeContactMsg(Long id, String closedMessage) {
        int updatedRows = contactRepository.updateStatusById(closedMessage,id, ApplicationUtil.getLoggedInUser());
        return updatedRows>0;
    }

    private Contact transformToContactEntity(ContactRequestDto contactRequestDto) {
     Contact contact = new Contact();
     BeanUtils.copyProperties(contactRequestDto, contact);
     contact.setStatus(NEW_MESSAGE);
     return contact;
    }

    private ContactResponseDto transformToContactResponseDto(Contact contact) {
       return  new ContactResponseDto(
                contact.getId(),contact.getName(),contact.getEmail(),contact.getUserType(),
                contact.getSubject(),contact.getMessage(),contact.getStatus(),contact.getCreatedAt()
        );
    }
}
