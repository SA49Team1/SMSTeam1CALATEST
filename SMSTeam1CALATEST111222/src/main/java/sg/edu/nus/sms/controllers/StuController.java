package sg.edu.nus.sms.controllers;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.event.DocumentListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import ch.qos.logback.classic.Logger;
import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.CourseRepository;
import sg.edu.nus.sms.repo.LeaveAppRepository;
import sg.edu.nus.sms.repo.StudentCourseRepository;
import sg.edu.nus.sms.repo.StudentsRepository;
import sg.edu.nus.sms.service.CourseService;
import sg.edu.nus.sms.service.CourseServiceImpl;
import sg.edu.nus.sms.service.FacultyService;
import sg.edu.nus.sms.service.FacultyServiceImpl;
import sg.edu.nus.sms.service.LeaveAppService;
import sg.edu.nus.sms.service.LeaveAppServiceImpl;
import sg.edu.nus.sms.service.StudentCourseService;
import sg.edu.nus.sms.service.StudentCourseServiceImpl;
import sg.edu.nus.sms.service.StudentService;
import sg.edu.nus.sms.service.StudentServiceImpl;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/student")
public class StuController {

	/////////////////////////Data Service
	private LeaveAppService leaservice;
	
	@Autowired
	public void setLeaservice(LeaveAppServiceImpl leaimpl)
	{
		this.leaservice=leaimpl;
	}
	
	
	private CourseService couservice;
	@Autowired
	public void setCouservice(CourseServiceImpl couimpl) {
		this.couservice = couimpl;
	}

	
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
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		
	}
	
	@GetMapping("/mygrades")
	public String mygrades(@SessionAttribute UserSession usersession, Model model) {
		
		if(!usersession.getUserType().equals("STU")||usersession==null) return "forward:/home/logout";
		
		//Pageable page1 = (Pageable) PageRequest.of(0, 3);
		
		Students stu=stuservice.findById(usersession.getId());
		
		List<StudentCourse> stucoulist=stucouservice.findAllByStudent(stu);
		
		List<StudentCourse> compstucoulist=new ArrayList<StudentCourse>();
		double mygpa=0.0;
		double units=0.0;
		
		//org.jboss.logging.Logger logger = LoggerFactory.logger(StuController.class);
		/*for(StudentCourse sc:stucoulist)
		{
			//logger.info("StudentCoursegggggggggggggggggggggggggggggggggggggg"+sc);
			if (sc.getStatus().equals("Graded")) 
				{
				compstucoulist.add(sc);
				if(sc.getGrade().equals("A")) {
					mygpa+=5*sc.getCourse().getCourseUnit();
					units+=sc.getCourse().getCourseUnit();
				}
				else if (sc.getGrade().equals("B")) {
					mygpa+=4*sc.getCourse().getCourseUnit();
					units+=sc.getCourse().getCourseUnit();
				}
				else if (sc.getGrade().equals("C")) {
					mygpa+=3*sc.getCourse().getCourseUnit();
					units+=sc.getCourse().getCourseUnit();
					}
				else if (sc.getGrade().equals("D")) {
					mygpa+=2*sc.getCourse().getCourseUnit();
					units+=sc.getCourse().getCourseUnit();
				}
				else if(sc.getGrade().equals("E")) {
					mygpa+=1*sc.getCourse().getCourseUnit();
					units+=sc.getCourse().getCourseUnit();
				}
			
		}*/
		
		mygpa=stu.getCgpa();
		units=stucouservice.findAllByStudent(stu).stream().collect(Collectors.summingInt(x->x.getCourse().getCourseUnit()));
		
			double a1=mygpa/units;
			double aa=Math.round(a1 * 100.0) / 100.0;
	
		
		
		model.addAttribute("studentname",stu.toString());
		model.addAttribute("compstucoulist", compstucoulist);
		model.addAttribute("mygpa",aa);
		
		model.addAttribute("mysemester", stu.getSemester());
		//model.addAttribute("mycourseenrolled", stucourepo.findAllByStudent(stu,(Pageable) PageRequest.of(0,3)).size());
		return "mygrades";
	}
	
	
	@RequestMapping(value = "/enrollcourse", method = RequestMethod.GET)
    public String listBooks(@SessionAttribute UserSession usersession,Model model,@RequestParam("page") Optional<Integer> page,@RequestParam("size") Optional<Integer> size) 
	{
		
		Students stu=stuservice.findById(usersession.getId());
		List<StudentCourse>stucourse= stucouservice.findAllByStudent(stu);
		List<StudentCourse>student_course_list=new ArrayList<StudentCourse>();
		System.err.println("----- \t \t Pagination Method \t \t -----");
		
		for(StudentCourse c:stucourse) {
			
			if(c.getStatus().equals("Available")) {
				student_course_list.add(c);
				//System.out.println("SSSSSSSSSSS"+c.getStatus());
			}
			
		}
		
		
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        
        currentPage = currentPage - 1;
        
        
	       int startItem = currentPage * pageSize;
	       List<StudentCourse> list;

	       if (student_course_list.size() < startItem) 
	       {
	    	   System.out.print("List ITEM EMPTY");
	           list = Collections.emptyList();
	       }
	       else {
	           int toIndex = Math.min(startItem + pageSize, student_course_list.size());
	           list = student_course_list.subList(startItem, toIndex);
	       }

	       Page<StudentCourse> Student_Course_Page = new PageImpl<StudentCourse>(list, PageRequest.of(currentPage, pageSize), student_course_list.size());
	       
		int totalPages = Student_Course_Page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            
        model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("bookPage",Student_Course_Page);
		
        return "availablecourse.html";
	}
	
	@GetMapping("/applycourse/{id}")
	public String applyCourse(@PathVariable("id") Integer id,Model model,@SessionAttribute UserSession usersession)
	{
		Course cou=couservice.findById(id);
		Students stu=stuservice.findById(usersession.getId());
		StudentCourse stucou =stucouservice.findByCourseAndStudent(cou,stu);
		stucou.setStatus("Pending");
		stucouservice.save(stucou);
	
		return "forward:/student/enrollcourse";
	}
	
	
	@GetMapping("/enrolled")
	public String EnrolledCourse(@SessionAttribute UserSession usersession,Model model)
	{
		Students stu=stuservice.findById(usersession.getId());
		
		List<StudentCourse> stucoulist=stucouservice.findAllByStudent(stu);
	
		List<StudentCourse> total=stucoulist.stream().filter(x->x.getStatus().equals("Approved")).collect(Collectors.toList());
				

		 model.addAttribute("enrolledcourse", total);
		return "enrolledcourses.html";
	}
	
	
	@GetMapping("/cancelenroll/{id}")
	public String cancelEnrolled(@PathVariable("id") Integer id,Model model,@SessionAttribute UserSession usersession)
	{
		Course cou=couservice.findById(id);
		Students stu=stuservice.findById(usersession.getId());
		StudentCourse stucou=stucouservice.findByCourseAndStudent(cou,stu);
	
		
				stucou.setStatus("Available");
				stucouservice.save(stucou);
		
		return "forward:/student/enrolled";
	}
	
	

	
	 
	}
