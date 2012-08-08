package oarlok.blackbox.test;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import oarlok.resources.json.JSONClass;
import oarlok.resources.json.JSONMessage;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CoachLoginTest extends TestCase{
private static ServletRunner servletRunner = null;
	
	public static void main(String arg[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(CoachLoginTest.class);		
	}

	public CoachLoginTest(String s) {
		super(s);
	}

	private static ServletUnitClient getServletUnitClient() throws Exception {
	    if (servletRunner == null) {
	      final File webContentRoot = getWebContentRoot();
	      assertNotNull(webContentRoot);

	      final File webXML = new File(webContentRoot + "/WEB-INF/web.xml");
	      assertTrue(webXML.exists());

	      servletRunner = new ServletRunner(webXML.getAbsoluteFile(), "/Oarlok");
	    }
	    assertNotNull("Cannot find Foo project", servletRunner);
	    return servletRunner.newClient();
	  }
	
	private static File getWebContentRoot() {
		final String currentPath = System.getProperty("user.dir");
		for (File parent = new File(currentPath).getParentFile(); parent.exists(); parent = parent.getParentFile()) {
			if (Arrays.asList(parent.list()).contains("Oarlok")) {
				final File file = new File(parent.getPath() + "/Oarlok/WebContent");
				assertTrue("Cannot find UnitTest/WebContent", file.exists());
				return file.getAbsoluteFile();
			}
		}
		return null;
	}

	public void testLoginSuccess() throws Exception{
		
		 String RequestJSONString = "{ 'messageType':'authenticationRequest', 'messageVersion':1, 'messagePayload':{'username':'vaidesai@yahoo.com', 'password':'test', 'userType':'coach'}}";
		
		 ServletUnitClient client  = CoachLoginTest.getServletUnitClient();
		 WebRequest request   = new PostMethodWebRequest("http://localhost:8080/Oarlok/Login");
		 request.setParameter("RequestType","testCode");
		 request.setParameter("json_request_message", RequestJSONString );
		 
		 //Debug Info
		 System.out.println("======================Coach Login Test======================");		 
		 System.out.println("Resquest JSON - "+RequestJSONString);
		 
		 //Get the response.
		 WebResponse response = client.getResponse(request);		 
		 String responseJSON = response.getText();
		 Type type = new TypeToken<JSONMessage>() {}.getType();
		 JSONMessage result = new Gson().fromJson(responseJSON, type);
		 JSONClass message_payload = result.getMessagePayload();
		 
		 //Run Check for the valid parameters.
		 assertEquals("content type", "application/json", response.getContentType());
		 assertEquals("messageType", "authenticationResponse",result.getMessageType());
		 assertEquals("success", true, message_payload.isSuccess());
		 assertEquals("resultCode", 0,result.getMessagePayload().getResultCode());
		 
		 //Debug Info
		 System.out.println("Oarlok Response JSON - "+ responseJSON);
	}
	
	public void testLoginPasswordFaliure() throws Exception{
		
		 String RequestJSONString = "{ 'messageType':'authenticationRequest', 'messageVersion':1, 'messagePayload':{'username':'vaidesai@yahoo.com', 'password':'somepass', 'userType':'coach'}}";
		
		 ServletUnitClient client  = CoachLoginTest.getServletUnitClient();
		 WebRequest request   = new PostMethodWebRequest("http://localhost:8080/Oarlok/Login");
		 request.setParameter("RequestType","testCode");
		 request.setParameter("json_request_message", RequestJSONString );
		 
		 //Debug Info.
		 System.out.println("======================Coach Login Test (Wrong Password)======================");
		 System.out.println("Resquest JSON - "+RequestJSONString);
		
		 //Get the server response.
		 WebResponse response = client.getResponse(request);
		 String responseJSON = response.getText();
		 Type type = new TypeToken<JSONMessage>() {}.getType();
		 JSONMessage result = new Gson().fromJson(responseJSON, type);
		 JSONClass message_payload = result.getMessagePayload();
		 
		 //Test-Conditions..
		 assertEquals("content type", "application/json", response.getContentType());
		 assertEquals("messageType", "authenticationResponse",result.getMessageType());
		 assertEquals("resultCode", 1,result.getMessagePayload().getResultCode());
		 assertEquals("success", false, message_payload.isSuccess());
		 
		 //Debug Info
		 System.out.println("Oarlok Response JSON - "+response.getText());
	}
	
	public void testLoginUserNameFaliure() throws Exception{
		
		 String RequestJSONString = "{ 'messageType':'authenticationRequest', 'messageVersion':1, 'messagePayload':{'username':'SomeUserName', 'password':'test', 'userType':'coach'}}";
		
		 ServletUnitClient client  = CoachLoginTest.getServletUnitClient();
		 WebRequest request   = new PostMethodWebRequest("http://localhost:8080/Oarlok/Login");
		 request.setParameter("RequestType","testCode");
		 request.setParameter("json_request_message", RequestJSONString );
		 
		 //Debug Info.
		 System.out.println("======================Coach Login Test (Wrong Password)======================");
		 System.out.println("Resquest JSON - "+RequestJSONString);
		
		 //Get the server response.
		 WebResponse response = client.getResponse(request);
		 String responseJSON = response.getText();
		 Type type = new TypeToken<JSONMessage>() {}.getType();
		 JSONMessage result = new Gson().fromJson(responseJSON, type);
		 JSONClass message_payload = result.getMessagePayload();
		 
		 //Test-Conditions..
		 assertEquals("content type", "application/json", response.getContentType());
		 assertEquals("messageType", "authenticationResponse",result.getMessageType());
		 assertEquals("success", false, message_payload.isSuccess());
		 assertEquals("resultCode", 1,result.getMessagePayload().getResultCode());
		 
		 //Debug Info
		 System.out.println("Oarlok Response JSON - "+response.getText());
	}		
}
