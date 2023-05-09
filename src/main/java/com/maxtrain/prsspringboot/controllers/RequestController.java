package com.maxtrain.prsspringboot.controllers;

import java.time.LocalDateTime;
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

import com.maxtrain.prsspringboot.entities.Request;
import com.maxtrain.prsspringboot.entities.User;
import com.maxtrain.prsspringboot.repositories.RequestRepository;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/requests")
public class RequestController {

	private final String NEW = "New";
	private final String REVIEW = "Review";
	private final String APPROVED = "Approved";
	private final String REJECTED = "Rejected";
	private final String REOPENED = "Reopened";
	
	@Autowired
	private RequestRepository requestRepo;
	
	// method handles HTTP GET requests to the URL "/requests" and returns a list of all requests
	@GetMapping("")
	public List<Request> getAll(){
		List<Request> requests = requestRepo.findAll();
		
		return requests;
	}
	
	// method handles HTTP GET requests to the URL "/requests/{id}" and returns the request with the specified ID
	@GetMapping("/{id}")
	public ResponseEntity<Request> getById(@PathVariable int id) {
	    Optional<Request> optionalRequest = requestRepo.findById(id);

	    if (!optionalRequest.isPresent()) {
	        // Return a 404 response if a request with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    Request request = optionalRequest.get();

	    return ResponseEntity.ok().body(request);
	}
	
	// method handles HTTP POST requests to the base URL ("/requests") and creates a new request using the request body
	@PostMapping("")
	public Request create(@RequestBody Request newRequest) {
		Request request = requestRepo.save(newRequest);
		
		boolean requestExists =  requestRepo.existsById(newRequest.getId());
		
		if(!requestExists) { 
			request.setStatus(NEW);
			request.setDeliveryMode("Pickup");
			request.setSubmittedDate(LocalDateTime.now());
			// save the new request to the repository
			request = requestRepo.save(newRequest); 
		}
		
		return request;
	}
	
	// method handles HTTP PUT requests to the base URL ("/products") and updates an existing product using the request body
	@PutMapping("")
	public ResponseEntity<Request> updateRequest(@RequestBody Request updatedRequest) {
	    // Check if the specified user exists
	    Optional<Request> optionalRequest = requestRepo.findById(updatedRequest.getId());

	    if (!optionalRequest.isPresent()) {
	    	return ResponseEntity.notFound().build();
	    } 
	    
	    Request request = requestRepo.save(updatedRequest);
	    return ResponseEntity.ok(request);
	}
	
	// method handles HTTP DELETE requests to the URL "/requests/{id}" and deletes the request with the specified ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable int id) {
	    Optional<Request> optionalRequest = requestRepo.findById(id);

	    if (!optionalRequest.isPresent()) {
	        // Return a 404 response if a request with the specified ID was not found
	        return ResponseEntity.notFound().build();
	    }

	    Request request = optionalRequest.get();
	    requestRepo.deleteById(id);

	    return ResponseEntity.ok().build();
	}
	
	// method handles HTTP GET requests to "/list-review/{userId}", returns a list of Requests
	@GetMapping("/list-review/{userId}")
	public List<Request> getAllForReview(@PathVariable int userId) {
		// Retrieve requests with status other than "REVIEW" and userId not equal to given id
		List<Request> requests = requestRepo.findByStatusAndUserIdNot(REVIEW, userId); 
		
		return requests;
	}
	
	@PutMapping("/approve")  
	public Request approve(@RequestBody Request approvedRequest) {
		Request request = new Request();
		
		// Check if the request with the ID of the input Request object exists in the repository
		boolean requestExists = requestRepo.existsById(approvedRequest.getId()); 
		
		if(requestExists) {
			// update the status to "APPROVED"
			approvedRequest.setStatus(APPROVED); 
			// Save the updated Request object to the repository
			request = requestRepo.save(approvedRequest); 
		}
		
		return request;
		
	}
	
	@PutMapping("/reject")
	public Request reject(@RequestBody Request rejectedRequest) {
		Request request = new Request();
		
		// Check if the request with the ID of the input Request object exists in the repository
		boolean requestExists = requestRepo.existsById(rejectedRequest.getId()); 
		
		if(requestExists) {
			// update the status to "REJECTED"
			rejectedRequest.setStatus(REJECTED); 
			
			// Save the updated Request object to the repository
			request = requestRepo.save(rejectedRequest); 
		}
		
		return request;
	}
	
	@PutMapping("/re-open")
	public Request reopen(@RequestBody Request reopenedRequest) {
		Request request = new Request();
		
		// Check if the request with the ID of the input Request object exists in the repository
		boolean requestExists = requestRepo.existsById(reopenedRequest.getId()); 
		
		if(requestExists) {
			// update the status to "REOPENED"
			reopenedRequest.setStatus(REOPENED); 
			
			// Save the updated Request object to the repository
			request = requestRepo.save(reopenedRequest); 
		}
		
		return request;
	}
	
	@PutMapping("/submit-for-review")
	public Request submitForReview(@RequestBody Request reviewRequest) {
	    Request request = new Request();
	    
	    // Check if the request with the ID of the input Request object exists in the repository
	    boolean requestExists = requestRepo.existsById(reviewRequest.getId()); 
	    
	    if (requestExists) {
	        request = requestRepo.findById(reviewRequest.getId()).get();
	        if (request.getTotal() <= 50) {
	        	// update the status to "APPROVED"
	            request.setStatus(APPROVED);
	            // set the current date/time
	            request.setSubmittedDate(LocalDateTime.now()); 
	        } else {
	        	// update the status to "REVIEW"
	            request.setStatus(REVIEW); 
	            // set the current date/time
	            request.setSubmittedDate(LocalDateTime.now()); 
	        }
	        // save the updated Request object to the repository
	        requestRepo.save(request); 
	    }
	    return request;
	}		

}

