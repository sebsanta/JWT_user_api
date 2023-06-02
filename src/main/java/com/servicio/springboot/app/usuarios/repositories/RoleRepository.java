package com.servicio.springboot.app.usuarios.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.servicio.springboot.app.usuarios.models.entities.Role;



public interface RoleRepository extends CrudRepository<Role, Long>{
	
	Optional<Role> findByName(String name);
	

}
