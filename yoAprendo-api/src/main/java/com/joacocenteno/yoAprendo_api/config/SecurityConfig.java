package com.joacocenteno.yoAprendo_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.joacocenteno.yoAprendo_api.security.JwtAuthFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET,
                    "/api/courses/**",
                    "/api/levels/**",
                    "/api/topics/**",
                    "/api/lessons/**",
                    "/api/exercises/**"
                ).authenticated()

                .requestMatchers(HttpMethod.POST,
                    "/api/courses/**", "/api/levels/**", "/api/topics/**",
                    "/api/lessons/**", "/api/exercises/**"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,
                    "/api/courses/**", "/api/levels/**", "/api/topics/**",
                    "/api/lessons/**", "/api/exercises/**"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,
                    "/api/courses/**", "/api/levels/**", "/api/topics/**",
                    "/api/lessons/**", "/api/exercises/**"
                ).hasRole("ADMIN")


                .requestMatchers("/api/exercise-progress/attempt").hasAnyRole("STUDENT", "ADMIN")

                .requestMatchers("/api/exercise-progress/**").hasAnyRole("ADMIN", "SUPERVISOR")

                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/cecoes/**").hasAnyRole("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/cecoes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/cecoes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/cecoes/**").hasRole("ADMIN")

                .requestMatchers("/api/progress/**").hasAnyRole("ADMIN", "SUPERVISOR")

                .requestMatchers("/api/estadisticas/**").hasAnyRole("ADMIN", "SUPERVISOR")

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"No autorizado: token inválido o ausente\"}"
                    );
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Acceso denegado\"}"
                    );
                })
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {

                    SecurityContextHolder.clearContext();

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");

                    response.getWriter().write("{\"message\":\"Sesión cerrada correctamente\"}");
                }))
            
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
