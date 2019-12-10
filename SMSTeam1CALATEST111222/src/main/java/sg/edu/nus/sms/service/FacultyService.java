package sg.edu.nus.sms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import sg.edu.nus.sms.model.Faculty;


public interface FacultyService {
	
	public Faculty findByFacultyID(int facultyID);

	public Faculty findByFirstName(String string);

	public ArrayList<Faculty> findByDepartment(String department);

	public Object findByUserName(String username);
	
	public Faculty findById(int id);

	public int count();

	public List<Faculty> findAll();

	public void delete(Faculty fac);

	public void save(@Valid Faculty fac);



}
