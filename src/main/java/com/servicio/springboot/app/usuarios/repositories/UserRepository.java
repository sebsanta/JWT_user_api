package com.servicio.springboot.app.usuarios.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.servicio.springboot.app.usuarios.models.entities.User;



public interface UserRepository extends CrudRepository<User, Long>{
	
	Optional<User> findByUsername(String username);
	
	//select u from User u where u.username=?1 and u.email=?2
	@Query("select u from User u where u.username=?1")
	Optional<User> getUserByUsername(String username);

}
