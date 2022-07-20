package com.desafiocrud.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafiocrud.entities.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

	
	
}
