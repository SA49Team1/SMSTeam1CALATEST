package sg.edu.nus.sms.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import sg.edu.nus.sms.model.LeaveApp;



public class LeaveValidator implements ConstraintValidator<IsValidLeave, LeaveApp> {

    public void initialize(IsValidLeave constraintAnnotation) {
    }
    
	@Override
	public boolean isValid(LeaveApp value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		 if(value.getStartDate()==null || value.getEndDate()==null) {
			 
			 return false;
		 }
		if (value.getStartDate().toString().compareTo(value.getEndDate().toString()) > 0) {
			return false;
		} else {
			return true;
		}
	}
}