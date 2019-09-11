/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.client.entity.DecompressingEntity.
import java.util.*;
import java.io.*;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
/**
 *
 * @author mwiser
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try
        {
       String url = "http://localhost:8088/services/collector";
     //   String url = "http://localhost:8444/services/collector";
        
	HttpClient client = HttpClientBuilder.create().build();
	HttpPost post = new HttpPost(url);

	// add header
	post.setHeader("User-Agent", "Mama");
        post.setHeader("Authorization", "Splunk 6b1413a3-d992-483f-9963-0778aadc7138");
        post.setHeader("Content-Type", "application/json");       
        
	//List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        
	//urlParameters.add(new BasicNameValuePair("\"event\"", "\"Test=123\""));
    	JSONObject json = new JSONObject();
        json.put("event", "test=123");   
        StringEntity params = new StringEntity(json.toString());
        post.setEntity(params);
        
       
        try
        {
	//post.setEntity(new UrlEncodedFormEntity(urlParameters));
       // post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

	HttpResponse response = client.execute(post);
	System.out.println("Response Code : " 
                + response.getStatusLine().getStatusCode());

	BufferedReader rd = new BufferedReader(
	        new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
        System.out.print (result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
        catch (Exception p)
    {
        p.printStackTrace();
    }
    }
    
}
