package com.servicio.springboot.app.usuarios.auth.filters;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicio.springboot.app.usuarios.auth.SimpleGrantedAuthorityJsonCreator;
import com.servicio.springboot.app.usuarios.auth.TokenJwtConfig;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter{

	public JwtValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
		
		if(header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
		
		try {
		//if(TokenJwtConfig.SECRET_KEY.equals(secret)){
			Claims claims = Jwts.parserBuilder()
			.setSigningKey(TokenJwtConfig.SECRET_KEY)
			.build()
			.parseClaimsJws(token)
			.getBody();
			
			Object authoritiesClaims = claims.get("authorities");
			String username = claims.getSubject();
			Object username2 = claims.get("username");
			
			Collection<? extends GrantedAuthority> authorities = Arrays
												.asList(new ObjectMapper()
															.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
															.readValue(authoritiesClaims
															.toString()
															.getBytes(), SimpleGrantedAuthority[].class));
			
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}catch(JwtException e){
		//} else {
			Map<String, Object> body = new HashMap<>();
			body.put("Error", e.getMessage());
			body.put("message", "El token JWT no es válido!");
			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(403);
			response.setContentType("application/json");
			
		}
		
	}
	
	

}
