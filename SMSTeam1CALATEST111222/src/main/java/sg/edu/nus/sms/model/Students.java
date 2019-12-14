package sg.edu.nus.sms.model;

import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

@Entity
@DiscriminatorValue("STU")
public class Students extends User{
	
	@NotNull
	@Column(unique=true)
	@CsvBindByPosition(position=0)
	private int studentID;
	
	@NotEmpty
	@CsvBindByPosition(position=1)
	private String firstName;
	@NotEmpty
	@CsvBindByPosition(position=2)
	private String lastName;
	@NotEmpty
	@CsvBindByPosition(position=3)
	private String semester;
	
	
	private int cgpa;
	
	@OneToMany(mappedBy="student", cascade= {CascadeType.ALL})
	private List<StudentCourse> GradeList;
	
	

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}



	public int getCgpa() {
		return cgpa;
	}

	public void setCgpa(int cgpa) {
		this.cgpa = cgpa;
	}

	public List<StudentCourse> getGradeList() {
		return GradeList;
	}

	public void setGradeList(List<StudentCourse> gradeList) {
		GradeList = gradeList;
	}







	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public Students() {
		super();
		this.cgpa=-1;
		// TODO Auto-generated constructor stub
	}

	public Students(int studentID, String firstName, String lastName, String semester) {
		super();
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.semester = semester;
		
		
	}

	@Override
	public String toString() {
		return "[" + firstName + "," + lastName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + studentID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Students other = (Students) obj;
		if (studentID != other.studentID)
			return false;
		return true;
	}
	
	

}
