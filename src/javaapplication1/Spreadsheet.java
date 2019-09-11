/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
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
//import org.apache.poi.ss.usermodel.Cell;
/**
 *
 * @author mwiser
 */
public class Spreadsheet {
       public static void main(String[] args) {
        // TODO code application logic here
        String excelFilePath = "Customers.xls";
        String password = "7xseven";
        try
        {
          Workbook workbook = WorkbookFactory.create(new File(excelFilePath), password);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
 
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
 
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();                     
                    System.out.print(cell.getStringCellValue());
                    
                    }
                    System.out.print("\t");
                }
                System.out.println();
            
 
            workbook.close();  
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       }
}
