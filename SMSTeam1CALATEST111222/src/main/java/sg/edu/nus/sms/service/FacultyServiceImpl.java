package sg.edu.nus.sms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.repo.FacultyRepository;
import sg.edu.nus.sms.repo.LeaveAppRepository;

@Service
public class FacultyServiceImpl implements FacultyService {

	
	private FacultyRepository facrepo;
	
	private LeaveAppRepository learepo;
	
	@Autowired
	public void setLearepo(LeaveAppRepository learepo)
	{
		this.learepo=learepo;
	}
	
	@Autowired
	public void setFacrepo(FacultyRepository facrepo) {
		this.facrepo = facrepo;
	}

	@Override
	public Faculty findByFacultyID(int facultyID) {
		// TODO Auto-generated method stub
		return facrepo.findByFacultyID(facultyID);
	}

	@Override
	public Faculty findByFirstName(String string) {
		// TODO Auto-generated method stub
		return facrepo.findByFirstName(string);
	}

	@Override
	public ArrayList<Faculty> findByDepartment(String department) {
		// TODO Auto-generated method stub
		return facrepo.findByDepartment(department);
	}

	@Override
	public Object findByUserName(String username) {
		// TODO Auto-generated method stub
		return facrepo.findByUserName(username);
	}



	@Override
	public Faculty findById(int id) {
		// TODO Auto-generated method stub
		return facrepo.findById(id).get();
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return (int) facrepo.count();
	}

	@Override
	public List<Faculty> findAll() {
		// TODO Auto-generated method stub
		return facrepo.findAll();
	}

	@Override
	public void delete(Faculty fac) {
		// TODO Auto-generated method stub
		facrepo.delete(fac);
	}

	@Override
	public void save(@Valid Faculty fac) {
		// TODO Auto-generated method stub
	facrepo.save(fac);	
	}

}
