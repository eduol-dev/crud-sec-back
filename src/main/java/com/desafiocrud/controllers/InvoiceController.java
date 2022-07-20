package com.desafiocrud.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafiocrud.entities.Invoice;
import com.desafiocrud.services.InvoiceService;

@RestController
@RequestMapping("invoice")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;

	@RequestMapping("/")
	ResponseEntity<List<Invoice>> myInvoicers() {
		return ResponseEntity.ok(new ArrayList<>());
	}
	
	@RequestMapping("/{id}")
	ResponseEntity<Invoice> myInvoicers(@PathVariable UUID id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<Invoice> p = Optional.empty();
		try {
			p = invoiceService.findByIdAndUser(id, auth.getName());
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(p.isPresent()) {
			return ResponseEntity.ok(p.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
