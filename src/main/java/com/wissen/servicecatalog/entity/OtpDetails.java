package com.wissen.servicecatalog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otp_id")
	private Integer otpId;

	@Column(name = "otp_number")
	private String otpNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "valid_from")
	private LocalDateTime validFrom;

	@Column(name = "valid_upto")
	private LocalDateTime validUpto;
	@Column(name = "attempt")
	private Integer attempt;


	
}
