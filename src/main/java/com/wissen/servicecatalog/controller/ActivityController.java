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

import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.service.impl.ActivityServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/service-catalog/activity")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityController {

	Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	ActivityServiceImpl activityService;

	@GetMapping("/get/{towerId}")
	public List<ActivityResponse> getTowerId(@PathVariable Integer towerId) throws TowerException {
		logger.info("getting the Tower Id from Activity Controller");
		return activityService.getTowerId(towerId);
	}

	@PostMapping("/add")
	public List<ActivityResponse> addActivity(@Valid @RequestBody List<ActivityRequest> activity)
			throws TowerException {
		logger.info("Adding the Activity from Activity Controller");
		return activityService.addActivity(activity);
	}

	@PutMapping("/update")
	public List<ActivityResponse> updateActivity(@RequestBody @Valid List<ActivityRequest> activity)
			throws ActivityException, TowerException {
		logger.info("Updating the Activity from Activity Controller");
		return activityService.updateActivity(activity);
	}
}
