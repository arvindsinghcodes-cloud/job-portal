package com.spring.learning.jobportal.security;

import com.spring.learning.jobportal.entity.JobPortalUser;
import com.spring.learning.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class JobPortalNonProdUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final JobPortalUserRepository  jobPortalUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        JobPortalUser jobPortalUser = jobPortalUserRepository.findJobPortalUserByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException(
                        "User Details not found for the user "+username));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(jobPortalUser.getRole().getName()));
        return new UsernamePasswordAuthenticationToken(jobPortalUser,null,authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
