package com.gbt.xlsx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxReader {

	public XlsxReader() throws Exception {
		
		File file = new File("test.xlsx");   
	    FileInputStream fis = new FileInputStream(file);
	    XSSFWorkbook wb = new XSSFWorkbook(fis);
	    
	    File outFile = new File("text.csv");
	    BufferedWriter bw = null;
	    //FileOutputStream out = null;

	    // Get worksheet by name
	    //XSSFSheet sh = wb.getSheet("<worksheet name>");
	    
	    // Get worksheet by index
	    XSSFSheet sh = wb.getSheetAt(0);
	    
	    Integer rowCounter = 0;
	    Integer rowCount = sh.getLastRowNum();
	    
	    Integer columnCounter = 0;
	    //short columnCount = null;
	    
	    System.out.println(new java.util.Date() + "[INFO] Worksheet Name: "+sh.getSheetName());
	    System.out.println(new java.util.Date() + "[INFO] Worksheet has " + (rowCount+1) + " lines of data.");
	    
	    System.out.println("\t[C0]\t[C1]\t[C2]");
	    
	    // This is nice to dynamically get your column count, but it sets the 
	    // worksheet row and column index to the end.
	    //short columnWidth = getColumnWidth(file);
	    //for(int i = 0; i < columnWidth; i++ ){
	    //	System.out.print("\t[C"+i+"]");
	    //}
	    //System.out.println();
	    
	    try{
		    if(rowCount > 0 ) {
		    	if(outFile.exists()){
		    		outFile.delete();
			    	outFile.createNewFile();	
		    	}
		    	
		    	bw = new BufferedWriter(new OutputStreamWriter(
		    	          new FileOutputStream(outFile), "utf-8"));
		    	//out = new FileOutputStream(outFile);
		    }
		    
		    while (rowCounter <= rowCount){
		    	//System.out.println(new java.util.Date() + "[INFO] Processing row " + rowCounter);
			    
		    	//Row row = sh.getRow(1);
			    Row currentRow = sh.getRow(rowCounter);
			    short columnCount = currentRow.getLastCellNum();
			    columnCounter = 0;
			    
			    while(columnCounter < columnCount){
			    	
			    	Cell cell = currentRow.getCell(columnCounter); 
			    	
			    	if(columnCounter == 0)
			    		System.out.print("[R"+rowCounter+"]");
			    	
			    	//String value = currentRow.getCell(i).toString();
			    	//System.out.println("currentRow.getCell(0).getNumericCellValue="+currentRow.getCell(i));
			    	System.out.print("\t" + cell.toString());
			    	
			    	if(columnCounter == 0)
			    		bw.write("\"" + cell.toString() + "\"");
			    	else 
			    		bw.write(",\"" + cell.toString() + "\"");
			    	
			    	if(columnCounter == (columnCount-1)){
			    		bw.write("\n");
			    		System.out.println();
			    	}
			    	
			    	columnCounter+=1;
			    }	    	    
		    	
		    	rowCounter+=1;
		    }	    
		    
		    System.out.println(new java.util.Date() + "[INFO] Processing finished. Converted document: \"test.csv\"");	
		    
	    }finally {
	    	if ( bw != null )
	    		try{
	    			bw.close();
	    		}catch(Exception e){/* do nothing */}
	    	
		    if(rowCount > 0){
		    	Runtime runtime = Runtime.getRuntime();
		    	Process process = runtime.exec("/Windows/write.exe " + outFile.getAbsolutePath());
		    }
	    }
	    
	}

	public short getColumnWidth(File file) throws Exception {
		
	    FileInputStream fis = new FileInputStream(file);
	    XSSFWorkbook wb = new XSSFWorkbook(fis);
	    
	    // Get worksheet by index
	    XSSFSheet sh = wb.getSheetAt(0);
	    
	    short columnWidth = 0;
		Integer rowCounter = 0;
	    Integer rowCount = sh.getLastRowNum();    
	    
	    while (rowCounter <= rowCount){
	    	Row currentRow = sh.getRow(rowCounter);
	    	short columnCount = currentRow.getLastCellNum();
	    	
	    	if(columnCount > columnWidth)
	    		columnWidth = columnCount;
	    }
	    
	    return columnWidth;
	}

	public static void main(String[] args) throws Exception {
		new XlsxReader();
	}

}
