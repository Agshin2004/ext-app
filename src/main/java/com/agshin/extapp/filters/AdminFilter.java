package com.agshin.extapp.filters;

import com.agshin.extapp.config.AdminAuthenticationToken;
import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Profile("dev")
@Component
public class AdminFilter extends OncePerRequestFilter {
    private static final String HEADER = "x-admin-token";
    private static final String DEV_SECRET = "admin";
    private final UserRepository userRepository;

    public AdminFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            // already logged in
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HEADER);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!Objects.equals(token, DEV_SECRET)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("Access Denied. Development stage");
            response.flushBuffer();
            return;
        }

        User admin = userRepository.findByUsername("agshin")
                .orElseThrow(() -> new DataNotFoundException("Admin user is missing in dev DB"));

        CustomUserDetails userDetails = new CustomUserDetails(admin);

        var adminAuthenticationToken = new AdminAuthenticationToken(userDetails);
        var emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(adminAuthenticationToken);
        SecurityContextHolder.setContext(emptyContext);

        filterChain.doFilter(request, response);
    }
}
