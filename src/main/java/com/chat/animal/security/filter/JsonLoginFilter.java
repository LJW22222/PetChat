package com.chat.animal.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonLoginFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (request.getContentType() != null &&
                request.getContentType().toLowerCase().contains("application/json")) {
            try (var is = request.getInputStream();
                 var reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String body = reader.lines().collect(Collectors.joining());
                var map = new ObjectMapper().readValue(body, new TypeReference<Map<String,String>>() {});
                String email = map.get("email");
                String password = map.get("password");
                var authRequest = new UsernamePasswordAuthenticationToken(email, password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new AuthenticationServiceException("Invalid login JSON", e);
            }
        }
        return super.attemptAuthentication(request, response);
    }
}