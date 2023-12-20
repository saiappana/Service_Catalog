package com.wissen.servicecatalog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectActivity {

	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer projectActivityId;
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project projectId;
    @ManyToOne
    @JoinColumn(name="activity_id")
	private Activity activityId;
    private String serviceApplicabilityYN;
    
}
