package com.desafiocrud.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.desafiocrud.entities.Invoice;

public interface InvoiceService {
	
	Optional<List<Invoice>> listByUser() throws UserPrincipalNotFoundException;

	Optional<Invoice> findById(UUID id) throws UserPrincipalNotFoundException;

}
