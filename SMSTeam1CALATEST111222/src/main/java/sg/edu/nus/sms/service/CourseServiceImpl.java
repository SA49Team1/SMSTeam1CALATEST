package sg.edu.nus.sms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.repo.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {
	
	private CourseRepository courepo;
	
	@Autowired
	public void setCourepo(CourseRepository courepo) {
		this.courepo = courepo;
	}

	@Override
	public Course findByCourseCode(String courseCode) {
		// TODO Auto-generated method stub
		return courepo.findByCourseCode(courseCode);
	}

	@Override
	public ArrayList<Course> findAllByCurrentFaculty(Faculty fac) {
		// TODO Auto-generated method stub
		return courepo.findAllByCurrentFaculty(fac);
	}

	@Override
	public Course findById(Integer id) {
		// TODO Auto-generated method stub
		return courepo.findById(id).get();
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return (int) courepo.count();
	}

	@Override
	public List<Course> findAll() {
		// TODO Auto-generated method stub
		return courepo.findAll();
	}

	@Override
	public void save(Course cou) {
		// TODO Auto-generated method stub
		courepo.save(cou);
	}

	@Override
	public void delete(Course cou) {
		// TODO Auto-generated method stub
		courepo.delete(cou);
	}

}
