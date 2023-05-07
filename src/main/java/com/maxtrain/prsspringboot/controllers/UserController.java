package com.maxtrain.prsspringboot.controllers;

//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.prsspringboot.entities.User;
import com.maxtrain.prsspringboot.repositories.UserRepository;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins="http://localhost:4200")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	// method handles HTTP GET requests to the URL "/users" and returns a list of all users
	@GetMapping("")
	public List<User> getAll() {
		List<User> users = userRepo.findAll();
		
		return users;
	}
	
	// method handles HTTP GET requests to the URL "/users" and returns the user with the specified ID
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable int id) {
	    Optional<User> optionalUser = userRepo.findById(id);

	    if (!optionalUser.isPresent()) {
	        // Return a 404 response if a user with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    User user = optionalUser.get();
	    return ResponseEntity.ok(user);
	}
	
	// method handles HTTP requests to the URL("/users") and creates a new user using the request body
	@PostMapping("")
	public User create(@RequestBody User newUser) {
		User user = new User();
		
		boolean userExists = userRepo.findById(newUser.getId()).isPresent();
		
		if(!userExists) {
			// save the new user to the repository
			user = userRepo.save(newUser);
		}
		
		return user;
		
	}
	
	// method handles HTTP PUT requests to the URL ("/users") and updates an existing user using the request body
	@PutMapping("")
	public User updateUser(@RequestBody User updatedUser) {
		User user = new User();
		
		 // Check if the specified user exists
		boolean userExists = userRepo.findById(updatedUser.getId()).isPresent();
		
		if (userExists) {
			// Update the request in the repository
			user = userRepo.save(updatedUser);
		}
				
		return user;
	}
	
	// method handles HTTP DELETE requests to the URL "/users/{id}" and deletes the user with the specified ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable int id) {
	    Optional<User> optionalUser = userRepo.findById(id);

	    if (!optionalUser.isPresent()) {
	        // Return a 404 response if a user with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    User user = optionalUser.get();
	    userRepo.deleteById(id);

	    return ResponseEntity.ok().build();
	}
	
	@PostMapping("/login")
//	public User authenticate(@RequestBody User loginUser) {
//		User user = new User();
//		// Retrieve the User object from the database that matches the username and password passed in via the loginUser object
//		user = userRepo.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
//		
//		// return the user object
//		return user;
//		
//	}
	public ResponseEntity authenticate(@RequestBody User loginUser) {
	User user = userRepo.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
	if (user == null) {
	return ResponseEntity.notFound().build(); // Return 404 Not Found response
	}
	return ResponseEntity.ok(user); // Return the user object with 200 OK response
	}
}
