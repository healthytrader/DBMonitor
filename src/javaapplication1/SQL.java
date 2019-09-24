/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
import com.mysql.jdbc.*;


//impoirt dreamtrader.S25.*;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;


/**
 *
 * @author Administrator
 */
/**
 *
 * @author mwiser
 */
public class SQL {
static java.sql.Connection con =null;    
boolean bBacktest=true;
//boolean bBacktest=false;

    /** Creates a new instance of DBManager */
 
    public SQL(String[] args) {
        try {
            
            /*
            //Working
             System.out.println (0);            
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             System.out.println (1);
             String connectionUrl = "jdbc:sqlserver://w014379:1590;user=OZ_Splunk_DB_Svc;password=P@$$4sQLDBCo&&ect;";
             System.out.println(connectionUrl);
             System.out.println (2);
             con = DriverManager.getConnection(connectionUrl);  
             System.out.println (3);*/
            
            //jdbc:jtds:sqlserver://x.x.x.x/database
            //String url = "jdbc:jtds:sqlserver://w014379:1590;databaseName=tempdb;domain=CGUser;useNTLMv2=true;user=OZ_Splunk_DB_Svc;password=P@$$4sQLDBCo&&ect;integratedSecurity=true;";

           // String url = "jdbc:jtds:sqlserver://w534108:1590;domain=CGUser;useNTLMv2=true;user=OZ_Splunk_DB_Svc;password=P@$$4rsQLDBCo&&ect;integratedSecurity=true;";
            
            //String url = "jdbc:jtds:sqlserver://w534108:1590;domain=CGUser;user=OZ_Splunk_DB_Svc;password=P@$$4sQLDBCo&&ect;integratedSecurity=true;";
            
            //String url = "jdbc:jtds:sqlserver://w014379:1590/CGSQL;user=OZ_Splunk_DB_Svc;password=P@$$4sQLDBCo&&ect;integratedSecurity=true;";
           //X898267	5100	oz_splunk_db_svc	GetTHeR3aLPa33!	NA	SQL_X898267		Sybase 
           // String driver = "net.sourceforge.jtds.jdbc.Driver";
            //String url = "jdbc:jtds:sybase://X898267:5100/SQL_X898267";
            //String url = "jdbc:jtds:sybase://X898267:5100;instance=SQL_X898267;user=oz_splunk_db_svc;password=GetTHeR3aLPa33!";
           // System.out.println (url);
           /* try
            {
                System.out.println ("Checking for URL Override");
                url = args[0];
                System.out.println (url);
                        
            }
            catch (Exception e)
            {
                
            }*/

            // con = DriverManager.getConnection(url);
            //String userName = "OZ_Splunk_DB_Svc";
            //String password = "P@$$4sQLDBCo&&ect";
            //IntegratedSecurity=true
         /*    System.out.println ("0b"); 
             Class.forName(driver);
             System.out.println(url); 
             System.out.println (2);
             con = DriverManager.getConnection(url);  
             System.out.println (3);
            
            
            
            
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //con = DriverManager.getConnection("jdbc:mysql:///","OZ_Splunk_DB_Svc", "P@$$4sQLDBCo&&ect"); 
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    //      String connectionUrl = "jdbc:sqlserver://w014379:1590" +  
  // "data//baseName=AdventureWorks;user=MyUserName;password=*****;";  
        //Connection con = DriverManager.getConnection(connectionUrl);  
        /*
        System.out.println (1+" L");
        Class.forName("oracle.jdbc.driver.OracleDriver"); 
        //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println (2);
        String url = "jdbc:oracle:thin:@x654898:1525/X654898";
        con = DriverManager.getConnection(url,"OZ_SPLUNK_DB_SVC","GetTHeR3aLPa33!");
         System.out.println (url);
            try
            {
                //System.out.println ("Checking for URL Override");
                //url = args[0];
                System.out.println (url);
                        
            }
            catch (Exception e)
            {
                
            }
        */
        
        //con=DriverManager.getConnection(url);  
        //String connectionUrl = "jdbc:sqlserver://database-2.chnq5lb3ureq.us-east-1.rds.amazonaws.com:1433;user=admin;password=secret123;";
        //con = DriverManager.getConnection(connectionUrl);  
        //System.out.println (3);
        //Class.forName("com.ibm.db2.jcc.DB2Driver");
        //String url = "jdbc:db2://localhost:50000/sample";
                                          // Set URL for data source
        //String user = "db2inst1";
        //String password = "tibbtp24";
        //con = DriverManager.getConnection(url, user, password); 
                                          // Create connection
       // Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/?user=postgres&password=abc";
        Properties props = new Properties();
        
//props.setProperty("ssl","true");
        con = DriverManager.getConnection(url, props);                                 
        
        
    } catch(Exception e) {
     e.printStackTrace();
      System.err.println("Exception: " + e.getMessage());
    } 
    }
        
public void closeconnection()
{
    try
    {
        con.close();
    }
    catch (Exception pl)
    {
        pl.printStackTrace();
    }
}
    
    
    
     public static void main(String[] args) {
        // TODO code application logic here
        SQL mys = new SQL(args);
        try
        {
            System.out.println ("starting up");
         java.sql.Statement st = con.createStatement();  
      
      //java.sql.ResultSet rs = st.executeQuery("SELECT * FROM TABLE(MON_GET_WORKLOAD('',-2)) AS t");
       //java.sql.ResultSet rs = st.executeQuery("SELECT * from SYSIBMADM.MON_CURRENT_SQL");
       //java.sql.ResultSet rs = st.executeQuery("select * from sysibmadm.snapdb");
      // java.sql.ResultSet rs = st.executeQuery("select * from SYSIBMADM.SNAPDB_MEMORY_POOL");
      // java.sql.ResultSet rs = st.executeQuery("SELECT * FROM TABLE(MON_GET_CONNECTION(cast(NULL as bigint),-2)) AS t");
     //java.sql.ResultSet rs = st.executeQuery("SELECT * FROM TABLE(WLM_GET_WORKLOAD_OCCURRENCE_ACTIVITIES_V97(cast(NULL as bigint), -1)) AS T");
      java.sql.ResultSet rs = st.executeQuery("select * from pg_stat_activity;");
      java.sql.ResultSetMetaData rsmd = rs.getMetaData();
      String szOut= new String();
      int columnCount = rsmd.getColumnCount();
      java.util.Vector vColumn = new java.util.Vector();
      String szText = "";
      for (int i = 1; i <= columnCount; i++ ) {
                                                String name = rsmd.getColumnName(i);
                                                vColumn.add(name);                                                
                                               }   

      while (rs.next())
      {
       for (int i = 1; i <= columnCount; i++ ) {
                                                String szVal = rs.getString(i);
                                               szText=szText+", "+(String) vColumn.elementAt(i-1)+"="+szVal;
                                               }   
       
       //System.out.println (szText);
      }
      System.out.println (szText);
      
       //System.out.println (rs.getString(1));
      //System.exit(0);
//      while (rs.next())

       // */
       // while (1==1)
     //   {
       //     Thread.sleep(1000);
        }
        //}
        catch (Exception e)
        {
            e.printStackTrace();
        }
     }
}
