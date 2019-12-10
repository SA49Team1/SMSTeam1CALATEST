package sg.edu.nus.sms.service;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;

public interface StudentCourseService {
	
	List<StudentCourse> findAllByCourse(Course cou);

	ArrayList<StudentCourse> findAllByStudent(Students stu);

	StudentCourse findByCourseAndStudent(Course course, Students students);

	List<StudentCourse> findAllByStatus(String status);
	
	StudentCourse findById(int id);

	void save(StudentCourse stucou);

	void deleteAll(List<StudentCourse> stucoulist);

	List<StudentCourse> findAll();

}
