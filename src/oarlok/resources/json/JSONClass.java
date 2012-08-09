/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oarlok.resources.json;

import java.util.ArrayList;

public class JSONClass {
    private String type; // operation:functionName, response:functionName
    
    private String username;
	private String password;
	private String userType;
	
    private Boolean success = null;
    private String message;
    private Integer resultCode = null;	
    
    private ArrayList<CoachRower> coachRowers;
	
	public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
       

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }	

	public int getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public void setUser(String user) {
		this.username = user;
	}

	public String getUser() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public ArrayList<CoachRower> getCoachRowers() {
		return coachRowers;
	}

	public void setCoachRowers(ArrayList<CoachRower> coachRowers) {
		this.coachRowers = coachRowers;
	}		
}
