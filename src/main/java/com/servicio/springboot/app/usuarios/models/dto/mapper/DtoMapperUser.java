package com.servicio.springboot.app.usuarios.models.dto.mapper;

import com.servicio.springboot.app.usuarios.models.dto.UserDto;
import com.servicio.springboot.app.usuarios.models.entities.User;

public class DtoMapperUser {


	
	private User user;
	
	private DtoMapperUser() {	
	}
	
	public static DtoMapperUser builder() {
		return  new DtoMapperUser();
	}

	public DtoMapperUser setUser(User user) {
		this.user = user;
		return this;
	}

	public UserDto build() {
		if(user == null) {
			throw new RuntimeException("Debe parar un entity user!");
		}
		return new UserDto(this.user.getId(), user.getUsername(), user.getEmail());
	}
	
	
	
}
