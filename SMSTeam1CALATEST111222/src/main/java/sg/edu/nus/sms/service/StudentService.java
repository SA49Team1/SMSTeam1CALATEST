package sg.edu.nus.sms.service;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.repository.CrudRepository;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Students;

public interface StudentService {

	
	public Students findByStudentID(int studentID);

	public Students findByUserName(String username);

	public boolean existsByUserName(String userName);
	
	public void saveStudents(Students stu);
	
	public void deleteStudents(Students stu);

	public Students findById(int id);

	public int count();

	public List<Students> findAll();

	public void save(@Valid Students stu);

	public void delete(Students stu);

	
	
}
