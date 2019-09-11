 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.dbmonitor;
import java.util.Random;
import com.mysql.jdbc.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
/**
 *
 * @author mwiser
 */
public class MonitoringThread extends Thread{
    public int iThreadID;
    public Targets mytarget;
    public String szGUID;
    public java.util.Hashtable hAccount;
    public MainConfig mc;
    public java.util.Vector vKPIs;
    public MonitoringThread(int x,String G, MainConfig mc2)
    {
     iThreadID=x;   
     szGUID = G;
     mc=mc2;
     
     //System.out.println (mytarget.szServerName);
     //System.out.println (mytarget.szDBName);
     Utils.heclog("info", szGUID, "Step=B, Note=\"Starting Thread with ID:"+x+"\"",mc);
    }
    public void run()
    {
        
        try
        {
        Random myr = new Random();
        int iwaiter = myr.nextInt(60);
        Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T1, Note=\"Waiting:"+iwaiter+" seconds until thread starts processing\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
        sleep(iwaiter*1000);
        int iMinuteCounter=0;
        while (1==1)
        //if (true)
        {   java.sql.Connection con =null; 
            String szExceptionKPIName="";
            boolean bSuccess=true;
            boolean bLoggedIn=false;
            iMinuteCounter++;
            try
            {
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T2, Note=\"Thread Waking Up and Connecting to DB\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                 
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T2A, Note=\"After Initializing Connection\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                if (mytarget.szType.equals("MySQL"))
                        {
             //   try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection("jdbc:mysql://"+mytarget.szServerName+":"+mytarget.iPort+"/"+mytarget.szDBName,mytarget.szUser,mytarget.szPassword);                   
                    bLoggedIn=true;
               //     } catch(Exception e3) {
                 //                           Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TEMySQL1, Note=\"Connection Exception: "+e3.getMessage()+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                   //                         bSuccess=false;
                     //                    }
                        }
                
               if (mytarget.szType.contains("ORCL"))
                        {
                //try {
                    Class.forName("oracle.jdbc.driver.OracleDriver"); 
                    System.out.println ("jdbc:oracle:thin:@"+mytarget.szServerName+":"+mytarget.iPort+"/"+mytarget.szDBName+" "+mytarget.szUser+" "+mytarget.szPassword);
                    //con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","admin","secret123");
                    con = DriverManager.getConnection("jdbc:oracle:thin:@"+mytarget.szServerName+":"+mytarget.iPort+"/"+mytarget.szDBName,mytarget.szUser,mytarget.szPassword);                   
                  //  } catch(Exception e3) {
                    //                        Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TEORCL1, Note=\"Connection Exception: "+e3.getMessage()+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                      //                      bSuccess=false;
                      bLoggedIn=true;
                        //                 }
                        } 
               if (mytarget.szType.contains("MSSQL"))
                        {
                /*try {
                      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                      String connectionUrl = "jdbc:sqlserver://"+mytarget.szServerName+":"+mytarget.iPort+";user="+mytarget.szUser+";password="+mytarget.szPassword+";";
                      con = DriverManager.getConnection(connectionUrl);                    
                    } catch(Exception e3) {
                                            Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TE, Note=\"Connection Exception: "+e3.getMessage()+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                                         }
                        } */
               // try {  
                       //System.out.println ("CA-   |"+mytarget.szUser+"|"+mytarget.szPassword+"|");     
                       //String url = "jdbc:jtds:sqlserver://"+mytarget.szServerName+":"+mytarget.iPort+";domain=CGUser;useNTLMv2=true;user="+mytarget.szUser+";password="+mytarget.szPassword+";integratedSecurity=true;";            
                       String url = new String();
                       if (mc.szDomain.equals("SQL"))
                       {
                       Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                       String connectionUrl = "jdbc:sqlserver://"+mytarget.szServerName+":"+mytarget.iPort+";user="+mytarget.szUser+";password="+mytarget.szPassword+";";
                       con = DriverManager.getConnection(connectionUrl);  
                       bLoggedIn=true;
                       }
                       else
                       {
                       url = "jdbc:jtds:sqlserver://"+mytarget.szServerName+":"+mytarget.iPort+";domain="+mc.szDomain+";useNTLMv2=true;user="+mytarget.szUser+";password="+mytarget.szPassword+";integratedSecurity=true;";             
                       System.out.println (url);
                       String driver = "net.sourceforge.jtds.jdbc.Driver";            
                       Class.forName(driver);             
                       con = DriverManager.getConnection(url);  
                       bLoggedIn=true;
                       }
                      // url = "jdbc:jtds:sqlserver://"+mytarget.szServerName+":"+mytarget.iPort+";domain="+mc.szDomain+";useNTLMv2=true;user="+mytarget.szUser+";password="+mytarget.szPassword+";integratedSecurity=true;";            
                       
                 //   } catch(Exception e3) {
                   //                         Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TEMSSQL1, Note=\"Connection Exception: "+e3.getMessage()+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                     //                       bSuccess=false;
                       //                  }
                        }
               if (mytarget.szType.contains("SYBASE"))
                        {               
              //  try {       
                       String url = "jdbc:jtds:sybase://"+mytarget.szServerName+":"+mytarget.iPort+";instance="+mytarget.szDBName+";user="+mytarget.szUser+";password="+mytarget.szPassword+";";
                       //String url = "jdbc:jtds:sqlserver://"+mytarget.szServerName+":"+mytarget.iPort+";domain=CGUser;useNTLMv2=true;user="+mytarget.szUser+";password="+mytarget.szPassword+";integratedSecurity=true;";            
                       String driver = "net.sourceforge.jtds.jdbc.Driver";            
                       Class.forName(driver);             
                       con = DriverManager.getConnection(url);   
                       bLoggedIn=true;
                //    } catch(Exception e3) {
                  //                          Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TESYBASE1, Note=\"Connection Exception: "+e3.getMessage()+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                    //                        bSuccess=false;
                      //                   }
                        } 
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T2B, Note=\"After setting ConnectionInfo\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T3, Note=\"Connection has been established successfully\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);                
                //System.exit(0);
                KPI mykpi = new KPI();
                int mymod = 0;
                for (int x2=0;x2<vKPIs.size();x2++)
                {
                  mykpi = (KPI) vKPIs.elementAt(x2);
                  szExceptionKPIName=mykpi.szName;
                  
                                            
                  mymod = iMinuteCounter%mykpi.iInterval;
                  //System.out.println (mykpi.szName+" "+mymod+" "+iMinuteCounter+" "+mykpi.iInterval);
                  //System.exit(0);
                  
                if ((mymod==0)&&(mykpi.szGroup.equals(mytarget.szType))) //add logig to loop through KPIs
                {
                            
                //try
                //{
                //mykpi=findKPI(vKPIs,mytarget.iKPI);    
                //if (mykpi.szName.equals(null))
                //    Utils.heclog("debug", szGUID, "Thread="+iThreadID+",Step=TE4, Note=\"Did not find KPI with ID:"+mytarget.iKPI+" in findKPI()\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
               // }
                //catch (Exception e4)
               // {
                 // Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TE4, Note=\"Did not find KPI with ID:"+mytarget.iKPI+" in findKPI()\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);  
               //   bSuccess=false;
               // }
            
                java.sql.Statement st = con.createStatement(); 
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T4, Note=\"Running SQL:"+mykpi.szSQL+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);                    
                java.sql.ResultSet rs = st.executeQuery(mykpi.szSQL);           
                //System.out.println("A-1");
                java.sql.ResultSetMetaData rsmd = rs.getMetaData();
                // System.out.println("A-2");
                //String szOut= new String();
                int columnCount = rsmd.getColumnCount();
              //   System.out.println("A-3");
                java.util.Vector vColumn = new java.util.Vector();
               //  System.out.println("A-4");
                
                for (int i = 1; i <= columnCount; i++ ) {
                                                            String name = rsmd.getColumnName(i);
                                                            name = name.replace(" ","_");
                                                            name = name.replace("%","X");
                                                            name = name.replace("(","_");
                                                            name = name.replace("*","X");
                                                            name = name.replace(")","_");
                                                            vColumn.add(name);     
                                                            // System.out.println("A-5 - will be repeat");
                                                        }   
               // System.out.println("A-6");
                //System.out.println("A-7"+rs.getRow()+"--");
                

                
                while (rs.next())
                    {
                       // System.out.println("A-8");
                        String szText = "";
                        for (int i = 1; i <= columnCount; i++ ) {
                                                String szVal = rs.getString(i);
                                                int iType = rsmd.getColumnType(i);
                                                //System.out.println (iType+" "+szVal);
                                                String szColTemp = new String();
                                                try
                                                {
                                                    szColTemp =  (String) vColumn.elementAt(i-1);
                                                    if (szColTemp.length()<1)
                                                    {
                                                       szColTemp = "Result"+i; 
                                                    }
                                                }
                                                catch (Exception e22)
                                                {
                                                    szColTemp = "SybaseResult"+i;
                                                }
                                                if (iType==2)
                                                    szText=szText+", "+szColTemp+"="+szVal;
                                                else
                                                    szText=szText+", "+szColTemp+"=\""+szVal+"\"";
                                               }   
                        szText=szText.substring(2);
                        //System.out.println (szText);
                        //System.exit(0);
                         Utils.heclog("debug", szGUID, "Thread="+iThreadID+",Step=T5, Note=\"SQL Result:"+szText+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
                         Utils.heclog("data", szGUID,"Thread="+iThreadID+",Step=Result,"+szText+", KPIName=\""+mykpi.szName+"\",Host="+mytarget.szServerName+", DB="+mytarget.szDBName+", DBPlatform="+mytarget.szType,mc);
                
                    }
            } 
            }
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T6, Note=\"Closing DB Connection\""+", KPIName=\""+mykpi.szName+"\",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);   
                con.close();                
                Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=T7, Note=\"Sleeping:60\""+", KPIName=\""+mykpi.szName+"\",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);   
                if (bSuccess)
                {
                  sleep (1000*60);                  
                }
                else
                  sleep (1000*60*15);
                //sleep 15 minutes on exceptions
            }
            catch (Exception ex)
        {
            ex.printStackTrace();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            String szAppend = new String();
            try
            {
              szAppend = ", KPIName=\""+szExceptionKPIName+"\""  ;
            }
            catch (Exception ee4)
            {
                
            }
            
            Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TE8, Note=\"Exception: "+ex.getMessage()+exceptionAsString+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName+szAppend,mc);
            bSuccess=false;
            if (bLoggedIn)
            {
              Utils.heclog("info", szGUID, "Thread="+iThreadID+",Step=TE8Step2, Note=\"Closing DB Connection after KPI Exception\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);   
              try
              {
              con.close();  
              }
              catch (Exception oi1)
              {
                  
              }
              Utils.heclog("debug", szGUID, "Thread="+iThreadID+",Step=TE8Step3, Note=\"Closing DB Connection after KPI Exception\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);   
            }
            Utils.heclog("debug", szGUID, "Thread="+iThreadID+",Step=TE8Step4, Note=\"Before Sleep\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName+szAppend,mc);            
            sleep (1000*60*15);
            Utils.heclog("debug", szGUID, "Thread="+iThreadID+",Step=TE8Step5, Note=\"After Sleep\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName+szAppend,mc);
        }
        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TE9, Note=\"Exception: "+e.getMessage()+"\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
            
        }
        
    }
    
  private KPI findKPI (java.util.Vector vKPIs, int iKPIID)
  {
      KPI myk = new KPI();
      for (int i=0;i<vKPIs.size();i++)
      {
          KPI myk2 = (KPI) vKPIs.elementAt(i);
          if (myk2.iMonitorID==iKPIID)
          {
              myk.iInterval = myk2.iInterval;
              myk.iMonitorID = iKPIID;
              myk.szGroup = myk2.szGroup;
              myk.szName = myk2.szName;
              myk.szSQL = myk2.szSQL;
          }
      }
      try
      {
          if (myk.szName.equals(null))
              Utils.heclog("debug", szGUID, "Thread="+iThreadID+",Step=TE10, Note=\"Did not find KPI with ID:"+iKPIID+" in findKPI()\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);                       
      }
      catch (Exception e)
      {
          Utils.heclog("error", szGUID, "Thread="+iThreadID+",Step=TE11, Note=\"Did not find KPI with ID:"+iKPIID+" in findKPI()\""+",Host="+mytarget.szServerName+", DB="+mytarget.szDBName,mc);
          e.printStackTrace();
      }
      return myk;
  }
}
