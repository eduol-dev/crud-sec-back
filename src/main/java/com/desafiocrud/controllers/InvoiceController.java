package com.desafiocrud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafiocrud.entities.Invoice;
import com.desafiocrud.repositories.InvoiceRepository;

@RestController
@RequestMapping("invoce")
public class InvoiceController {
	
	@Autowired
	private InvoiceRepository invoiceRepository;

	@RequestMapping("/")
	ResponseEntity<List<Invoice>> myInvoicers() {
		return ResponseEntity.ok(invoiceRepository.findAll());
	}
}
