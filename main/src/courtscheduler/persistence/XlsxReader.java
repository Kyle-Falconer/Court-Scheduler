package courtscheduler.persistence;

import courtscheduler.domain.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class XlsxReader {

	public static CourtSchedule readExcelFile(String filename) throws Exception {
		
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
        return generateCourtScheduleFromTeamList(teamList);
	}

    private static CourtSchedule generateCourtScheduleFromTeamList(List<Team> teamList) {
        CourtSchedule schedule = new CourtSchedule();
        schedule.setTeamList(teamList);
        //time
        MatchTime[] time = new MatchTime[24];
        for(int i=0;i<23;i++){
            MatchTime hourly= new MatchTime();
            hourly.setStartTime(i+":00");
            hourly.setEndTime(i+":50");
            time[i]=hourly;
        }
        schedule.setMatchTimeList(Arrays.asList(time));

        //date
        MatchDate[] date = new MatchDate[31];
        for(int i=0;i<31;i++){
            MatchDate day= new MatchDate();
            day.setDate("Oct."+i);
            switch(i%7){
                case(0):
                    day.setDayOfWeek(DayOfWeek.MONDAY);
                    break;
                case(1):
                    day.setDayOfWeek(DayOfWeek.TUESDAY);
                    break;
                case(2):
                    day.setDayOfWeek(DayOfWeek.WEDNESDAY);
                    break;
                case(3):
                    day.setDayOfWeek(DayOfWeek.THURSDAY);
                    break;
                case(4):
                    day.setDayOfWeek(DayOfWeek.FRIDAY);
                    break;
                case(5):
                    day.setDayOfWeek(DayOfWeek.SATURDAY);
                    break;
                case(6):
                    day.setDayOfWeek(DayOfWeek.SUNDAY);
            }
            date[i]=day;
        }
        schedule.setMatchDateList(Arrays.asList(date));

        //Match
        List<Match> matches= new ArrayList<Match>();
        Match match = new Match();
        for(int i=0; i<31;i++){
            for(int j=0;j<24;j++){
                for(int k=5;k<5;k++){
                    match.setMatchDate(date[i]);
                    match.setMatchTime(time[j]);
                    match.setCourtId(k);
                    matches.add(match);
                }
            }
        }
        schedule.setMatchList(matches);

        //conference
        List<Conference> conferences= new ArrayList<Conference>();
        for(int i=0;i<teamList.size();i++){
            for(int j=0;j<4;j++){
                Conference conference = new Conference();
                conference.setConference(j);
                conference.setTeam(teamList.get(i));
                conferences.add(conference);
            }
        }
        schedule.setConferenceList(conferences);
        //schedule.setGenderList();
        //schedule.setGradeList();
        //schedule.setLevelList();
        return schedule;
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
