package com.agshin.extapp.shared.config;

import com.agshin.extapp.shared.security.CustomAuthenticationEntryPoint;
import com.agshin.extapp.shared.security.filter.AdminFilter;
import com.agshin.extapp.shared.security.filter.IsUserApprovedFilter;
import com.agshin.extapp.shared.security.filter.JwtRequestFilter;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final AdminFilter adminFilter;
    private final IsUserApprovedFilter userApprovedFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    public SecurityConfig(
            AdminFilter adminFilter,
            JwtRequestFilter jwtRequestFilter,
            IsUserApprovedFilter userApprovedFilter,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.adminFilter = adminFilter;
        this.jwtRequestFilter = jwtRequestFilter;
        this.userApprovedFilter = userApprovedFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/sign-in").permitAll()
                        .requestMatchers("/users/sign-up").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/*",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/actuator/**").permitAll()  // TODO: change
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userApprovedFilter, JwtRequestFilter.class)
                .addFilterAfter(adminFilter, IsUserApprovedFilter.class)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("{\"Message\"" + accessDeniedException.getMessage() + "\"}");
                        }).authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }
}
