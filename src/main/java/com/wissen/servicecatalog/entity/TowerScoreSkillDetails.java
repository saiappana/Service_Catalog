package com.wissen.servicecatalog.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TowerScoreSkillDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer towerSkillDetailsId;
    @OneToOne
    @JoinColumn(name="skill_id")
    private Skill skill;
    @OneToOne
    @JoinColumn(name="tower_id")
    private Tower tower;
    private  Integer minimumScore;
    private Integer maximumScore;
    private Integer nextLevelMin;


}
