/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.dbmonitor;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
 
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Vector;
/**
 *
 * @author mwiser
 */
public class SpreadsheetReader {
    public static Vector getMonitoringInputs(String szFileName,String szGUID,MainConfig mc)
    {
    
        String excelFilePath = szFileName;
        //String password = szPass;
        Utils.heclog("info",szGUID,"Step=1, Note=\"Opening Spreadsheet:"+szFileName+"\"",mc);
        java.util.Vector vReturn = new java.util.Vector();
        
        
        try
        {
          Workbook workbook = WorkbookFactory.create(new File(excelFilePath));
          //System.out.println (workbook.getName(szGUID).toString());
          //System.exit(0);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
 
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                KPI mykpi = new KPI();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
 
                while (cellIterator.hasNext()) {
                    
                    Cell cell = cellIterator.next();  
                                        
                    if (cell.getRowIndex()>=1)
                    {
                        if (cell.getColumnIndex()==0)
                        {
                            mykpi.iMonitorID = (int)cell.getNumericCellValue();
                            //System.out.println (mykpi.iMonitorID);
                        }
                        if (cell.getColumnIndex()==1)
                        {
                            mykpi.szName = cell.getStringCellValue();                            
                        }
                        if (cell.getColumnIndex()==2)
                        {
                            mykpi.iInterval = (int)cell.getNumericCellValue();                            
                        }
                        if (cell.getColumnIndex()==3)
                        {
                            mykpi.szGroup = cell.getStringCellValue() ;                           
                        }
                        if (cell.getColumnIndex()==4)
                        {
                            mykpi.szSQL = cell.getStringCellValue();                            
                        }                    
                    }
                    
                    }
                    try
                    {
                    if (!mykpi.szName.equals(null))
                    {
                    Utils.heclog("debug", szGUID, "MonitorID="+mykpi.iMonitorID+", MonitorName=\""+mykpi.szName+"\", Interval="+mykpi.iInterval+", SQL=\""+mykpi.szSQL+"\", Group="+mykpi.szGroup,mc);
                    vReturn.add(mykpi);
                    }
                    } 
                    catch (Exception e)
                    {
                        
                    }
                }
//                System.out.println();
            
 
            workbook.close();  
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return vReturn;
    }
 public static Vector getMonitoringTargets(String szFileName,String szGUID,MainConfig mc)
    {
    
        String csvFile = szFileName;        
        Utils.heclog("info",szGUID,"Step=2, Note=\"Opening CSVFile:"+szFileName+"\"",mc);
        java.util.Vector vReturn = new java.util.Vector();                
        try
        {
            
         java.io.BufferedReader br = null;
         String line = "";
         br = new java.io.BufferedReader(new java.io.FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                try
                {
                // use comma as separator
                String[] CSVList = line.split(",");
                //System.out.println (CSVList[0]+"---"+CSVList[1]);
                         //.split(cvsSplitBy);
                         Targets myt = new Targets();
                         myt.szServerName = CSVList[0];
                         myt.iPort = new Integer(CSVList[1]).intValue();
                         myt.szAccount= CSVList[2];
                         myt.szDBName= CSVList[3];
                         myt.szType= CSVList[4];
                         vReturn.add(myt);                         
                }
                catch (Exception e2)
                {
                    
                }
               

            }           
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return vReturn;
    }
    
}
