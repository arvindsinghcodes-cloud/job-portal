/**
 * API Configuration
 * Centralized configuration for all API endpoints
 */

// Base URL for the backend API
export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

// API Version Configuration
// Default version sent in Accept header for all REST API calls
// Format: application/vnd.eazyapp+json;v=1.0
export const DEFAULT_API_VERSION = "1.0";

// Supported API versions
export const SUPPORTED_API_VERSIONS = ["1.0", "2.0", "3.0"];

// Helper to generate Accept header value for a specific version
export const getAcceptHeader = (version = DEFAULT_API_VERSION) =>
  `application/vnd.eazyapp+json;v=${version}`;

// API Endpoints
export const API_ENDPOINTS = {
  // Company endpoints
  COMPANIES: "/companies/public",
  COMPANY_BY_ID: (id) => `/companies/${id}/admin`,
  ADMIN_COMPANIES: "/companies/admin",
  CREATE_COMPANY: "/companies/admin",

  // Job endpoints (placeholder for future use)
  JOBS: "/jobs",
  JOB_BY_ID: (id) => `/jobs/${id}`,

  // Auth endpoints (placeholder for future use)
  LOGIN: "/auth/login/public",
  REGISTER: "/auth/register/public",
  LOGOUT: "/auth/logout",

  // User endpoints
  PROFILE: "/users/profile/jobseeker",
  UPDATE_PROFILE: "/users/profile/jobseeker",
  // Specific asset endpoints (backend expects asset then user type)
  PROFILE_PICTURE: "/users/profile/picture/jobseeker",
  PROFILE_RESUME: "/users/profile/resume/jobseeker",

  // Contact endpoints
  CONTACTS: "/contacts/public",
  CONTACT_BY_ID: (id) => `/contacts/${id}`,
  ADMIN_CONTACTS: "/contacts/admin",
  ADMIN_CONTACTS_SORT: "/contacts/sort/admin",
  ADMIN_CONTACTS_PAGE: "/contacts/page/admin",
  UPDATE_CONTACT_STATUS: (id) => `/contacts/${id}/status/admin`,

  // CSRF token endpoint
  CSRF_TOKEN: "/csrf-token/public",

  // Admin User Management endpoints
  SEARCH_USER_BY_EMAIL: "users/search/admin",
  ELEVATE_TO_EMPLOYER: (userId) => `/users/${userId}/role/employer/admin`,
  ASSIGN_COMPANY_TO_EMPLOYER: (userId, companyId) => `/users/${userId}/company/${companyId}/admin`,

  // Employer Job Management endpoints
  EMPLOYER_JOBS: "/jobs/employer",
  POST_JOB: "/jobs/employer",
  UPDATE_JOB_STATUS: (jobId) => `/jobs/${jobId}/status/employer`,

  // Saved Jobs endpoints
  SAVED_JOBS: "/users/saved-jobs/jobseeker",
  SAVED_JOB_IDS: "/users/saved-jobs/ids/jobseeker",
  SAVE_JOB: (jobId) => `/users/saved-jobs/${jobId}/jobseeker`,
  UNSAVE_JOB: (jobId) => `/users/saved-jobs/${jobId}/jobseeker`,
  CHECK_JOB_SAVED: (jobId) => `/users/saved-jobs/check/${jobId}/jobseeker`,

  // Job Application endpoints (user-scoped jobseeker routes)
  JOB_APPLICATIONS: "/users/job-applications/jobseeker",
  MY_APPLICATIONS: "/users/job-applications/jobseeker",
  APPLIED_JOB_IDS: "/users/job-applications/ids/jobseeker",
  APPLY_JOB: "/users/job-applications/jobseeker",
  WITHDRAW_APPLICATION: (jobId) => `/users/job-applications/${jobId}/jobseeker`,
  CHECK_APPLIED: (jobId) => `/users/job-applications/${jobId}/jobseeker`,
  // Employer-scoped job application endpoints
  APPLICATIONS_BY_JOB: (jobId) => `/jobs/applications/${jobId}/employer`,
  COMPANY_APPLICATIONS: "/jobs/applications/employer",
  UPDATE_APPLICATION_STATUS: "/jobs/applications/employer",
};

// HTTP Headers
export const API_HEADERS = {
  "Content-Type": "application/json",
};

// Request timeout (in milliseconds)
export const API_TIMEOUT = 30000;

export default {
  API_BASE_URL,
  API_ENDPOINTS,
  API_HEADERS,
  API_TIMEOUT,
  DEFAULT_API_VERSION,
  SUPPORTED_API_VERSIONS,
};
