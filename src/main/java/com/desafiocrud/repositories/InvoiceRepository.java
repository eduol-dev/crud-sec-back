package com.desafiocrud.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafiocrud.entities.Invoice;
import com.desafiocrud.entities.User;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

	
	Optional<Invoice> findByIdAndUserTo(UUID id, User userTo);
	
	
}
