package oarlok.application.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oarlok.application.errors.ErrorMappings;
import oarlok.application.errors.InvalidDateException;
import oarlok.application.errors.JSONDataException;
import oarlok.application.errors.WrongLoginCredentialException;
import oarlok.resources.dbo.DatabaseConnection;
import oarlok.resources.dbo.SqlQueries;
import oarlok.resources.json.CoachRower;
import oarlok.resources.json.JSONClass;
import oarlok.resources.json.JSONMessage;
import oarlok.resources.json.Rower;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CoachRowerDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String CreateResponse(boolean SuccessStatus, Integer ResultCode, ArrayList<CoachRower> CoachList ) {
		String response;	
		
		JSONMessage json_message = new JSONMessage();
		json_message.setMessageType("getCoachRowersResponse");
		
		JSONClass jclass = new JSONClass();			
				
		jclass.setSuccess(SuccessStatus);
		jclass.setResultCode(ResultCode);
		jclass.setMessage(new ErrorMappings().getErrorMessage(ResultCode));
		jclass.setCoachRowers(CoachList);
		
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
			Boolean resultStatus = true;
			
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
					//SQLstatement = new SqlQueries().getLoginRowerQuery();
					resultStatus = false;
					throw new WrongLoginCredentialException("Wrong User");
				} else {
					resultStatus = false;
					throw new JSONDataException("The JSON Data sent is not correct.");
				}
				
				//Call the connection handler to get the connection object.
				myConnection = new DatabaseConnection().getConnectionHandler();			
				myStatement = myConnection.prepareStatement(SQLstatement);
				myStatement.setString(1, user_name);
				myStatement.setString(2, user_password_encrypted);				
				ResultSet rs = myStatement.executeQuery();
				
				if (!(rs.next())) {	
					throw new WrongLoginCredentialException("The JSON Data sent is not correct.");
				}
				
				SQLstatement = new SqlQueries().getCoachData();
				myConnection = new DatabaseConnection().getConnectionHandler();			
				myStatement = myConnection.prepareStatement(SQLstatement);
				rs = myStatement.executeQuery();						
				ArrayList<CoachRower> CoachList = new ArrayList<CoachRower>();				
				
				while(rs.next()) {
					CoachRower coachObject = new CoachRower();
					Integer Coach_ID = rs.getInt("coach_id");
					coachObject.setCoachId(Coach_ID);
					coachObject.setCoachName(rs.getString("coach_name"));
					String coachProfilerImage = rs.getString("image");
					if(coachProfilerImage!=null) {
						coachObject.setCoachProfileImage(coachProfilerImage);					
					} else {
						coachObject.setCoachProfileImage("");
					}
					
					//Get all the rowers for a Rower ID.
					SQLstatement = new SqlQueries().getRowerDataForCoach();
					myConnection = new DatabaseConnection().getConnectionHandler();			
					myStatement = myConnection.prepareStatement(SQLstatement);
					myStatement.setInt(1, Coach_ID);
					
					ResultSet rower = myStatement.executeQuery();
					ArrayList<Rower> RowerList = new ArrayList<Rower>();
					
					while(rower.next()) {
						Rower rowerObject = new Rower();
						rowerObject.setRowerName(rower.getString("Rower_name"));
						rowerObject.setRowerGender(rower.getString("Rower_gender"));
						rowerObject.setRowerWeight(rower.getString("Rower_weight"));
						rowerObject.setRowerHeight(rower.getString("Rower_height"));
						try {
							rowerObject.setRowerAge(getAge(rower.getString("Rower_DOB")));
						} catch(InvalidDateException e) {
							resultStatus = false;
							out.println(CreateResponse(false, 4, null));
						}
						RowerList.add(rowerObject);
					}
					coachObject.setRowerDetails(RowerList);
					CoachList.add(coachObject);
				}
				
				if(resultStatus) {
					out.println(CreateResponse(true, 0, CoachList));
				}
				
			}catch (WrongLoginCredentialException e) {
				resultStatus = false;
				out.println(CreateResponse(false, 3, null));	
			}catch (JSONDataException e) {
				resultStatus = false;
				out.println(CreateResponse(false, 2, null));
			}catch (Exception e) {
				resultStatus = false;
				out.println(CreateResponse(false, 500, null));	
			}
	}

	public int getAge(String DOB) throws InvalidDateException {
		String[] dataFormatted = DOB.split("-"); 
		Calendar cal = new GregorianCalendar(Integer.parseInt(dataFormatted[0]),Integer.parseInt(dataFormatted[1]),Integer.parseInt(dataFormatted[2]));
	    Calendar now = new GregorianCalendar();
	    int age = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
	    if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
	        || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) > now
	            .get(Calendar.DAY_OF_MONTH))) {
	      age--;
	    }
	    if(age<=0) {	    	
			throw new InvalidDateException("The date entered is not valid.");			
	    }
	    return age;
	}	
}
