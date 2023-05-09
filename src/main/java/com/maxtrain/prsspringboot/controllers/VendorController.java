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

import com.maxtrain.prsspringboot.entities.User;
import com.maxtrain.prsspringboot.entities.Vendor;
import com.maxtrain.prsspringboot.repositories.VendorRepository;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/vendors")
public class VendorController {

		@Autowired
		private VendorRepository vendorRepo;
		
		@GetMapping("")
		public List<Vendor> getAll() {
			List<Vendor> vendors = vendorRepo.findAll();
			
			return vendors;
		}
		
		@GetMapping("/{id}")
		public ResponseEntity<Vendor> getById(@PathVariable int id) {
		    Optional<Vendor> optionalVendor = vendorRepo.findById(id);

		    if (!optionalVendor.isPresent()) {
		        // Return a 404 response if a vendor with the specified ID was not found
		        return ResponseEntity.notFound().build();
		    }

		    Vendor vendor = optionalVendor.get();
		    return ResponseEntity.ok(vendor);
		}

		@PostMapping("")
		public Vendor create(@RequestBody Vendor newVendor) {
			Vendor vendor = new Vendor();
			
			boolean vendorExists = vendorRepo.findById(newVendor.getId()).isPresent();
			
			if(!vendorExists) {
				vendor = vendorRepo.save(newVendor);
			}
			
			return vendor;
		}
		
	@PutMapping("")
	public ResponseEntity<Vendor> updateVendor(@RequestBody Vendor updatedVendor) {
	    // Check if the specified user exists
	    Optional<Vendor> optionalVendor = vendorRepo.findById(updatedVendor.getId());

	    if (!optionalVendor.isPresent()) {
	    	return ResponseEntity.notFound().build();
	    } 
	    
	    Vendor vendor = vendorRepo.save(updatedVendor);
	    return ResponseEntity.ok(vendor);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable int id) {
	    Optional<Vendor> optionalVendor = vendorRepo.findById(id);

	    if (!optionalVendor.isPresent()) {
	        // Return a 404 response if a vendor with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    Vendor vendor = optionalVendor.get();
	    vendorRepo.deleteById(id);

	    return ResponseEntity.ok().build();
	}
}
