package sg.edu.nus.sms.controllers;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.CourseRepository;
import sg.edu.nus.sms.repo.StudentCourseRepository;
import sg.edu.nus.sms.repo.StudentsRepository;
import sg.edu.nus.sms.service.CourseService;
import sg.edu.nus.sms.service.CourseServiceImpl;
import sg.edu.nus.sms.service.StudentCourseService;
import sg.edu.nus.sms.service.StudentCourseServiceImpl;
import sg.edu.nus.sms.service.StudentService;
import sg.edu.nus.sms.service.StudentServiceImpl;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/student")
public class StuController {
	
	

	
	private StudentCourseService stucouservice;
	
	@Autowired
	public void setStucouservice(StudentCourseServiceImpl stucouimpl) {
		this.stucouservice = stucouimpl;
	}


	private StudentService stuservice;
	
	@Autowired
	public void setStudentService(StudentServiceImpl stuimpl) {
		this.stuservice = stuimpl;
	}

	@GetMapping("/mygrades")
	public String mygrades(@SessionAttribute UserSession usersession, Model model) {
		
		if(!usersession.getUserType().equals("STU")||usersession==null) return "forward:/home/logout";
		
		
		Students stu=stuservice.findById(usersession.getId());
		List<StudentCourse> stucoulist=stucouservice.findAllByStudent(stu);
		List<StudentCourse> compstucoulist=new ArrayList<StudentCourse>();
		long mygpa=0;
		
		
		for(StudentCourse sc:stucoulist)
		{
			
			if (sc.getStatus().equals("Graded")) 
				{
				compstucoulist.add(sc);
				if(sc.getGrade().equals("A")) mygpa+=5*sc.getCourse().getCourseUnit();
				else if (sc.getGrade().equals("B")) mygpa+=4*sc.getCourse().getCourseUnit();
				else if (sc.getGrade().equals("C")) mygpa+=3*sc.getCourse().getCourseUnit();
				else if (sc.getGrade().equals("D")) mygpa+=2*sc.getCourse().getCourseUnit();
				}
		}
		
		
		model.addAttribute("studentname",stu.toString());
		model.addAttribute("compstucoulist", compstucoulist);
		model.addAttribute("mygpa",mygpa);
		
		model.addAttribute("mysemester", stu.getSemester());
		model.addAttribute("mycourseenrolled", stucouservice.findAllByStudent(stu).size());
		return "mygrades";
	}
	
	@GetMapping("/enrollcourse")
	public String enrollCourse(Model model,@SessionAttribute UserSession usersession)
	{

		Students stu=stuservice.findById(usersession.getId());
		ArrayList<StudentCourse> stucoulist=stucouservice.findAllByStudent(stu);
		ArrayList<StudentCourse> availcourses=new ArrayList<StudentCourse>();
		ArrayList<StudentCourse> mycourseapp=new ArrayList<StudentCourse>();

		for (StudentCourse stucou:stucoulist)
		{
			if (stucou.getStatus().equals("Available"))
				availcourses.add(stucou);
			else
				mycourseapp.add(stucou);
			
		}
		model.addAttribute("availcourses",availcourses);
		model.addAttribute("mycourseapps",mycourseapp);

		model.addAttribute("mysemester", stu.getSemester());
		model.addAttribute("mycourseenrolled", stucouservice.findAllByStudent(stu).size());
		
		return "availablecourse";
	}
	
	@GetMapping("/applycourse/{id}")
	public String applyCourse(@PathVariable("id") Integer id,Model model)
	{
		StudentCourse stucou=stucouservice.findById(id);
		stucou.setStatus("Pending");
		stucouservice.save(stucou);
	
		return "forward:/student/enrollcourse";
	}

}
