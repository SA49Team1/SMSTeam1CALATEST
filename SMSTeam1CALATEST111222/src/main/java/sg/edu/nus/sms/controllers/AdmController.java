package sg.edu.nus.sms.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.catalina.filters.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.CourseRepository;
import sg.edu.nus.sms.repo.FacultyRepository;
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
@RequestMapping("/admin")
public class AdmController {
	
private LeaveAppService leaservice;
	
	@Autowired
	public void setLeaservice(LeaveAppServiceImpl leaimpl)
	{
		this.leaservice=leaimpl;
	}

	private FacultyService facservice;
	@Autowired
	public void setFacservice(FacultyServiceImpl facimpl) {
		this.facservice = facimpl;
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
	
	static List<String> dlist= Arrays.asList("Physics","Chemistry","Magic","Literature");
	
	
	//////////////////////////////////////////Student
	
	@GetMapping("/studentlist")
	public String liststudents(Model model, @SessionAttribute UserSession usersession) {
		
		
		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";

		ArrayList<Students> stulist=new ArrayList<Students>();
		stulist.addAll(stuservice.findAll());
		model.addAttribute("students",stulist);
		
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		
		return "studentlist";
	}
	
	@GetMapping("/addstudent")
	public String addStudentForm(Model model) {
		Students stu=new Students();
		
		model.addAttribute("student",stu);
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "studentform";
	}
	
	@RequestMapping(value="/savestudent",path="/savestudent", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveStudent(@Valid @ModelAttribute Students stu, BindingResult bindingResult,Model model) {
		
		if(bindingResult.hasErrors())
		{
			
			return "forward:/admin/addstudent";
		}
		
		Students s1= stuservice.findByStudentID(stu.getStudentID());
		
		if(s1!=null) stu.setId(s1.getId());
		
		//////verify if username already exist
		if(stuservice.existsByUserName(stu.getUserName())) return "forward:/admin/addstudent";
		
		stuservice.save(stu);
		
		////////////////Initiate course application list for each student
		List<Course> coulist=couservice.findAll();
		
		for(Course cou:coulist) {
			StudentCourse stucou=new StudentCourse();
			stucou.setStudent(stu);
			stucou.setCourse(cou);
			stucouservice.save(stucou);
		}
		
		return "forward:/admin/studentlist";
	}

	@GetMapping("/editstudent/{id}")
	public String editStudentForm(Model model, @PathVariable("id") Integer id) {
		Students stu=stuservice.findById(id);
		model.addAttribute("student",stu);
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "studentform";
	}

	
	@GetMapping("/deletestudent/{id}")
	public String deleteStudent(Model model, @PathVariable("id") Integer id) {
		Students stu=stuservice.findById(id);
		
		//remove correlated stu_course record before remove course record.
		List<StudentCourse> stucoulist=stucouservice.findAllByStudent(stu);
		stucouservice.deleteAll(stucoulist);
		
		
		stuservice.delete(stu);
		
		
		
		return "forward:/admin/studentlist";
	}
	
	/////////////////////////////////////////////////////faculty
	
	@GetMapping("/facultylist")
	public String listfaculty(Model model,@SessionAttribute UserSession usersession) {
		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
		
		List<Faculty> faclist=new ArrayList<Faculty>();
		faclist=facservice.findAll();
		model.addAttribute("faculties",faclist);

		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		return "facultylist";
	}
	
	
	@GetMapping("/addfaculty")
	public String addFacultyForm(Model model) {
		Faculty fac=new Faculty();
		model.addAttribute("faculty",fac);
		model.addAttribute("departmentlist",dlist);
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		return "facultyform";
	}
	
	@GetMapping("/editfaculty/{id}")
	public String editFacultyForm(Model model, @PathVariable("id") Integer id) {
		Faculty fac=facservice.findById(id);
		
		model.addAttribute("departmentlist",dlist);
		model.addAttribute("faculty",fac);
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "facultyform";
	}
	
	@GetMapping("/deletefaculty/{id}")
	public String deleteFaculty(Model model, @PathVariable("id") Integer id) {
		Faculty fac=facservice.findById(id);
		facservice.delete(fac);
		return "forward:/admin/facultylist";
	}
	
	
	@RequestMapping(value="/savefaculty",path="/savefaculty", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveFaculty(@Valid @ModelAttribute Faculty fac, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "forward:/admin/addfaculty";
		}
		
        Faculty f1= facservice.findByFacultyID(fac.getFacultyID());
		
		if(f1!=null) fac.setId(f1.getId());
		
		//////verify if username already exist
		if(stuservice.existsByUserName(fac.getUserName())) return "forward:/admin/addstudent";
		
		facservice.save(fac);
		
		return "forward:/admin/facultylist";
	}
	
	///////////////////////////////////////////Course
	
	@GetMapping("/courselist")
	public String listcourse(Model model,@SessionAttribute UserSession usersession) {

		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
		
		List<Course> coulist=new ArrayList<Course>();
		coulist=couservice.findAll();
		model.addAttribute("courses",coulist);
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		
		
		
		return "courselist";
	}
	
	
	@GetMapping("/addcourse")
	public String addCourseForm(Model model) {
		Course cou=new Course();
		Faculty abs=new Faculty();
		cou.setCurrentFaculty(abs);
		
		model.addAttribute("departmentlist",dlist);
		model.addAttribute("course",cou);
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "courseform";
	}
	
	@GetMapping("/assignfaculty/{id}")
	public String assignfaculty(Model model,@PathVariable("id") Integer id) {
		Course cou=couservice.findById(id);
		model.addAttribute("course", cou);
		ArrayList<Faculty> facsdepart=facservice.findByDepartment(cou.getDepartment());
		model.addAttribute("facsofdepartment", facsdepart);
		
		model.addAttribute("departmentlist",dlist);
		
		return "assignfacultyform";
	}
	
	
	@GetMapping("/editcourse/{id}")
	public String editCourseForm(Model model, @PathVariable("id") Integer id) {
		Course cou=couservice.findById(id);
		
		model.addAttribute("departmentlist",dlist);
		model.addAttribute("course",cou);
		
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		return "courseform";
	}
	
	@GetMapping("/deletecourse/{id}")
	public String deleteCourse(Model model, @PathVariable("id") Integer id) {
		
		Course cou=couservice.findById(id);
		
		//remove correlated stu_course record before remove course record.
		List<StudentCourse> stucoulist=stucouservice.findAllByCourse(cou);
		stucouservice.deleteAll(stucoulist);
		
		couservice.delete(cou);
		
		
		
		return "forward:/admin/courselist";
	}
	
	
	@RequestMapping(value="/savecourse",path="/savecourse", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveCourse(@Valid @ModelAttribute Course cou, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "forward:/admin/addcourse";
		}
		
		Course c1= couservice.findByCourseCode(cou.getCourseCode());
		
		if(c1!=null) cou.setId(c1.getId());
		
		
		couservice.save(cou);
		
		return "forward:/admin/courselist";
	}
	
	
	@RequestMapping(value="/saveassign",path="/saveassign", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveAssign(@ModelAttribute Course cou) {
		
		Course c1= couservice.findByCourseCode(cou.getCourseCode());
		
		if(c1!=null) cou.setId(c1.getId());
		
		
		couservice.save(cou);
		
		return "forward:/admin/courselist";
	}
	
	/////////////////////////////////////////Leave application
	
	@GetMapping("/applicationlist")
	public String listleaveapp(Model model, @SessionAttribute UserSession usersession) {

		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
		
		
		List<LeaveApp> lealist=new ArrayList<LeaveApp>();
		List<LeaveApp> alllealist=new ArrayList<LeaveApp>();
		lealist=leaservice.findAllByStatus("Pending");
		alllealist=leaservice.findAll();
		model.addAttribute("leaveapps",lealist);
		model.addAttribute("allleaveapps",alllealist);
		
		
		int leacount=leaservice.findAllByStatus("Pending").size();
		int couappcount=stucouservice.findAllByStatus("Pending").size();
		model.addAttribute("stucount",stuservice.count());
		model.addAttribute("faccount",facservice.count());
		model.addAttribute("coucount",couservice.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		
		
		
		return "applicationlist";
	}
	
	

	
	@GetMapping("/approveleaveapp/{id}")
	public String approveLeaveAppForm(Model model, @PathVariable("id") Integer id) {
		LeaveApp leaapp=leaservice.findById(id);
		leaapp.setStatus("Approved");
		leaservice.save(leaapp);
		model.addAttribute("leaveapp",leaapp);
				
		return "forward:/admin/applicationlist";
	}
	
	@GetMapping("/rejectleaveapp/{id}")
	public String rejectLeaveAppForm(Model model, @PathVariable("id") Integer id) {
		LeaveApp leaapp=leaservice.findById(id);
		leaapp.setStatus("Rejected");
		leaservice.save(leaapp);
		model.addAttribute("leaveapp",leaapp);
				
		return "forward:/admin/applicationlist";
	}

	
	
	@RequestMapping(value="/saveleaveapp",path="/saveleaveapp", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveLeaveApp(@Valid @ModelAttribute LeaveApp lea, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "leaveappform";
		}
		
		Faculty f1=facservice.findByFirstName("Jon");
		lea.setFaculty(f1);
		
		leaservice.save(lea);
		
		return "forward:/admin/applicationlist";
	}
	
	//////////////////////////////////Course Application
	@GetMapping("/courseapplist")
	public String courseAppList(Model model,@SessionAttribute UserSession usersession) {
		
	if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
	
	List<StudentCourse> stucoulist=stucouservice.findAll();
	List<StudentCourse> pendingstucoulist=new ArrayList<StudentCourse>();
	List<StudentCourse> managedstucoulist=new ArrayList<StudentCourse>();
	
	for (StudentCourse stucou:stucoulist)
	{
		if(stucou.getStatus().equals("Pending")) 
			{
			pendingstucoulist.add(stucou);
			}
		else if (stucou.getStatus().equals("Approved")||stucou.getStatus().equals("Rejected"))
			{
			managedstucoulist.add(stucou);
			}
		
	}
	
	model.addAttribute("pendingstucoulist",pendingstucoulist);
	model.addAttribute("managedstucoulist",managedstucoulist);
	
	
	int leacount=leaservice.findAllByStatus("Pending").size();
	int couappcount=stucouservice.findAllByStatus("Pending").size();
	model.addAttribute("stucount",stuservice.count());
	model.addAttribute("faccount",facservice.count());
	model.addAttribute("coucount",couservice.count());
	model.addAttribute("leacount",leacount);
	model.addAttribute("couappcount",couappcount);
	
	
	
	return "courseapplist";
	}
	
	@GetMapping("/approvecourseapp/{id}")
	public String approveCourseApp(@PathVariable("id") Integer id) {
		StudentCourse stucou=stucouservice.findById(id);
		stucou.setStatus("Approved");
		stucouservice.save(stucou);
				
		return "forward:/admin/courseapplist";
	}
	
	@GetMapping("/rejectcourseapp/{id}")
	public String rejectCourseApp(@PathVariable("id") Integer id) {
		StudentCourse stucou=stucouservice.findById(id);
		stucou.setStatus("Rejected");
		stucouservice.save(stucou);
		return "forward:/admin/courseapplist";
	}
}
