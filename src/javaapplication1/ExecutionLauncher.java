/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author mwiser
 */
public class ExecutionLauncher {
 
      public static void main(String[] args) {
          
        // TODO code application logic here
        try
        {
           System.out.println ("starting");            
           Runtime r = Runtime.getRuntime();
           Process p;     // Process tracks one external native process
           java.io.BufferedReader is;  // reader for output of process
           String line;            
           p = r.exec("python /Users/mwiser/dbmonitor/startme.py");
           System.out.println("In Main after exec");
           is = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
           int i=0;
            while ((line = is.readLine()) != null)
            {
                i++;
                System.out.println(line+" "+i);
            }
    
            System.out.println("In Main after EOF");
            System.out.flush();
    
            p.waitFor();  // wait for process to complete
   
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
      }
}
