package courtscheduler.persistence;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import courtscheduler.persistence.Team;

public class XlsxReader {

	public XlsxReader(String filename) throws Exception {
		
		File file = new File(filename);
	    FileInputStream fis = new FileInputStream(file);
	    XSSFWorkbook wb = new XSSFWorkbook(fis);
	    
	    File outFile = new File("text.csv");
	    BufferedWriter bw = null;
	    //FileOutputStream out = null;

	    // Get worksheet by name
	    //XSSFSheet sh = wb.getSheet("<worksheet name>");
	    
	    // Get worksheet by index
	    XSSFSheet sh = wb.getSheetAt(0);
	    
	    Integer rowCounter = 2;
	    Integer rowCount = sh.getLastRowNum();
        ArrayList<Team> teamList = new ArrayList<Team>();
	    
	    Integer columnCounter = 0;
	    //short columnCount = null;

	    System.out.println(new java.util.Date() + "[INFO] Worksheet Name: " + sh.getSheetName());
	    System.out.println(new java.util.Date() + "[INFO] Worksheet has " + (rowCount - 1) + " lines of data.");

	    
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

                Integer teamId = null;
                String x = "";
                String teamName = "";
                String year = "";
                String gender = "";
                Integer grade = null;
                String level = "";
                String requests = "";
                String notSameTimeAs = "";
                Team team = new Team();
			    
			    while(columnCounter < columnCount){
			    	
			    	Cell cell = currentRow.getCell(columnCounter);

			    	
			    	if(columnCounter == 0){
                        int index = cell.toString().indexOf(".");
                        teamId = Integer.parseInt(cell.toString().substring(0,index));
                    }
                    else
			    		bw.write(",\"" + cell.toString() + "\"");


                    if(columnCounter == 1)
                        x = cell.toString();
                    if(columnCounter == 2)
                        teamName = cell.toString();
                    if(columnCounter == 3)
                        year =  cell.toString();
                    if(columnCounter == 4)
                        gender = cell.toString();
                    if(columnCounter == 5){
                        int index = cell.toString().indexOf(".");
                        grade = Integer.parseInt(cell.toString().substring(0,index));
                    }
                    if(columnCounter == 6)
                        level = cell.toString();
                    if(columnCounter == 7)
                        requests = cell.toString();
                    if(columnCounter == 8)
                        notSameTimeAs = cell.toString();

                    team.setTeamId(teamId);
                    team.setX(x);
                    team.setTeamName(teamName);
                    team.setYear(year);
                    team.setGender(gender);
                    team.setGrade(grade);
                    team.setLevel(level);
                    team.setRequests(requests);
                    team.setNotSameTimeAs(notSameTimeAs);

			    	
			    	columnCounter+=1;
			    }

                teamList.add(team);
		    	
		    	rowCounter+=1;
		    }

            for(int x = 0; x < 7; x++)
                System.out.println(teamList.get(x));

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

}
