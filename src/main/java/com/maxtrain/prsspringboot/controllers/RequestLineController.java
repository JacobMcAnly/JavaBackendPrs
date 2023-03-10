package com.maxtrain.prsspringboot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.maxtrain.prsspringboot.repositories.RequestLineRepository;
import com.maxtrain.prsspringboot.repositories.RequestRepository;


@RestController
@RequestMapping("/request-lines")
public class RequestLineController {
	
	@Autowired
	private RequestRepository requestRepo;   

	@Autowired
	private RequestLineRepository requestLineRepo;

	@GetMapping("")
	public List<RequestLine> getAll() {
		List<RequestLine> requestLines = requestLineRepo.findAll();

		return requestLines;
	}

	@GetMapping("/{id}")
	public RequestLine getById(@PathVariable int id) {
		RequestLine requestLine = new RequestLine();
		Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(id);

		if (optionalRequestLine.isPresent()) {
			requestLine = optionalRequestLine.get();
		}

		return requestLine;
	}

	@PostMapping("")
	public RequestLine create(@RequestBody RequestLine newRequestLine) {
		RequestLine requestLine = new RequestLine();

		boolean requestLineExists = requestLineRepo.findById(newRequestLine.getId()).isPresent();

		if (!requestLineExists) {
			requestLine = requestLineRepo.save(newRequestLine);
			recalculateTotal(requestLine.getRequest());
		}

		return requestLine;
	}

	@PutMapping("")
	public RequestLine update(@RequestBody RequestLine updatedRequestLine) {
		RequestLine requestLine = new RequestLine();

		boolean requestLineExists = requestLineRepo.findById(updatedRequestLine.getId()).isPresent();

		if (requestLineExists) {
			requestLine = requestLineRepo.save(updatedRequestLine);
			recalculateTotal(requestLine.getRequest());
		}

		return requestLine;
	}

	@DeleteMapping("/{id}")
	public RequestLine delete(@PathVariable int id) {
		RequestLine requestLine = new RequestLine();
		Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(id);

		boolean requestLineExists = optionalRequestLine.isPresent();

		if (requestLineExists) {
			requestLine = optionalRequestLine.get();
			requestLineRepo.deleteById(id);
			recalculateTotal(requestLine.getRequest());
		}

		return requestLine;
	}

	@GetMapping("/lines-for-request/{requestId}") // Need to return null???
	public List<RequestLine> getAllRequestLines(@PathVariable int requestId) {
		List<RequestLine> requestLines = requestLineRepo.findByRequestId(requestId);

		return requestLines;
	}

	private void recalculateTotal(Request request) {
		List<RequestLine> requestLines = requestLineRepo.findByRequestId(request.getId());
		double runningTotal = 0;

		for (RequestLine lineItem : requestLines) {
			double total = lineItem.getProduct().getPrice() * lineItem.getQuantity();
			runningTotal += total;
		}
		request.setTotal(runningTotal);

		requestRepo.save(request);
	}

}
