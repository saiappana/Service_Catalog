package com.wissen.servicecatalog.entity;

import com.wissen.servicecatalog.pojo.ResourceDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQuery(query="select s.employee_id employeeid, e.employee_name employeename, e.primary_skill primaryskill, e.employee_status employeeActive,e.secondary_skill secondaryskill, s.year year, s.quarter quarter,s.road_map roadMap, p.project_name projectname, pm.employee_name projectmanagername, t.tower_name towername, sk.skill_level skilllevel, f.feedback_name feedbackname, st.current_employee_status employeestatus, ts.minimum_score minscore, ts.maximum_score maxscore, sum(score) currentquarter "
		+ "from score s, employee_master e, employee_master pm, project p, tower t, tower_score_skill_details ts, skill sk, feedback f, status st "
		+ "where s.status ='Published' and s.employee_id = e.employee_id and s.project_id =p.project_id and p.project_manager_id = pm.employee_id and s.tower_id = t.tower_id and s.tower_id = ts.tower_id and s.skill_id = ts.skill_id and s.skill_id = sk.skill_id and s.feed_back_id= f.feedback_id and s.status_id = st.status_id "
		+ "group by s.employee_id, e.employee_name, e.primary_skill, e.secondary_skill, s.year, s.quarter, p.project_name, pm.employee_name, t.tower_name, sk.skill_level, f.feedback_name, st.current_employee_status, ts.minimum_score, ts.maximum_score "
		+ "order by e.employee_name desc, s.year desc, s.quarter desc, p.project_name desc, t.tower_name desc",
		name = "Score.getAllPublishedScores",
		resultSetMapping = "Mapping.ResourceDetails")
@NamedNativeQuery(query="",name="Score.findByProjectIdAndTowerIdAndYearAndQuarter",resultSetMapping="")

@SqlResultSetMapping(name = "Mapping.ResourceDetails",
classes = @ConstructorResult(targetClass = ResourceDetails.class,
                             columns = {@ColumnResult(name = "employeeId", type = Integer.class),
                            		 @ColumnResult(name = "employeeName", type = String.class),
                            		 @ColumnResult(name = "primarySkill", type = String.class),
                            		 @ColumnResult(name = "secondarySkill", type = String.class),
                            		 @ColumnResult(name = "year", type = Integer.class),
                            		 @ColumnResult(name = "quarter", type = String.class),
                            		 @ColumnResult(name = "projectName", type = String.class),
                            		 @ColumnResult(name = "projectManagerName", type = String.class),
                            		 @ColumnResult(name = "towerName", type = String.class),
                            		 @ColumnResult(name = "skillLevel", type = String.class),
                            		 @ColumnResult(name = "feedbackName", type = String.class),
                            		 @ColumnResult(name = "employeeStatus", type = String.class),
                            		 @ColumnResult(name = "minScore", type = Integer.class),
                            		 @ColumnResult(name = "maxScore", type = Integer.class),
                            		 @ColumnResult(name = "currentQuarter", type = Integer.class),
                            		 @ColumnResult(name = "roadMap", type = String.class),
                            		 @ColumnResult(name = "employeeActive", type = String.class),
                            		 }))

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score
{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scoreId;
    private String quarter;
    private int year;
    private Integer score;
    private String roadMap;
    private String timeLine;

    private String status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tower_id")
    private Tower tower;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
    private EmployeeMaster employeeMaster;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "feedBack_id")
    private Feedback feedbackMaster;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="status_id")
    private Status currentEmployeeStatus;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="skill_id")
    private Skill projectSkill;

}