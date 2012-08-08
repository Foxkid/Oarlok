package oarlok.dispatcher.DispatchFilter;

import java.util.HashMap;
import java.util.Map;

public class Router {
	
	//Create a Hash Map to store all the service URLs.
	Map<String,String> services = new HashMap<String,String>();
	
	public Router() {
		services.put("authenticationRequest","Login");
		services.put("createUserRequest","InsertUser");		
	}
	
	public String getService(String requestMessage) {
		String service = services.get(requestMessage);		
		return service;
	}	
	
	public boolean isValidService(String service) {		
		//System.out.println("Service - "+service);
		if(service.equals("/")) {
			return true;
		}else{
			return services.containsValue(replace(service,"/",""));
		}
	}
	
	private String replace(String str, String pattern, String replace) {
	    int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	        result.append(str.substring(s, e));
	        result.append(replace);
	        s = e+pattern.length();
	    }
	    result.append(str.substring(s));
	    return result.toString();
	}
}
