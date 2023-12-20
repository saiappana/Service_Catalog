package com.wissen.servicecatalog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Setting {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer settingId;
	@Column(unique = true)
	@NotNull(message = "Please enter the setting name")
	String name;
	@NotNull(message = "Please enter the setting description")
	String description;
	@NotNull(message="Please enter the data")
	String data;
	
	
}

