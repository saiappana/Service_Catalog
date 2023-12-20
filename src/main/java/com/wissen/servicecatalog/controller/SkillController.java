package com.wissen.servicecatalog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.pojo.SkillRequest;
import com.wissen.servicecatalog.service.impl.SkillServiceImpl;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) 
@RequestMapping("/service-catalog/skill")
public class SkillController {

	Logger logger = LoggerFactory.getLogger(SkillController.class);

	@Autowired
	SkillServiceImpl skillService;

	@PostMapping("/add")
	public Skill addSkill(@RequestBody @Valid SkillRequest skill) {
		logger.info("Adding skill from Skill Controller");
		return skillService.addSkill(skill);
	}

	@GetMapping("/get/{skillId}")
	public Skill getSkill(@PathVariable Integer skillId) {
		logger.info("Getting skill by skill Id from Skill Controller");
		return skillService.getSkill(skillId);
	}

	@PutMapping("/update/{skillId}/{skillLevel}")
	public Skill updateSkill(@PathVariable Integer skillId, @PathVariable String skillLevel) {
		logger.info("Updating skill by skill Id and skill Level from Skill Controller");
		return skillService.updateSkill(skillId, skillLevel);
	}

	@GetMapping("/getAll")
	public List<Skill> getAllSkill() {
		logger.info("Getting all skills from Skill Controller");
		return skillService.getAllSkill();
	}
	
	@PostMapping("/delete/{skillId}")
	public String deleteSkill(@PathVariable Integer skillId)
	{
		return skillService.deleteSkill(skillId);
	}
}
 