package com.servicio.springboot.app.usuarios.services;

import java.util.List;
import java.util.Optional;

import com.servicio.springboot.app.usuarios.models.dto.UserDto;
import com.servicio.springboot.app.usuarios.models.entities.User;

public interface UserService {
	
	
	List<UserDto> findAll();
	
	Optional<UserDto> findById(Long id);
	
	UserDto save(User user);
	Optional<UserDto> update(User user, Long id);
	
	void remove(Long id);

}
