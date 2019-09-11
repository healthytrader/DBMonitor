/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.dbmonitor;
import java.io.*;
import java.util.*;
/**
 *
 * @author mwiser
 */

public class StartUp {
    static MainConfig mc=null;
    
    private static AccountInfo getCredentialsFor(String szAccount,MainConfig mc3,String[] args)
    {
        AccountInfo mya = new AccountInfo();
       // System.out.println ("Launching Call to:"+mc3.szCyberArkPath);
       Utils.heclog("info", args[1],"Launching Call to to Cyberark:"+mc3.szCyberArkPath+"\"",mc);
       try
        {
         //  System.out.println ("starting");            
           Runtime r = Runtime.getRuntime();
           Process p;     // Process tracks one external native process
           java.io.BufferedReader is;  // reader for output of process
           String line;            
           p = r.exec(mc3.szCyberArkPath+" "+szAccount);
           //System.out.println("In Main after exec");
           is = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
           int i=0;
            while ((line = is.readLine()) != null)
            {
                i++;
               // if (i==1)
                 //   mya.szAccount = line;
                if (i==1)
                    mya.szUser = line;
                if (i==2)
                    mya.szPassword = line;
                
            }
    
            //System.out.println("In Main after EOF");
            //System.out.flush();
    
            p.waitFor();  // wait for process to complete
           // System.exit(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       Utils.heclog("info", args[1],"Retrieved ID for account="+szAccount+",user="+mya.szUser+",pwdlength="+mya.szPassword.length(),mc);
        //mya.szAccount = szAccount;
        //mya.szPassword = "tibbtp24";
        //mya.szUser = "root";
        return mya;
    }
    
    public static MainConfig readfile (String path, String file)
    {
    mc = new MainConfig();        
    String szFileName = path+"/"+file+".enc";
    //System.out.println (szFileName);
    try
    {
        BufferedReader br = new BufferedReader(new FileReader(szFileName));
        String line = br.readLine();
        while (line != null)
            {
                //System.out.println (line);                
                if (line.startsWith("HEC="))
                    mc.szHECToken=line.substring(line.indexOf("=")+1);
                    //System.out.println (mc.szHECToken);
                if (line.startsWith("HECServer="))
                    mc.szServerPort=line.substring(line.indexOf("=")+1);
                    //System.out.println (mc.szHECToken);    
                if (line.startsWith("CyberArk="))
                    mc.szCyberArkPath=line.substring(line.indexOf("=")+1);
                if (line.startsWith("WindowsDomain="))
                    mc.szDomain=line.substring(line.indexOf("=")+1);
                if (line.startsWith("SuicideMode="))
                    mc.szSuicide=line.substring(line.indexOf("=")+1);
                line = br.readLine();
                
            }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    
    return mc;
    }
    
    
    public static void main(String[] args) {
        try
        {
        //String szStartString = "Starting up with: " ;   
        System.out.println ("Reading Config Files - V0.99aV2");
        Globals.vMessages = new java.util.Vector();
        MainConfig mc2 = readfile(args[0],args[1]);
        mc2.szSheet = args[2];
        mc=mc2;
        
        if (!args[3].equals("T"))
        {
           String szFile=args[0]+"/"+args[1]+".enc";           
           //No longer needed to delete the enc file
           /*try
            { 
                File f = new File(szFile);
                f.delete();
                Utils.bdebug=false;
            //Files.deleteIfExists(Paths.get(szFile)); 
            } 
           catch (Exception e)
           {
               e.printStackTrace();
           }*/
           
        }
        else
            Utils.bdebug=true;
       // System.out.println (mc2.szHECToken);
       // System.out.println (mc2.szServerPort);
     //   System.out.println (mc2.szToken);
//        System.out.println (Utils.decode(mc2.szToken));

        java.util.Vector vInputs = new java.util.Vector();
        java.util.Vector vTargets = new java.util.Vector();
        vInputs=SpreadsheetReader.getMonitoringInputs(args[0]+"/sheets/MonitorQueries.xls",args[1],mc2);
        
        //Suicide Thread
        try
        {
           if (mc.szSuicide.equals("No"))
               Utils.heclog("info", args[1],"No Suicide Mode",mc);
           else
           {
               Utils.heclog("info", args[1],"Suicide Mode:"+mc.szSuicide,mc);
               SuicideThread st = new SuicideThread(mc.szSuicide,mc,args[1]);
               st.start();
           }
        }
        catch (Exception eee)
        {
            
        }
        
        vTargets=SpreadsheetReader.getMonitoringTargets(args[0]+"/sheets/"+args[2],args[1],mc2);        
        Utils.heclog("info", args[1],"Monitoring_Targets="+vTargets.size()+", vInputs="+vInputs.size(),mc);
        //System.exit(0);
        java.util.Hashtable hAccountList = new java.util.Hashtable();
        
        for (int x=0; x<vTargets.size();x++)
        {
             Targets myt = (Targets) vTargets.elementAt(x);
             if (hAccountList.containsKey(myt.szAccount))
             {                 
                 Utils.heclog("info", args[1],"Already have credentials for this Account:"+myt.szAccount,mc);
                 AccountInfo ai = (AccountInfo) hAccountList.get(myt.szAccount);
                 myt.szPassword = ai.szPassword;
                 myt.szUser = ai.szUser;
                 
                 
             }
             else
             {
                 AccountInfo myacc = getCredentialsFor(myt.szAccount,mc2,args);
                 myt.szPassword = myacc.szPassword;
                 myt.szUser = myacc.szUser;
                 hAccountList.put(myt.szAccount,myacc);                
                 Utils.heclog("info", args[1],"Got new Account Info from CyberArk:"+myt.szAccount+" userid.length:"+myt.szUser.length()+" pwd.length:"+myt.szPassword.length(),mc);
             }
             
        }
       
        Globals.mc = mc;
        HECThread myhect = new HECThread();
        myhect.start();
        
        Utils.heclog("info", args[1],"Step=A, Note=\"Starting Thread Logic\"",mc);
        for (int x=0;x<vTargets.size();x++)
        {
             
            MonitoringThread mt = new MonitoringThread(x,args[1],mc);
            mt.iThreadID=x;            
            mt.mytarget = (Targets)vTargets.elementAt(x);
            mt.szGUID = args[1];
            mt.vKPIs = vInputs;
            mt.mc = mc;            
            mt.start();            
            
        }
        
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
     }
 
}
