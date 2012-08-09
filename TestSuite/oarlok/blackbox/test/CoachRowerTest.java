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

public class CoachRowerTest extends TestCase{
private static ServletRunner servletRunner = null;
	
	public static void main(String arg[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(CoachRowerTest.class);		
	}

	public CoachRowerTest(String s) {
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
	    assertNotNull("Cannot find Oarlok project", servletRunner);
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

	public void testGetCoachRowerListSuccess() throws Exception{
		
		 String RequestJSONString = "{ 'messageType':'getCoachRowersRequest', 'messageVersion':1, 'messagePayload':{'username':'vaishnav', 'password':'test', 'userType':'coach'}}";
		
		 ServletUnitClient client  = CoachRowerTest.getServletUnitClient();
		 WebRequest request   = new PostMethodWebRequest("http://localhost:8080/Oarlok/CoachAndRowersDetails");
		 request.setParameter("RequestType","testCode");
		 request.setParameter("json_request_message", RequestJSONString );
		 
		 //Debug Info
		 System.out.println("======================Coach Rower Test======================");		 
		 System.out.println("Resquest JSON - "+RequestJSONString);
		 
		 //Get the response.
		 WebResponse response = client.getResponse(request);		 
		 String responseJSON = response.getText();
		 Type type = new TypeToken<JSONMessage>() {}.getType();
		 JSONMessage result = new Gson().fromJson(responseJSON, type);
		 JSONClass message_payload = result.getMessagePayload();
		 
		 //Run Check for the valid parameters.
		 assertEquals("content type", "application/json", response.getContentType());
		 assertEquals("messageType", "getCoachRowersResponse",result.getMessageType());
		 assertEquals("success", true, message_payload.isSuccess());
		 assertEquals("resultCode", 0,result.getMessagePayload().getResultCode());
		 
		 //Debug Info
		 System.out.println("Oarlok Response JSON - "+ responseJSON);
	}	
	
	public void testGetCoachRowerListFail_RowerLogin() throws Exception{
		
		 String RequestJSONString = "{ 'messageType':'getCoachRowersRequest', 'messageVersion':1, 'messagePayload':{'username':'rower1', 'password':'test', 'userType':'rower'}}";
		
		 ServletUnitClient client  = CoachRowerTest.getServletUnitClient();
		 WebRequest request   = new PostMethodWebRequest("http://localhost:8080/Oarlok/CoachAndRowersDetails");
		 request.setParameter("RequestType","testCode");
		 request.setParameter("json_request_message", RequestJSONString );
		 
		 //Debug Info
		 System.out.println("======================Coach Rower Test======================");		 
		 System.out.println("Resquest JSON - "+RequestJSONString);
		 
		 //Get the response.
		 WebResponse response = client.getResponse(request);		 
		 String responseJSON = response.getText();
		 Type type = new TypeToken<JSONMessage>() {}.getType();
		 JSONMessage result = new Gson().fromJson(responseJSON, type);
		 JSONClass message_payload = result.getMessagePayload();
		 
		 //Run Check for the valid parameters.
		 assertEquals("content type", "application/json", response.getContentType());
		 assertEquals("messageType", "getCoachRowersResponse",result.getMessageType());
		 assertEquals("success", false, message_payload.isSuccess());
		 assertEquals("resultCode", 3,result.getMessagePayload().getResultCode());
		 
		 //Debug Info
		 System.out.println("Oarlok Response JSON - "+ responseJSON);
	}	
	
}
