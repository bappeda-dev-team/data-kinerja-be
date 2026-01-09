package com.kertas_kerja.data_kinerja.config;

import com.kertas_kerja.data_kinerja.security.CustomBasicAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomBasicAuthenticationEntryPoint customBasicAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // 🔥 PENTING: CORS harus diaktifkan SEBELUM authorize
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 1. ✅ Allow semua OPTIONS request (preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. ✅ Public endpoints (TANPA AUTH)
                        .requestMatchers("/actuator/health", "/public/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/jenisdata/**").permitAll()
                        .requestMatchers("/jenisdataopd/**").permitAll()
                        .requestMatchers("/datakinerjapemda/**").permitAll()
                        .requestMatchers("/datakinerjaopd/**").permitAll()

                        // 3. Protected endpoints (PERLU AUTH)
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic
                        .realmName("Swagger UI Access")
                        .authenticationEntryPoint(customBasicAuthEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🔥 CRITICAL: Allowed Origins
        configuration.setAllowedOriginPatterns(List.of("*")); // Untuk development
        // Untuk production, ganti dengan:
        // configuration.setAllowedOrigins(Arrays.asList(
        //     "http://localhost:3000",
        //     "https://your-frontend.zeabur.app"
        // ));

        // 🔥 CRITICAL: Allowed Methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // 🔥 CRITICAL: Allowed Headers (semua header diizinkan)
        configuration.setAllowedHeaders(List.of("*"));

        // 🔥 CRITICAL: Expose Headers (agar frontend bisa baca)
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Session-Id"
        ));

        // 🔥 CRITICAL: Allow Credentials
        configuration.setAllowCredentials(true);

        // 🔥 CRITICAL: Max Age (cache preflight response)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("kertaskerja")
                .password(passwordEncoder().encode("katasandi"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}