package com.quest.etna.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -9077248927410675523L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        // Set the response status to 401 (unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Set the content type of the response to application/json
        response.setContentType("application/json");

        // Write a JSON error message to the response
        String json = "{\"message\": \"Token invalide\"}";
        response.getWriter().write(json);
    }
}