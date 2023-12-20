package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.servicecatalog.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

	
	Skill findBySkillLevel(String skillLevel);

	Skill findBySkillId(Integer skillId);

	Skill findBySkillIdAndFlag(Integer skillId, boolean b);

	List<Skill> findByFlag(boolean flag);
}
