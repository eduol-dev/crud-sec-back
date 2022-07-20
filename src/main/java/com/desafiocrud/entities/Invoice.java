package com.desafiocrud.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "invoicers")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "invo_id")
	private UUID id;
		
	@ManyToOne()
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@NotNull(message = "Please inform the sender user.")
	private User userFrom;
	
	@ManyToOne()
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@NotNull(message = "Please inform the recipient user.")
	private User userTo;
		
	@Column(name = "invo_amount")
	@NotNull(message = "Please inform the amount.")
	private BigDecimal amount;
	
	@Column(name = "invo_createAt")
	@NotNull(message = "Date of invoicer cannot be null.")
	private LocalDateTime createAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public User getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}

	public User getUserTo() {
		return userTo;
	}

	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}
	
	
}
