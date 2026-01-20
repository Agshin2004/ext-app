package com.agshin.extapp.filters;

import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.exceptions.UnauthorizedException;
import com.agshin.extapp.model.enums.RegistrationStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Profile("prod")
@Component
public class IsUserApprovedFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }


        CustomUserDetails userDetail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetail.getRegistrationStatus().equals(RegistrationStatus.INACTIVE)) {
            try {
                throw new UnauthorizedException("Account is inactive");
            } catch (UnauthorizedException e) {
                handleUnauthorizedException(response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleUnauthorizedException(HttpServletResponse response, Exception ex) throws IOException {
        sendErrorMessage(response, HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    private void sendErrorMessage(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(""" 
                {
                    "success": false,
                    "message": "%s"
                }
                """.formatted(message));
    }
}
