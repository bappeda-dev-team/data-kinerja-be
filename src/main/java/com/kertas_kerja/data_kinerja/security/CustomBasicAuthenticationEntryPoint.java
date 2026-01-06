package com.kertas_kerja.data_kinerja.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException)
            throws IOException {

        // Langsung hardcode realm name di sini (lebih simpel)
        response.addHeader("WWW-Authenticate", "Basic realm=\"Swagger UI Access\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json"); // Jangan lupa set content type

        // For Swagger paths, force Basic Auth popup logic
        String requestPath = request.getRequestURI();
        String message = (requestPath.contains("swagger-ui") || requestPath.contains("api-docs"))
                ? "Basic authentication required for Swagger UI access"
                : "Basic authentication required";

        response.getWriter().write(String.format("""
            {
                "success": false,
                "statusCode": 401,
                "message": "%s"
            }
            """, message));
    }
}