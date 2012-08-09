package oarlok.application.errors;

import java.util.HashMap;
import java.util.Map;

public class ErrorMappings {

	//Create a Hash Map to store all the service URLs.
	Map<Integer,String> errors = new HashMap<Integer,String>();
	
	public ErrorMappings() {
		errors.put(0,"Login Successful");
		errors.put(1,"Login Unsuccessful");
		errors.put(2,"User Type Not Provided");
		errors.put(3,"Wrong Login Credentials. Try Again.");
		errors.put(4,"Invalid Date Of Birth in the database. Cannot process the result. Contact Adminstrator.");
		errors.put(500,"Server Not Responding. Please contact database administrator.");
	}
	
	public String getErrorMessage(Integer ErrorCode) {
		String service = errors.get(ErrorCode);		
		return service;
	}	
}
