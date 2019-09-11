/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.dbmonitor;

/**
 *
 * @author mwiser
 */
public class SuicideThread extends Thread{
 String szDate;
 java.util.Date dSuicideDate;
 String szGUID;
 MainConfig mc;
    public SuicideThread(String szInput,MainConfig mc2,String szGUID2)
    {
        szDate = szInput;
        szGUID = szGUID2;
        mc = mc2;
        String szH = szDate.substring(0,szDate.indexOf(":"));
        String szM = szDate.substring(szDate.indexOf(":")+1);
        
        dSuicideDate = new java.util.Date();
        dSuicideDate.setHours(new Integer(szH).intValue());
        dSuicideDate.setMinutes(new Integer(szM).intValue());
        
        java.util.Date dNow = new java.util.Date();
        if (dNow.after(dSuicideDate))
        {
            long l1 = dSuicideDate.getTime();           
            l1 = l1 + 24*1000*3600;            
            dSuicideDate = new java.util.Date(l1);
        }
        //dSuicideDate.set
        Utils.heclog("info", szGUID, "Step=XX1, Note=\"Starting Suicide Thread to terminate at:"+dSuicideDate.toString()+"\"",mc);
        
        
    }
    public void run()
    {
        while (1==1)
        {
            try
            {
                java.util.Date dRightNow = new java.util.Date();
                //double dDiff = dRightNow.getTime()-dSuicideDate.getTime();
                //System.out.println (dDiff+"XXX");
                if (dRightNow.after(dSuicideDate))
                {
                  Utils.heclog("info", szGUID, "Step=XX2, Note=\"Executing Suicide Thread to terminate at:"+dSuicideDate.toString()+"\"",mc);  
                  System.exit(0);
                }
                else
                    sleep (60*1000);
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
