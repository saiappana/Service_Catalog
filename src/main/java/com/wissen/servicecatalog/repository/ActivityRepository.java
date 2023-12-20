package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
	@Query("From Activity where tower.towerId=:towerId")
	List<Activity> getActivity(Integer towerId);

	Activity findByActivityId(Integer activityId);

	Activity findByActivityName(String activityName);

	@Query("From Activity where category=:category AND service=:service AND technologies=:technologies AND activityName=:activityName AND skill.skillLevel=:skillLevel AND facilitator=:facilitator AND tower.towerName=:towerName AND skill.skillLevel=:skill")
	Activity findByDuplicates(String category,String service,String technologies,String activityName,String skillLevel,String facilitator,String towerName,String skill);

	@Query("From Activity where tower.towerName=:towerName")
	List<Activity> findByTowerName(String towerName);

	@Query("From Activity a " +
			"where a.activityName=:activityName " +
			"and a.category=:category " +
			"and a.service=:service " +
			"and a.technologies=:technology and a.skill.skillId=:skillId and a.tower.towerId=:towerId")
	Activity findByAllProps(String activityName, String category,
								  String service,
								  String technology, Integer skillId, Integer towerId);

	
}