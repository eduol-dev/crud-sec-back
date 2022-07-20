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
		Optional<List<Invoice>> inv = Optional.empty();
		try {
			inv = invoiceService.listByUser();
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(inv.isPresent()) {
			return ResponseEntity.ok(inv.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping("/{id}")
	ResponseEntity<Invoice> myInvoicers(@PathVariable UUID id) {
		
		Optional<Invoice> inv = Optional.empty();
		try {
			inv = invoiceService.findById(id);
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(inv.isPresent()) {
			return ResponseEntity.ok(inv.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
