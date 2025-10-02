package com.chat.animal.security.config;

import com.chat.animal.security.filter.JsonLoginFilter;
import com.chat.animal.security.filter.JwtAuthenticationFilter;
import com.chat.animal.security.handler.FailedAuthenticationEntryPoint;
import com.chat.animal.security.handler.LoginSuccessHandler;
import com.chat.animal.security.handler.OAuthFailureHandler;
import com.chat.animal.security.handler.OAuthSuccessHandler;
import com.chat.animal.security.service.CustomOauth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2Service customOauth2Service;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final FailedAuthenticationEntryPoint failedAuthenticationEntryPoint;

    private final OAuthFailureHandler oAuthFailureHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final OAuthSuccessHandler oAuthSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .exceptionHandling(exception ->
//                        exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jsonLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
//                .addFilter(jsonLoginFilter)
                .formLogin(form -> form
                        .loginProcessingUrl("/v1/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler((req,res,ex) -> res.sendError(401, "Bad credentials"))
                        .permitAll()
                )
                .authorizeHttpRequests(authRequests -> authRequests
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/", "/v1/auth/**", "/oauth2/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(customOauth2Service)) // 사용자 정보 서비스
                        .successHandler(oAuthSuccessHandler) // OAuth2 성공 핸들러
                        .failureHandler(oAuthFailureHandler)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(failedAuthenticationEntryPoint))
        ;

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public JsonLoginFilter jsonLoginFilter(
//            AuthenticationManager authenticationManager,
//            LoginSuccessHandler loginSuccessHandler
//    ) {
//        JsonLoginFilter filter = new JsonLoginFilter();
//        filter.setAuthenticationManager(authenticationManager);
//        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
//        filter.setAuthenticationFailureHandler((req, res, ex) -> res.sendError(401, "Bad credentials"));
//        return filter;
//    }

}