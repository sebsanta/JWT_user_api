package com.servicio.springboot.app.usuarios.auth;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class TokenJwtConfig {
	
	public final static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);;
	public final static String PREFIX_TOKEN = "Bearer ";
	public final static String HEADER_AUTHORIZATION = "Authorization";
	public final static String SECRET_KEY_DOT = "algun_token_con_alguna_frase_secreta.";

	
	//Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	//SecretKey key =  Jwts.SIG.HS256.keyBuilder().build();

}
