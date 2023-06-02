package com.servicio.springboot.app.usuarios.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import com.servicio.springboot.app.usuarios.auth.filters.JwtAuthenticationFilter;
import com.servicio.springboot.app.usuarios.auth.filters.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {
	
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager() throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		return http.authorizeHttpRequests()
				.requestMatchers(HttpMethod.GET, "/users").permitAll()
				.requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.POST,"/users").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE,"/users/{id}").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT,"/users/{id}").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
				.addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
				.csrf(config -> config.disable())
				.sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}
	

}

