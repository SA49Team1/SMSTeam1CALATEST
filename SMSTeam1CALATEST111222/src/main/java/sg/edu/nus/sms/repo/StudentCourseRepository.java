package sg.edu.nus.sms.repo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;


public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer>{

	List<StudentCourse> findAllByCourse(Course cou);
	
	ArrayList<StudentCourse> findAllByStudent(Students stu);
	
	StudentCourse findByCourseAndStudent(Course course, Students students);

	List<StudentCourse> findAllByStatus(String status);
	
	StudentCourse findAllById(Integer id);
	

	StudentCourse findByStudentId(int id);
	
	ArrayList<StudentCourse> findAllByCourseId(Integer id);
	
	//@Query("select u from User u where u.firstnamelike %?1") List<User> findByFirstnameEndsWith(String firstname);
	/*
	@Query("select s from StudentCourse s where s.getCourse().getId() like %?1 and s.getStudent().getId() like %?2")
    StudentCourse findByCourseId(int course_id, int student_id);
	
	@Query("Select s from StudentCourse s where s.getCourse().getId()==course_id and s.getStudent().getId()==student_id")
	public StudentCourse findByCourseId(@Param("course_id")int course_id,@Param("student_id") int student_id);
	*/
}
