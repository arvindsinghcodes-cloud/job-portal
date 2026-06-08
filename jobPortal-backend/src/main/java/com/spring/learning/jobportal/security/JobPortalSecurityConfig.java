package com.spring.learning.jobportal.security;

import com.spring.learning.jobportal.security.filter.JwtTokenValidatorFilter;
import com.spring.learning.jobportal.security.util.CorsProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobPortalSecurityConfig {

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Qualifier("securedPaths")
    private final List<String> securedPaths;

    @Qualifier("adminPaths")
    private final List<String> adminPaths;

    @Qualifier("employerPaths")
    private final List<String> employerPaths;

    @Qualifier("jobseekerPaths")
    private final List<String> jobseekerPaths;

    private final CorsProperties corsProperties;

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        return http
              //  .csrf(csrfConfig ->csrfConfig.disable())
                .csrf(csrfConfig -> csrfConfig
                        .ignoringRequestMatchers("/jobportal/actuator/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests->{
                        publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                        adminPaths.forEach(path -> requests.requestMatchers(path).hasRole("ADMIN"));
                        employerPaths.forEach(path -> requests.requestMatchers(path).hasRole("EMPLOYER"));
                        jobseekerPaths.forEach(path -> requests.requestMatchers(path).hasRole("JOB_SEEKER"));
                        securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                        requests.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class)
//                        requests.requestMatchers("/api/companies/public").permitAll()
//                                .requestMatchers("/api/contacts/public").permitAll())
//                        requests.requestMatchers(RegexRequestMatcher.regexMatcher(".*public$")).permitAll()
//                                .requestMatchers("/api/swagger-ui.html",
//                                            "/swagger-ui/**",
//                                            "/api/v3/api-docs/**",
//                                            "/swagger-resources/**",
//                                            "/swagger-ui.html",
//                                            "/webjars/**").permitAll())
                .formLogin(flc -> flc.disable())
                .httpBasic(htb -> htb.disable())
                .exceptionHandling(exception -> exception
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"Access Denied\", \"message\": \"You don't have permission to access this resource\"}");
                                })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
                        })

                )
                .build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        corsConfiguration.setAllowedMethods(corsProperties.getAllowedMethods());
        corsConfiguration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());
        corsConfiguration.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
////        String pass1 = passwordEncoder().encode("arvind@123");
////        System.out.println(pass1);
////
////        String pass2 = passwordEncoder().encode("admin@123");
////        System.out.println(pass2);
//
//        //pass1 encode password
//        var user1 =  User.builder().username("arvind").password("$2a$10$Te.P1uqdXb6o/mm80/Q4FusxZfomHcQBxY6ZLniv/Nz4iu7QN.RTC")
//                .roles("USER").build();
//
//        //pass2 encoded password
//       var user2 =  User.builder().username("admin").password("$2a$10$8y8NeBv6d.uJkyGUK6jiCepO8/Hp0nfMJ0OPs9gjVDbobs.bDwEr6")
//                .roles("ADMIN").build();
//
//       return new InMemoryUserDetailsManager(user1,user2);
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationProvider authenticationProvider) {
//        var authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
      //  return new ProviderManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker  compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
