package sg.edu.nus.sms.service;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Faculty;

public interface CourseService {

	public Course findByCourseCode(String courseCode);

	

	public ArrayList<Course> findAllByCurrentFaculty(Faculty fac);



	public Course findById(Integer id);



	public int count();



	public List<Course> findAll();



	public void save(Course cou);



	public void delete(Course cou);


	
}
