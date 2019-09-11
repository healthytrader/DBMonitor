/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.dbmonitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

/**
 *
 * @author mwiser
 */
public class HECThread extends Thread{
    public HECThread()
    {
     System.out.println (new java.util.Date().toString()+" HEC Thread started");   
    }
    
 
public void heclogpost ()
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
   
    
    public void run()
    {
        int icounter=0;
        while (1==1)
        {
           try
           {
           //System.out.println ("LogMsg Vector size:"+Globals.vMessages.size());   
           heclogpost();
           sleep (1000);
           icounter++;
           if (icounter>10)
           {
             //System.out.println (new java.util.Date().toString()+"LogMsg Vector size:"+Globals.vMessages.size());
             Utils.heclog("debug", "", "LogMsg Vector size:"+Globals.vMessages.size(), Globals.mc);
             
             icounter=0;
           }
           
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }    
        }
    }        
}
