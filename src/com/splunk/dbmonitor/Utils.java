/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.dbmonitor;
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
public class Utils {

public static boolean bdebug=false;

public static String decode(String enc) {
        String s = enc;
        String dec="";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            dec=dec+c;            
        }
        return dec;
    }

public static void heclog (String szSourceType, String szGUID,String szText,MainConfig mc)
{
    LogMsg mymsg = new LogMsg();
    mymsg.szGUID = szGUID;
    mymsg.szMsg = szText;
    mymsg.szSourcetype = szSourceType;
    //for (int x=0;x<500;x++)
   // {
    Globals.vMessages.add(mymsg);
    //}
    if (Utils.bdebug)
            System.out.println (new java.util.Date().toString()+" "+szSourceType+" "+szGUID+" "+szText);
    //heclogpost();
}

/*public static void heclogpost2 ()
{
    try
        {
        MainConfig mc = Globals.mc;     
        //if (szSourceType.equals("error")||szSourceType.equals("data")||bdebug)
       // {
        String url = "http://"+mc.szServerPort+"/services/collector";  
        //System.out.println (url);
          //      System.exit(0);
        HttpClient client = HttpClientBuilder.create().build();
	HttpPost post = new HttpPost(url);

	// add header
	post.setHeader("User-Agent", "DBMonitor");
        post.setHeader("Authorization", "Splunk "+mc.szHECToken);
        post.setHeader("Content-Type", "application/json");   
        String params2 = new String();
        int xcount1=Globals.vMessages.size();
        if (xcount1>5000)
            xcount1=5000;
    	for (int c1=0;c1<xcount1;c1++)
        {
         JSONObject json = new JSONObject();
         LogMsg mymsg = (LogMsg) Globals.vMessages.elementAt(0); 
         Globals.vMessages.removeElementAt(0);
         json.put("event", mymsg.szMsg+", Spreadsheet="+mc.szSheet+", GUID="+mymsg.szGUID);
         json.put("sourcetype", mymsg.szSourcetype);
         params2=params2+json.toString();
        }     
        StringEntity params = new StringEntity(params2);
        post.setEntity(params);
        try
        {

	HttpResponse response = client.execute(post);
	BufferedReader rd = new BufferedReader(
	        new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
        //System.out.println(line);
        rd.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //}
    }
        catch (Exception p)
    {
        p.printStackTrace();
    }
    }
    
    */
    
    
    
}



