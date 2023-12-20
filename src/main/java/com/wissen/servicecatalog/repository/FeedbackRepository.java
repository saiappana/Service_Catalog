package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

	Feedback findByFeedbackName(String feedbackName);

	Feedback findByFeedbackId(Integer id);

	List<Feedback> findByFlag(boolean flag);

	Feedback findByFeedbackNameAndFlag(String feedbackName, boolean flag);
	
	Feedback findByFeedbackIdAndFlag(Integer id, boolean flag);
}
