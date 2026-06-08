package com.spring.learning.jobportal.job.service.impl;

import com.spring.learning.jobportal.dto.JobApplicationDto;
import com.spring.learning.jobportal.dto.JobDto;
import com.spring.learning.jobportal.dto.UpdateJobApplicationDto;
import com.spring.learning.jobportal.entity.Job;
import com.spring.learning.jobportal.entity.JobApplication;
import com.spring.learning.jobportal.entity.JobPortalUser;
import com.spring.learning.jobportal.job.service.IJobService;
import com.spring.learning.jobportal.repository.JobApplicationRepository;
import com.spring.learning.jobportal.repository.JobPortalUserRepository;
import com.spring.learning.jobportal.repository.JobRepository;
import com.spring.learning.jobportal.util.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl  implements IJobService {

    private final JobRepository jobRepository;
    private final JobPortalUserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream()
                .map(ApplicationUtil::transformJobToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {
        // Validate status
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return ApplicationUtil.transformJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        // Validate employer and get their company
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = tranformDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return ApplicationUtil.transformJobToDto(savedJob);
    }

    @Override
    public List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobIdOrderByAppliedAtAsc(jobId);
        return applications.stream()
                .map(ApplicationUtil::mapToJobApplicationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean updateJobApplication(UpdateJobApplicationDto updateJobApplicationDto) {
        int updatedRows = jobApplicationRepository.updateStatusAndNotesById(
                updateJobApplicationDto.status().name(), updateJobApplicationDto.notes(), updateJobApplicationDto.applicationId(), ApplicationUtil.getLoggedInUser());
        return updatedRows > 0;
    }

    private Job tranformDtoToEntity(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }
}
