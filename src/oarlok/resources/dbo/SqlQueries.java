/** 
 *  @author OARLOK TEAM (FRAMEHAWK.COM)
 *  Email			- flipteam@framehawk.com
 *  Date Modified	- 8th AUG 2012.
 *  Information		- List all the SQL Statements that are used in the project. This is a read-only file.
 *   
 */

package oarlok.resources.dbo;

public class SqlQueries {
	
	private String LoginCoachQuery = "Select * from Coach WHERE email = ? AND password = ?";
	private String LoginRowerQuery = "Select * from Rower WHERE email = ? AND password = ?";

	public String getLoginCoachQuery() {
		return LoginCoachQuery;
	}

	public String getLoginRowerQuery() {
		return LoginRowerQuery;
	}
	
}
