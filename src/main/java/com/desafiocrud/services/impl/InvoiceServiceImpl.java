package com.desafiocrud.services.impl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desafiocrud.dtos.InvoiceDTO;
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
	public Optional<Invoice> findById(UUID id) throws UserPrincipalNotFoundException {
		return invoiceRepository.findByIdAndUserTo(id, getUserLogged());
	}

	@Override
	public Optional<List<Invoice>> listByUser() throws UserPrincipalNotFoundException {
		return invoiceRepository.findByUserTo(getUserLogged());
	}

	@Override
	public Invoice newInvoice(InvoiceDTO invoiceDTO) throws UsernameNotFoundException, UserPrincipalNotFoundException {
		Optional<User> userTo = userRepository.findByUserName(invoiceDTO.getUserTo());
		if(userTo.isEmpty()) {
			throw new UsernameNotFoundException(String.format("Username %s not found.", invoiceDTO.getUserTo()));
		}
		
		Invoice invoice = new Invoice();
		invoice.setAmount(invoiceDTO.getAmount());
		invoice.setCreateAt(LocalDateTime.now());
		invoice.setUserFrom(getUserLogged());
		invoice.setUserTo(userTo.get());
		return invoiceRepository.save(invoice);
	}

	private User getUserLogged() throws UserPrincipalNotFoundException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> user = userRepository.findByUserName(userName);
		if (user.isEmpty()) {
			throw new UserPrincipalNotFoundException(userName);
		}		
		return user.get();
	}

}
