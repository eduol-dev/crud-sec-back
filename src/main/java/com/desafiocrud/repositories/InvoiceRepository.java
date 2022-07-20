package com.desafiocrud.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.desafiocrud.entities.Invoice;
import com.desafiocrud.entities.User;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
	
	Optional<Invoice> findByIdAndUserTo(UUID id, User userTo);	
	
	@Query("SELECT i FROM Invoice i WHERE i.userTo = ?1")
	Optional<List<Invoice>> findByUserTo(User userTo);
	
}
