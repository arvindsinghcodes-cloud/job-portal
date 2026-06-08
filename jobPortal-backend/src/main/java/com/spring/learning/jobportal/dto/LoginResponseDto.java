package com.spring.learning.jobportal.dto;

public record LoginResponseDto(String message,UserDto user,String jwtToken  ) {
}
