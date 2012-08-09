/** 
 *  @author OARLOK TEAM (OARLOK.COM)
 *  Email			- help@oarlok.com
 *  Date Modified	- 7th Aug 2012.
 *  Information		- Takes in the user (Rower/Coach) Credentials and returns the Login Status JSON.
 *   
 */

package oarlok.application.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import oarlok.resources.json.*;
import oarlok.resources.dbo.*;
import oarlok.application.errors.*; 

 public class LoginResource extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
	public LoginResource() {
		super();
	}   	
	
	
	private String CreateResponse(boolean SuccessStatus, Integer ResultCode ) {
		String response;	
		
		JSONMessage json_message = new JSONMessage();
		json_message.setMessageType("authenticationResponse");
		
		JSONClass jclass = new JSONClass();			
				
		jclass.setSuccess(SuccessStatus);
		jclass.setResultCode(ResultCode);
		jclass.setMessage(new ErrorMappings().getErrorMessage(ResultCode));
		
		//Set the message payload.
		json_message.setMessagePayload(jclass);

		//Get the final json string.
		response = new Gson().toJson(json_message);
		
		return response;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		//Get the user login string from the application.
		String jsonStringUserLogin = (String)request.getParameter("json_request_message");
				
		//De-serializing the JSON message passed from the UGM application. 
		Type type = new TypeToken<JSONMessage>() {}.getType();
		
		//Use GSON class.
		JSONMessage result = new Gson().fromJson(jsonStringUserLogin, type);
		
		//Get the sent message payload.
		JSONClass message_payload = result.getMessagePayload();
		
		//Get the connection handler and store it in this variable.
		Connection myConnection = null;
		PreparedStatement myStatement;
		
		//Create the object to write onto the response stream.
		PrintWriter out = response.getWriter();
		response.setContentType("application/json; charset=UTF-8;");
		
		try {
			
			//Extract the required fields from the JSON.
			String user_name = message_payload.getUser();
			String user_password = message_payload.getPassword();								
			String user_type = message_payload.getUserType();
			
			// encrypt password
			//MD5 md=new MD5();
			//String user_password_encrypted =md.MD5encrypt(user_password);
			String user_password_encrypted = user_password;
			
			
			String SQLstatement = null;
			
			if(user_type.equalsIgnoreCase("coach")) {				
				SQLstatement = new SqlQueries().getLoginCoachQuery();
			}else if(user_type.equalsIgnoreCase("rower")){
				SQLstatement = new SqlQueries().getLoginRowerQuery();
			}else {
				throw new JSONDataException("The JSON Data sent is not correct.");				
			}
			
			//Call the connection handler to get the connection object.
			myConnection = new DatabaseConnection().getConnectionHandler();			
			myStatement = myConnection.prepareStatement(SQLstatement);
			myStatement.setString(1, user_name);
			myStatement.setString(2, user_password_encrypted);				
			ResultSet rs = myStatement.executeQuery();
									
			if (!(rs.next())) {				
				out.println(CreateResponse(false, 1));				
			} else {
				out.println(CreateResponse(true, 0));				
			}			
			
		}catch (JSONDataException e) {
			out.println(CreateResponse(false, 2));			
			//System.out.println(e);
		}catch (Exception e) {
			out.println(CreateResponse(false, 500));			
			//System.out.println(e);
		}				
	}   	  	    
}
