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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tower {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tower_id")
	private Integer towerId;
	@NotNull(message = "Please enter the tower name")
	@Column(name = "tower_name")
	private String towerName;
}
