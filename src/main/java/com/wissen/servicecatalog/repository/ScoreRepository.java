package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.pojo.ResourceDetails;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {

	@Query("From Score where scoreId=:scoreId")
	Score findByScoreId(Integer scoreId);

	@Query("From Score where employeeMaster.employeeId=:employeeId")
	List<Score> findByEmployeeId(Integer employeeId);

	@Query("From Score where employeeMaster.employeeId=:employeeId and quarter=:quarter and year=:year and status=:status")
	List<Score> findByEmployeeIdAndQuarterAndYearAndStatus(Integer employeeId, String quarter, Integer year,String status);

	@Query("From Score where tower.towerName=:towerName")
	List<Score> findByTowerName(String towerName);

	@Query("From Score where project.projectName=:projectName")
	List<Score> findByProjectName(String projectName);

	@Query("From Score where tower.towerName=:towerName And project.projectName=:projectName")
	List<Score> findByTowerNameAndProject(String towerName, String projectName);

	@Query("From Score where project.projectName=:projectName And employeeMaster.employeeId=:empId")
	Score findByProjectNameAndEmployeeId(String projectName, Integer empId);

	@Query("From Score where tower.towerName=:towerName And project.projectName=:projectName And employeeMaster.employeeId=:employeeId")
	List<Score> findByTowerNameAndProjectAndEmployeeId(String towerName, String projectName, Integer employeeId);

	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And project.projectId=:projectId And year=:year And tower.towerId=:towerId And activity.activityId=:activityId")
	public Score findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndActivityActivityId(Integer employeeId, String quarter,
			Integer projectId,Integer year,Integer towerId,Integer activityId);
	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And project.projectId=:projectId And year=:year And tower.towerId=:towerId And activity.activityId=:activityId And status=:status")
	public Score findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndActivityActivityId(Integer employeeId, String quarter,
			Integer projectId,Integer year,Integer towerId,Integer activityId,String status);
	
	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And project.projectId=:projectId And year=:year And tower.towerId=:towerId")
	public List<Score> findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerId(Integer employeeId, String quarter,
			Integer projectId,Integer year,Integer towerId);


	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And project.projectId=:projectId And year=:year And tower.towerId=:towerId And status=:status")
	public List<Score> findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndStatus(Integer employeeId, String quarter,
			Integer projectId,Integer year,Integer towerId,String status);

	List<Score> findByProjectAndTowerAndEmployeeMaster(Project projectDetails, Tower tower,
			EmployeeMaster employeeMaster);

	Score findByEmployeeMasterAndProject(EmployeeMaster employeeMaster, Project projectDetails);

	@Query("From Score where project.projectId=:projectId And tower.towerId=:towerId And year=:year And quarter=:quarter")
	List<Score> findByProjectIdAndTowerIdAndYearAndQuarter(Integer projectId, Integer towerId,Integer year,String quarter);

	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId And quarter=:quater And year=:year And status=:status")
	List<Score> findByPublisedScore(Integer employeeId, Integer towerId, Integer projectId, String quater, Integer year,
			String status);

	@Query("From Score where activity.activityId=:activityId And tower.towerId=:towerId And project.projectId=:projectId And employeeMaster.employeeId=:employeeId And quarter=:quater And year=:year And status=:status")
	Score findByActivity(Integer activityId, Integer towerId, Integer projectId, Integer employeeId, String quater,
			int year, String status);

	@Query("From Score where activity.activityId=:activityId And tower.towerId=:towerId And project.projectId=:projectId And employeeMaster.employeeId=:employeeId order by year desc, quarter desc" )
	List<Score> findByActivity(Integer activityId, Integer towerId, Integer projectId, Integer employeeId);

	@Query("From Score where project.projectId=:projectId")
	Score findByProjectId(Integer projectId);

	@Query("From Score where project.projectId=:projectId And tower.towerId=:towerId And status=:status")
	List<Score> findByProjectIdAndTowerId(Integer projectId, Integer towerId, String status);

	@Query("From Score where quarter=:quarter And status=:status And employeeMaster.employeeId=:employeeId And year=:year And tower.towerId=:towerId And project.projectId=:projectId")
	List<Score> findByQuarterAndStatusEmployeeIdAndYearTowerAndProject(String quarter, String status,
			Integer employeeId, int year, Integer towerId, Integer projectId);

	@Query("From Score where tower.towerId=:towerId And project.projectId=:projectId And quarter=:quater And year=:year")
	List<Score> findByPublisedScore(Integer towerId, Integer projectId, String quater, Integer year);

	@Query("From Score where quarter=:quarter And year=:year And status=:status")
	List<Score> findByQuarterAndYearAndStatus(String quarter, int year, String status);

	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId And quarter=:quarter And year=:year")
	List<Score> findByEmployeeIdAndTowerAndProject(Integer employeeId, Integer towerId, Integer projectId,
			String quarter,Integer year);

	List<Score> findAllById(Iterable<Integer> scoreIds);

	@Query("From Score where quarter=:currentQuarter AND year=:year")
	List<Score> findByCurrentQuarter(String currentQuarter, int year);

	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId")
	List<Score> findByEmployeeIdAndTowerAndProject(Integer employeeId, Integer towerId,Integer projectId );

	@Query("Select distinct s.year From Score s order by s.year asc")
	List<String> getListYear();

	List<Score> findByStatus( String published);

	List<Score> findByStatusAndEmployeeMasterEmployeeIdAndYearAndQuarter(String published, Integer employeeId,Integer year,String quarter);

	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And activity.activityId=:activityId And project.projectId=:projectId And tower.towerId=:towerId")
	public Score findByEmployeeIdAndQuarterAndActivityId(Integer employeeId, String quarter, Integer activityId,
			Integer projectId,Integer towerId);


	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And activity.activityId=:activityId And project.projectId=:projectId And tower.towerId=:towerId And year=:year")
	public Score findByEmployeeIdAndQuarterAndActivityIdAndYear(Integer employeeId, String quarter, Integer activityId,
			Integer projectId,Integer towerId,Integer year);

	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId And quarter=:quarter")
	List<Score> findByEmployeeMaster(Integer employeeId,  Integer towerId, Integer projectId,String quarter);

	@Query("FROM Score where status=:status AND year=:year AND quarter=:quarter GROUP BY tower.towerId,project.projectId,quarter,year,employeeMaster.employeeId")
	List<Score> groupBytowerIdAndProjectId(String status,Integer year,String quarter);

	@Query("FROM Score where status=:status AND year=:year AND quarter=:quarter AND employeeMaster.employeeId=:employeeId GROUP BY tower.towerId,project.projectId,quarter,year,employeeMaster.employeeId")
	List<Score> groupBytowerIdAndProjectId(String status,Integer year,String quarter,Integer employeeId);

	@Query("FROM Score where employeeMaster.employeeId=:employeeId AND status=:status GROUP BY tower.towerId,project.projectId,quarter,year")
	List<Score> groupBytowerIdAndProjectId(Integer employeeId,String status);

	@Query("FROM Score where status=:status GROUP BY tower.towerId,project.projectId,quarter,year,employeeMaster.employeeId")
	List<Score> groupBytowerIdAndProjectId(String status);
	
	@Query(nativeQuery=true)
	List<ResourceDetails> getAllPublishedScores();
	
//	@Query("From Score where status=Published")
//	List<Score> getAllPublisedScore();
	
	@Query("FROM Score where employeeMaster.employeeId=:employeeId and status=:status GROUP BY tower.towerId,project.projectId,quarter,year,employeeMaster.employeeId")
	List<Score> groupBytowerIdAndProjectId(String status,Integer employeeId);

	List<Score> findByStatusAndEmployeeMasterEmployeeId(String published, Integer employeeId);

	@Query("Select employeeMaster.employeeId, Sum(score) From Score where year=:year AND quarter=:quarter and status=:status GROUP BY tower.towerId,project.projectId,quarter,year,employeeMaster.employeeId")
	List<Integer> resourceDetailsSummedScore(Integer year,String quarter,String status);

	@Query("Select employeeMaster.employeeId From Score where status=:status GROUP BY tower.towerId,project.projectId,employeeMaster.employeeId")
	List<Integer> resourceDetailsSummedScore(String status);

	@Query("Select employeeMaster.employeeId From Score where status=:status And employeeMaster.employeeId=:employeeId GROUP BY tower.towerId,project.projectId,employeeMaster.employeeId")
	List<Integer> resourceDetailsSummedScore(String status,Integer employeeId);

	@Query("Select employeeMaster.employeeId From Score where status=:status And employeeMaster.employeeId=:employeeId And year=:year AND quarter=:quarter GROUP BY tower.towerId,project.projectId,employeeMaster.employeeId")
	List<Integer> resourceDetailsSummedScore(String status,Integer employeeId,Integer year,String quarter);
}
