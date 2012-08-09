package oarlok.resources.json;

import java.util.ArrayList;

public class CoachRower {
    private Integer coachId;
    private String coachName;
    private String coachProfileImage;
    private ArrayList<Rower> rowerDetails;
	public Integer getCoachId() {
		return coachId;
	}
	public void setCoachId(Integer coachId) {
		this.coachId = coachId;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
	public String getCoachProfileImage() {
		return coachProfileImage;
	}
	public void setCoachProfileImage(String coachProfileImage) {
		this.coachProfileImage = coachProfileImage;
	}
	public ArrayList<Rower> getRowerDetails() {
		return rowerDetails;
	}
	public void setRowerDetails(ArrayList<Rower> rowerDetails) {
		this.rowerDetails = rowerDetails;
	}        
}
