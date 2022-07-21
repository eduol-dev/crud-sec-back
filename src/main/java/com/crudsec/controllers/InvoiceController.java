package com.crudsec.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudsec.dtos.InvoiceDTO;
import com.crudsec.entities.Invoice;
import com.crudsec.services.InvoiceService;

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
		if (inv.isPresent()) {
			return ResponseEntity.ok(inv.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping("/{id}")
	ResponseEntity<Invoice> myInvoiceById(@PathVariable UUID id) {

		Optional<Invoice> inv = Optional.empty();
		try {
			inv = invoiceService.findById(id);
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (inv.isPresent()) {
			return ResponseEntity.ok(inv.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/new")
	ResponseEntity<Invoice> newInvoice(@Valid InvoiceDTO invoice) {

		try {
			Invoice inv = invoiceService.newInvoice(invoice);
			return ResponseEntity.ok(inv);
		} catch (UsernameNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

}
