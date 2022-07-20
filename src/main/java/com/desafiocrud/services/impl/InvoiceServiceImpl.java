package com.desafiocrud.services.impl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafiocrud.entities.Invoice;
import com.desafiocrud.entities.User;
import com.desafiocrud.repositories.InvoiceRepository;
import com.desafiocrud.repositories.UserRepository;
import com.desafiocrud.services.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<Invoice> findByIdAndUser(UUID id, String userName) throws UserPrincipalNotFoundException {

		Optional<User> user = userRepository.findByUserName(userName);

		if (user.isEmpty()) {
			throw new UserPrincipalNotFoundException(userName);
		}
		
		return invoiceRepository.findByIdAndUserTo(id, user.get());
	}

}
