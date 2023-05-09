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

import com.maxtrain.prsspringboot.entities.Request;
import com.maxtrain.prsspringboot.entities.RequestLine;
import com.maxtrain.prsspringboot.entities.User;
import com.maxtrain.prsspringboot.repositories.RequestLineRepository;
import com.maxtrain.prsspringboot.repositories.RequestRepository;


@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/request-lines")
public class RequestLineController {
	
	@Autowired
	private RequestRepository requestRepo;   

	@Autowired
	private RequestLineRepository requestLineRepo;

	// method handles HTTP GET requests to the URL "/request-lines" and returns a list of all request lines
	@GetMapping("")
	public List<RequestLine> getAll() {
		List<RequestLine> requestLines = requestLineRepo.findAll();

		return requestLines;
	}

	// method handles HTTP GET requests to the URL "/request-lines/{id}" and returns the request-line with the specified ID
	@GetMapping("/{id}")
	public RequestLine getById(@PathVariable int id) {
		RequestLine requestLine = new RequestLine();
		// retrieve the RequestLine object with the specified ID from the RequestLineRepository
		Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(id);

		if (optionalRequestLine.isPresent()) {
			// Set the RequestLine object to the retrieved request line
			requestLine = optionalRequestLine.get();
		}

		return requestLine;
	}

	// method handles the HTTP POST request to the base URL ("/request-lines") and creates a new request line using the request body
	@PostMapping("")
	public RequestLine create(@RequestBody RequestLine newRequestLine) {
		RequestLine requestLine = new RequestLine();
		
		// Check if a request line with the same ID already exists
		boolean requestLineExists = requestLineRepo.findById(newRequestLine.getId()).isPresent();

		if (!requestLineExists) {
			// Save the new request line to the repository
			requestLine = requestLineRepo.save(newRequestLine);
			recalculateTotal(requestLine.getRequest());
		}

		return requestLine;
	}

	// method handles HTTP PUT requests to the base URL ("/request-lines") and updates an existing request line using the request body
	@PutMapping("")
	public ResponseEntity<RequestLine> updateRequestLine(@RequestBody RequestLine updatedRequestLine) {
	    // Check if the specified user exists
	    Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(updatedRequestLine.getId());

	    if (!optionalRequestLine.isPresent()) {
	    	return ResponseEntity.notFound().build();
	    } 
	    
	    RequestLine requestLine = requestLineRepo.save(updatedRequestLine);
	    return ResponseEntity.ok(requestLine);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
	    Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(id);

	    if (!optionalRequestLine.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }

	    RequestLine requestLine = optionalRequestLine.get();
	    requestLineRepo.deleteById(id);
	    recalculateTotal(requestLine.getRequest());

	    return ResponseEntity.ok(requestLine);
	}

	@GetMapping("/lines-for-request/{requestId}")
	public List<RequestLine> getAllRequestLines(@PathVariable int requestId) {
		// Retrieve all RequestLine objects associated with the given requestId from the requestLineRepo
		List<RequestLine> requestLines = requestLineRepo.findByRequestId(requestId); 

		return requestLines;
	}

	private void recalculateTotal(Request request) {
		 // retrieve all RequestLine objects related to the given Request
		List<RequestLine> requestLines = requestLineRepo.findByRequestId(request.getId());
		
		 // initialize a variable to hold the running total
		double runningTotal = 0;

		 // iterate over each RequestLine and calculate the total cost
		for (RequestLine lineItem : requestLines) {
			// calculate the total cost of the line item
			double total = lineItem.getProduct().getPrice() * lineItem.getQuantity();
			// add the total cost to the running total
			runningTotal += total;
		}
		// set the Request's Total property to the running total
		request.setTotal(runningTotal);

		// Save the updated Request object to the repository
		requestRepo.save(request);
	}

}
