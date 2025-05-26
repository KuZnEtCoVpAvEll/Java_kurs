package com.example.demo.Security;

import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .map(user -> {
                    System.out.println("Loaded user: " + user.getEmail() + ", role: " + user.getRole());
                    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                            .password(user.getPassword())
                            .authorities(user.getRole().name())
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET, "/api/enrollments/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/enrollments/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/enrollments/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/enrollments/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/modules/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/modules/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/modules/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/modules/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
