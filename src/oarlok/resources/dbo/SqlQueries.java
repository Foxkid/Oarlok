/** 
 *  @author OARLOK TEAM (FRAMEHAWK.COM)
 *  Email			- flipteam@framehawk.com
 *  Date Modified	- 8th AUG 2012.
 *  Information		- List all the SQL Statements that are used in the project. This is a read-only file.
 *   
 */

package oarlok.resources.dbo;

public class SqlQueries {
	
	private String LoginCoachQuery = "Select * from Coach WHERE username = ? AND password = ?";
	private String LoginRowerQuery = "Select * from Rower WHERE username = ? AND password = ?";
	private String CoachData = "Select * from Coach";
	private String RowerDataForCoach = "Select * from Rower where Coach_Id=?";
	
	public String getLoginCoachQuery() {
		return LoginCoachQuery;
	}

	public String getLoginRowerQuery() {
		return LoginRowerQuery;
	}

	public String getCoachData() {
		return CoachData;
	}

	public String getRowerDataForCoach() {
		return RowerDataForCoach;
	}

	public void setRowerDataForCoach(String rowerDataForCoach) {
		RowerDataForCoach = rowerDataForCoach;
	}
	
}
