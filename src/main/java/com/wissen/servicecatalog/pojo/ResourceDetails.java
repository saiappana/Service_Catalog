package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDetails {
	Integer employeeId;
	String employeeName;
	String towerName;
	String skillLevel;
	String projectName;
	String primarySkill;
	String secondarySkill;
	Integer previousQuarter;
	Integer currentQuarter;
	String quarterComparsion;
	String feedbackName;
	Integer minScore;
	Integer maxScore;
	String employeeStatus;
	String employeeActive;
	String projectManagerName;
	String roadMap;
	String timeLine;
	String quarterYear;
	String quarter;
	Integer year;
	
	public ResourceDetails(Integer employeeId, String employeeName, String primarySkill, String secondarySkill, 
			Integer year, String quarter, String projectName, String projectManagerName, String towerName, 
			String skillLevel, String feedbackName, String employeeStatus, Integer minScore, 
			Integer maxScore, Integer currentQuarter,String roadMap,String employeeActive) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.towerName = towerName;
		this.skillLevel = skillLevel;
		this.projectName = projectName;
		this.primarySkill = primarySkill;
		this.secondarySkill = secondarySkill;
		this.currentQuarter = currentQuarter;
		this.feedbackName = feedbackName;
		this.employeeStatus = employeeStatus;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.projectManagerName = projectManagerName;
		this.quarter = quarter;
		this.year = year;
		this.roadMap=roadMap;
		this.employeeActive=employeeActive;
	}
}
