package com.Application.MyReminder.Security.Filter;

import com.Application.MyReminder.Security.CustomUserDetailsService;
import com.Application.MyReminder.Security.JWT.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);try {
                username = jwtUtil.extractUserName(jwt);
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                Map<String, String> errorDetails = new HashMap<>();
                errorDetails.put("error", "Unauthorized");
                errorDetails.put("message", "Failed to authorize");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(errorDetails);
                response.getWriter().write(jsonResponse);
                return;
            }
        }

        try {

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);


                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Unauthorized");
            errorDetails.put("message", "Failed to authorize");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorDetails);
            response.getWriter().write(jsonResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }
}

