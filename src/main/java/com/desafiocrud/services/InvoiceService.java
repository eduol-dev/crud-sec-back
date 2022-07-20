package com.desafiocrud.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.UUID;

import com.desafiocrud.entities.Invoice;

public interface InvoiceService {

	Optional<Invoice> findByIdAndUser(UUID id, String userName) throws UserPrincipalNotFoundException;

}
