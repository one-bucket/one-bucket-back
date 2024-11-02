package com.onebucket.global.auth.config;

import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import com.onebucket.global.auth.springSecurity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <br>package name   : com.onebucket.global.auth.config
 * <br>file name      : SecurityConfig
 * <br>date           : 2024-06-25
 * <pre>
 * <span style="color: white;">[description]</span>
 * Security config class file about spring security.
 * [Permit All]
 * /test/**
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-25        jack8              init create
 * </pre>
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtValidator jwtValidator;
    private final GuestOnlyAuthorizationManager guestOnlyAuthorizationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) ->
                        auth.requestMatchers("/test/**").permitAll()
                                .requestMatchers("/sign-in").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/refresh-token").permitAll()
                                .requestMatchers("/docs/**").permitAll()
                                .requestMatchers("/test/create-testuser").permitAll()
                                .requestMatchers("/member/password/reset").permitAll()
                                .requestMatchers("/ws").permitAll()
                                .requestMatchers("/guest/**").hasRole(String.valueOf(Role.GUEST))
       //                         .requestMatchers("/admin/**").hasRole(String.valueOf(Role.ADMIN))
                                .anyRequest().access(guestOnlyAuthorizationManager))
                .addFilterBefore(new JwtAuthenticationFilter(jwtValidator),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
