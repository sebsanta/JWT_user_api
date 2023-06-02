package com.servicio.springboot.app.usuarios.auth.filters;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.servicio.springboot.app.usuarios.auth.TokenJwtConfig;


import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicio.springboot.app.usuarios.models.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		
		User user = null;
		String username = null;
		String password = null;
		
		try {
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			username = user.getUsername();
			password = user.getPassword();
		} catch (StreamReadException e) {

			e.printStackTrace();
		} catch (DatabindException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String username =((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
				.getUsername();
		
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		
		boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		claims.put("isAdmin", isAdmin);
		//String originalInput = TokenJwtConfig.SECRET_KEY_DOT + username;
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(TokenJwtConfig.SECRET_KEY)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +3600000))
				.compact();
				
		
		response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);
		
		
		
		Map<String, Object> body = new HashMap<>();
		body.put("token", token);
		body.put("Message", String.format("Hola %s, has iniciado sesión", username));
		body.put("username", username);
	 	response.getWriter().write(new ObjectMapper().writeValueAsString(body));
	 	response.setStatus(200);
	 	response.setContentType("application/json");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, Object> body = new HashMap<>();
		body.put("message", "Error en la autenticación username o password incorrecto");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
		
	}
	
	

}
