package com.servicio.springboot.app.usuarios.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.springboot.app.usuarios.models.dto.UserDto;
import com.servicio.springboot.app.usuarios.models.entities.User;
import com.servicio.springboot.app.usuarios.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	
	@GetMapping
	public List<UserDto> list(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Optional<UserDto> userOptional = service.findById(id);
		
		if(userOptional.isPresent()) {
			return ResponseEntity.ok(userOptional.orElseThrow());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		if(result.hasErrors()) {
			return validation(result);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody User user,BindingResult result, @PathVariable Long id){
		if(result.hasErrors()) {
			return validation(result);
		}
		Optional<UserDto> o = service.update(user, id);
		if(o.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id){
		Optional<UserDto> o = service.findById(id);
		if(o.isPresent()) {
			service.remove(id);
			return ResponseEntity.noContent().build(); //204
		}
		return ResponseEntity.notFound().build();
	}
	
	//creacion de metodo validation
		private ResponseEntity<?> validation(BindingResult result) {
			Map<String, String> errors = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errors.put(err.getField(), "El campo "+ err.getField() + " " + err.getDefaultMessage());
			});
			return ResponseEntity.badRequest().body(errors);
		}
	
	
}
