package com.app.theraventesttask.config;

import com.app.theraventesttask.filter.FilterChainExceptionHandler;
import com.app.theraventesttask.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for setting up security configurations in the application.
 *
 * This class configures Spring Security to handle authentication and authorization.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenFilter jwtTokenFilter;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    /**
     * Constructor for SecurityConfig.
     *
     * @param authenticationProvider        Authentication provider for custom authentication.
     * @param jwtTokenFilter                JWT token filter for handling token-based authentication.
     * @param filterChainExceptionHandler   Exception handler for handling filter chain exceptions.
     */
    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtTokenFilter jwtTokenFilter,
                          FilterChainExceptionHandler filterChainExceptionHandler) {
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenFilter = jwtTokenFilter;
        this.filterChainExceptionHandler = filterChainExceptionHandler;
    }

    /**
     * Configures the security filter chain for the application.
     *
     * @param http HttpSecurity instance for configuring security settings.
     * @return SecurityFilterChain bean representing the configured security filter chain.
     * @throws Exception Thrown if an error occurs during security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(HttpMethod.POST, "/api/customers", "/api/customers/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/customers", "/api/customers/**")
                                .authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/customers/**")
                                .authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/customers/**")
                                .authenticated())
                .sessionManagement(configurer ->
                        configurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filterChainExceptionHandler, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
