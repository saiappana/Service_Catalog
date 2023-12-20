package com.wissen.servicecatalog.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Activity
{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer activityId;
    @Column(name="activity_name",length = 1000)
    private String activityName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="towerId")
    private Tower tower;
    private String category;
    private String service;
    private String facilitator;
    private String technologies;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="skillId")
    private Skill skill;
    
}
