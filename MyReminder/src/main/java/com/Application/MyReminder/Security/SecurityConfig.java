package com.Application.MyReminder.Security;

import com.Application.MyReminder.Security.Filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;
    @Autowired
    public SecurityConfig(JwtFilter jwtFilter,CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(config->
                        config

                                .requestMatchers(HttpMethod.POST, "api/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "api/login").permitAll()
                                .anyRequest().authenticated()
                );

        httpSecurity.sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }



    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
         auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();

    }

}
