package com.wissen.servicecatalog.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TowerScoreRequest {


	private Integer towerSkillDetailsId;
    @NotNull(message="Please Select the Tower")
    private Integer towerId;
    @NotBlank(message="Skill level not be empty")
    private String skillLevel;
    @NotNull(message="Please give the minimum score minimum score should be 80% of maximumScore")
    private Integer minimumScore;
    @NotNull(message="Please give the maximum score")
    private Integer maximumScore;
    @NotNull(message="Please give the next level score")
    private Integer nextLevelMin;
}
