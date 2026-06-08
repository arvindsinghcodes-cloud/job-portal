package com.spring.learning.jobportal.company.service;

import com.spring.learning.jobportal.dto.CompanyDto;
import com.spring.learning.jobportal.entity.Company;

import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

    List<CompanyDto> getAllCompaniesForAdmin();

    void deleteCompanyById(Long id);

    boolean updateCompanyDetails(Long id, CompanyDto companyDto);

    boolean createCompany(CompanyDto companyDto);
}
