package sg.edu.nus.sms.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.repo.StudentsRepository;


@Service
public class StudentServiceImpl implements StudentService {

	private StudentsRepository sturepo;
	
	@Autowired
	public void setStudentsRepository(StudentsRepository sturepo )
	{
		this.sturepo=sturepo;
		
	}
	
	
	
	@Override
	public Students findByStudentID(int studentID) {
		// TODO Auto-generated method stub
		return sturepo.findByStudentID(studentID);
	}

	@Override
	public Students findByUserName(String username) {
		// TODO Auto-generated method stub
		return sturepo.findByUserName(username);
	}

	@Override
	public boolean existsByUserName(String userName) {
		// TODO Auto-generated method stub
		return sturepo.existsByUserName(userName);
	}

	@Override
	public void saveStudents(Students stu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteStudents(Students stu) {
		// TODO Auto-generated method stub

	}

	@Override
	public Students findById(int id) {
		// TODO Auto-generated method stub
		return sturepo.findById(id).get();
	}



	@Override
	public int count() {
		// TODO Auto-generated method stub
		return (int) sturepo.count();
	}



	@Override
	public List<Students> findAll() {
		// TODO Auto-generated method stub
		return sturepo.findAll();
	}



	@Override
	public void save(@Valid Students stu) {
		// TODO Auto-generated method stub
		sturepo.save(stu);
	}



	@Override
	public void delete(Students stu) {
		// TODO Auto-generated method stub
		sturepo.delete(stu);
	}

}
