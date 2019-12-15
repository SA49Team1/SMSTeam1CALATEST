package sg.edu.nus.sms.controllers;

import java.rmi.registry.RegistryHandler;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.tomcat.jni.Registry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.model.User;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.FacultyRepository;
import sg.edu.nus.sms.repo.StudentsRepository;
import sg.edu.nus.sms.repo.UserRepository;
import sg.edu.nus.sms.service.LeaveAppService;
import sg.edu.nus.sms.service.LeaveAppServiceImpl;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private UserRepository userrepo;

	private FacultyRepository facrepo;

	private StudentsRepository sturepo;

	@Autowired
	public void setUserrepo(UserRepository userrepo) {
		this.userrepo = userrepo;
	}

	@Autowired
	public void setFacrepo(FacultyRepository facrepo) {
		this.facrepo = facrepo;
	}

	@Autowired
	public void setSturepo(StudentsRepository sturepo) {
		this.sturepo = sturepo;
	}

	@Autowired
	public void setLeaservice(LeaveAppService leaservice) {
		this.leaservice = leaservice;
	}

	private LeaveAppService leaservice;

	@Autowired
	public void setLeaservice(LeaveAppServiceImpl leaimpl) {
		this.leaservice = leaimpl;
	}

	@GetMapping("/index")
	public String index() {

		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	@GetMapping("/logout")
	public String logout(UserSession usersession, SessionStatus status) {
		status.setComplete();
		;
		return "forward:/home/login";
	}

//	@RequestMapping(value="/authenticate", path="/authenticate",method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
//	public String getAuthentication(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpSession session)
//	{
//		
//		
//		
//		if(bindingResult.hasErrors()) {
//			return "login";
//		}
//		
//		
//		User u1= userrepo.findByUserName(user.getUserName());
//		if(u1!=null) user.setId(u1.getId());
//		
//		
//		String username=user.getUserName();
//		String usertype;
//		if (facrepo.findByUserName(username)!=null) usertype="FAC";
//		else if (sturepo.findByUserName(username)!=null) usertype="STU";
//		else usertype="ADM";
//		
//		if(usertype=="STU")
//		{
//			UserSession usersession=new UserSession(userrepo.findByUserName(username).getId(),"STU");
//			session.setAttribute("usersession",usersession);
//			return "forward:/student/mygrades";
//		}
//		else if(usertype=="ADM") 
//		{
//			UserSession usersession=new UserSession(userrepo.findByUserName(username).getId(),"ADM");
//			session.setAttribute("usersession",usersession);
//			
//			return "forward:/admin/studentlist";
//		}
//		
//		else if(usertype=="FAC")
//		{
//			UserSession usersession=new UserSession(userrepo.findByUserName(username).getId(),"FAC");
//			session.setAttribute("usersession",usersession);
//			
//			return "forward:/faculty/assignedcourses";
//		}
//		
//		else
//			return "login";
//	}

	@GetMapping("/test")
	public String test(Principal principal, HttpSession session) {

		User u1 = userrepo.findByUserName(principal.getName());

		String s = u1.getRoles();

		if (s.equalsIgnoreCase("ROLE_FACULTY")) {
			UserSession usersession = new UserSession(userrepo.findByUserName(principal.getName()).getId(), "FAC");
			session.setAttribute("usersession", usersession);
			return "redirect:/faculty/assignedcourses";
		}

		else if (s.equalsIgnoreCase("ROLE_ADMIN")) {
			UserSession usersession = new UserSession(userrepo.findByUserName(principal.getName()).getId(), "ADM");
			session.setAttribute("usersession", usersession);
			return "redirect:/admin/studentlist";
		}

		else if (s.equalsIgnoreCase("ROLE_STUDENT")) {

			UserSession usersession = new UserSession(userrepo.findByUserName(principal.getName()).getId(), "STU");
			session.setAttribute("usersession", usersession);
			return "redirect:/student/mygrades";
		}
		return "forward:/faculty/assignedcourses";

	}

///////////////////Movement REg
	@GetMapping("/movement/{id}")
	public String MovementRegister(Model model, @SessionAttribute UserSession usersession, @PathVariable("id") int id) {

		List<LeaveApp> lealist = new ArrayList<LeaveApp>();
		lealist = leaservice.findAll().stream().filter(x -> x.getStatus().equals("Approved"))
				.collect(Collectors.toList());

		List<LeaveApp> this_month = new ArrayList<LeaveApp>();
		List<LeaveApp> last_month = new ArrayList<LeaveApp>();
		List<LeaveApp> next_month = new ArrayList<LeaveApp>();

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		int currentmonth = calendar.get(Calendar.MONTH);

		for (LeaveApp l : lealist) {
			date = l.getStartDate();
			calendar.setTime(date);
			int i = calendar.get(Calendar.MONTH);

			if (i == currentmonth) {
				this_month.add(l);
			} else if (i + 1 == currentmonth) {
				last_month.add(l);
			} else if (currentmonth == 11) {
				if (i + 11 == currentmonth)
					next_month.add(l);
			}
		}

		if (id == 2) {
			model.addAttribute("leavelists", this_month);
		} else if (id == 1) {
			model.addAttribute("leavelists", last_month);
		} else if (id == 3) {
			model.addAttribute("leavelists", next_month);
		}
		return "movementreg.html";
	}

}
