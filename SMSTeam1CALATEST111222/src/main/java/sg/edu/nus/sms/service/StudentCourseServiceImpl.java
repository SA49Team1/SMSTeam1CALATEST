package sg.edu.nus.sms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.repo.StudentCourseRepository;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {
	
	
	private StudentCourseRepository stucourepo;
	

	@Autowired
	public void setStucourepo(StudentCourseRepository stucourepo) {
		this.stucourepo = stucourepo;
	}

	@Override
	public List<StudentCourse> findAllByCourse(Course cou) {
		// TODO Auto-generated method stub
		return stucourepo.findAllByCourse(cou);
	}

	@Override
	public ArrayList<StudentCourse> findAllByStudent(Students stu) {
		// TODO Auto-generated method stub
		return stucourepo.findAllByStudent(stu);
	}

	@Override
	public StudentCourse findByCourseAndStudent(Course course, Students students) {
		// TODO Auto-generated method stub
		return stucourepo.findByCourseAndStudent(course, students);
	}

	@Override
	public List<StudentCourse> findAllByStatus(String status) {
		// TODO Auto-generated method stub
		return stucourepo.findAllByStatus(status);
	}

	@Override
	public StudentCourse findById(int id) {
		// TODO Auto-generated method stub
		return stucourepo.findById(id).get();
	}


	@Override
	public void save(StudentCourse stucou) {
		// TODO Auto-generated method stub
		stucourepo.save(stucou);
	}

	@Override
	public void deleteAll(List<StudentCourse> stucoulist) {
		// TODO Auto-generated method stub
		stucourepo.deleteAll(stucoulist);
	}

	@Override
	public List<StudentCourse> findAll() {
		// TODO Auto-generated method stub
		return stucourepo.findAll();
	}
	
}
