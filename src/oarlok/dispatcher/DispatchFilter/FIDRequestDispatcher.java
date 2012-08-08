package oarlok.dispatcher.DispatchFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import oarlok.resources.json.*;
import oarlok.service.connection.ServiceConnectionHandler;

public class FIDRequestDispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Get the request JSON from the client.
		String json_request = (String)request.getParameter("json_request_message");
		
		//Get the JSON Object from the String.
		Type type = new TypeToken<JSONMessage>(){}.getType();
		JSONMessage result = new Gson().fromJson(json_request, type);
		
		//Create a new Router object to get the service.
		Router router = new Router();
		
		//Get the service to be called.
		String Resource = router.getService(result.getMessageType());
				
		//Create a connection handler for which invokes the selected service.
		ServiceConnectionHandler connection = new ServiceConnectionHandler();
		connection.setResourceURL(request.getRequestURL().toString(), request.getServletPath(), Resource);		
		String json_response_message = connection.getServiceResponse(json_request);
			
		
		System.out.println("Res -- "+json_response_message);
		
		//Create the object to write onto the response stream.
		PrintWriter out = response.getWriter();
		response.setContentType("application/json; charset=UTF-8;");
		out.println(json_response_message);
	}

}
