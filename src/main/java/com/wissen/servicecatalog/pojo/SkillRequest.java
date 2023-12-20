package com.wissen.servicecatalog.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SkillRequest {

	private Integer skillId;

	@NotNull(message = "Please select the skill level")
	@NotBlank(message = "Please enter the skill level")
	private String skillLevel;
}
