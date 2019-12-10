package sg.edu.nus.sms.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;

public interface LeaveAppService {
	
	public LeaveApp findByStartDateAndEndDate(Date startDate, Date endDate);

	public List<LeaveApp> findAllByFaculty(Faculty fac);

	public List<LeaveApp> findAllByStatus(String string);
	
	public LeaveApp findById(int id);

	public void deleteLeave(LeaveApp leaapp);

	public void save(@Valid LeaveApp lea);

	public List<LeaveApp> findAll();



}
