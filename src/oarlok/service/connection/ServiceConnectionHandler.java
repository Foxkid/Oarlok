/*
 * This java class handles the connection parameters to the FID server.
 * 
 * (!Important) Please be careful while changing the parameters.  
 * 				There parameters are used throughout the client application.
 *              This is a read-only class.
 * 
 * @Authors 		- FLIP TEAM ( FRMAEHAWK.COM)
 * Email 			- FLIP@FRAMEHAWK.COM
 * Date Modified	- 19th July 2012
 */

package oarlok.service.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServiceConnectionHandler {
	private String service_url = "";
	
	/*
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
	}*/
			
	public void setResourceURL(String RequestUrl, String DispatcherPath,String Resource) {
		//service_url =  replace(RequestUrl,DispatcherPath,"").concat(Resource);
		service_url =  RequestUrl.concat(Resource);
		System.out.println("Res == "+Resource);
	}
			
	public String getServiceResponse(String RequestJSON) throws ClientProtocolException, IOException {
		
		//Create a HttpClient.
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(service_url);

		//Add the POST parameters.
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("json_request_message", RequestJSON));		
    	httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		HttpResponse fidresponse = client.execute(httpPost);		
		
		//Get the response entity.
		HttpEntity entity  = fidresponse.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
		
		//Read the response from the FID.
		String lineRead = null;
		String FID_response = "";
		while ((lineRead=br.readLine())!=null) {
			FID_response=FID_response+lineRead;
		}
		
		//This assumes that the response is one line of JSON Code.
		return(String)FID_response;
	}
}
