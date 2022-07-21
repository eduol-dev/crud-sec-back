package com.crudsec.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.crudsec.dtos.InvoiceDTO;
import com.crudsec.entities.Invoice;

public interface InvoiceService {
	
	Optional<List<Invoice>> listByUser() throws UserPrincipalNotFoundException;

	Optional<Invoice> findById(UUID id) throws UserPrincipalNotFoundException;
	
	Invoice newInvoice(InvoiceDTO invoiceDTO) throws UsernameNotFoundException, UserPrincipalNotFoundException;

}
