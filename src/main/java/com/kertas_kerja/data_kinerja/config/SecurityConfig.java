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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Panggil config CORS
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow Preflight (OPTIONS) - Wajib buat React/NextJS fetch
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Public endpoints
                        .requestMatchers("/actuator/health", "/public/**").permitAll()
                        .requestMatchers("/auth/**", "/jenisdata/**", "/jenisdataopd/**", "/datakinerjapemda/**", "/datakinerjaopd/**").permitAll()

                        // 3. Swagger & Protected endpoints
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

        // 🔥 PERBAIKAN 1: Gunakan AllowedOrigins eksplisit seperti kode yang berhasil
        // Pastikan URL frontend Zeabur kamu juga masuk sini!
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://127.0.0.1:3000",
                "https://kta-service.zeabur.app", // Contoh domain zeabur kamu (sesuaikan jika beda)
                "https://nama-project-frontend-kamu.zeabur.app" // TAMBAHKAN URL FRONTEND KAMU DISINI
        ));

        // 🔥 PERBAIKAN 2: Allowed Methods lengkap
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 🔥 PERBAIKAN 3: Gunakan setAllowedHeaders (List) bukan addAllowedHeader
        configuration.setAllowedHeaders(List.of("*"));

        // Expose header agar frontend bisa baca (terutama jika pakai custom header auth nanti)
        configuration.setExposedHeaders(List.of("Authorization", "X-Session-Id"));

        // Allow credentials (Cookies/Auth headers)
        configuration.setAllowCredentials(true);

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