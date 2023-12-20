package com.wissen.servicecatalog.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="skill_id")
	private Integer skillId;
	
	@NotNull(message = "Please select the skill level")
	@Column(name="skill_level")
	@NotNull(message="Please enter the skill level")
	private String skillLevel;
	@JsonIgnore
	private boolean flag;
	
//	@OneToMany()
//	private List<Activity> activities=new ArrayList<>():
}
