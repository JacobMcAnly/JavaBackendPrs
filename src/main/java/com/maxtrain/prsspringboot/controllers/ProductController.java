package com.maxtrain.prsspringboot.controllers;

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

import com.maxtrain.prsspringboot.entities.Product;
import com.maxtrain.prsspringboot.entities.User;
import com.maxtrain.prsspringboot.repositories.ProductRepository;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepo;
	
	// handles HTTP GET requests to the base URL ("/products") and returns a list of all products
	@GetMapping("")
	public List<Product> getAll() {
		// retrieve all products from the repository
		List<Product> products = productRepo.findAll(); 
		
		return products; 
	}
	
	// method handles HTTP GET requests to the URL "/products/{id}" (e.g. "/products/1") and returns the product with the specified ID
	@GetMapping("/{id}")
	public ResponseEntity<Product> getById(@PathVariable int id) {
	    Optional<Product> optionalProduct = productRepo.findById(id);

	    if (!optionalProduct.isPresent()) {
	        // Return a 404 response if a product with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    Product product = optionalProduct.get();
	    return ResponseEntity.ok(product);
	}
	
	// method handles HTTP POST requests to the base URL ("/products") and creates a new product using the request body
	@PostMapping("")
	public Product create(@RequestBody Product newProduct) {
		Product product = new Product();
		
		// Check if a product with the same ID already exists
		boolean productExists = productRepo.findById(newProduct.getId()).isPresent(); 
		
		if(!productExists) {
			// Save the new product to the repository
			product = productRepo.save(newProduct); 
		}
		
		return product; 
		
	}
	
	// method handles HTTP PUT requests to the base URL ("/products") and updates an existing product using the request body
	@PutMapping("")
	public ResponseEntity<Product> updateProduct(@RequestBody Product updatedProduct) {
	    // Check if the specified user exists
	    Optional<Product> optionalProduct = productRepo.findById(updatedProduct.getId());

	    if (!optionalProduct.isPresent()) {
	    	return ResponseEntity.notFound().build();
	    } 
	    
	    Product product = productRepo.save(updatedProduct);
	    return ResponseEntity.ok(product);
	}
	
	// method handles HTTP DELETE requests to the URL "/products/{id}" and deletes the product with the specified ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable int id) {
	    Optional<Product> optionalProduct = productRepo.findById(id);

	    if (!optionalProduct.isPresent()) {
	        // Return a 404 response if a product with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    Product product = optionalProduct.get();
	    productRepo.deleteById(id);

	    return ResponseEntity.ok().build();
	}

}
