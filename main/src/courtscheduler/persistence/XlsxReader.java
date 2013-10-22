package courtscheduler.persistence;

import courtscheduler.domain.MatchDate;
import courtscheduler.domain.MatchTime;
import courtscheduler.domain.Team;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class XlsxReader {

    private static ArrayList<Team> teamList = new ArrayList<Team>();
    private String filename;

    public XlsxReader  (String filename){
        this.filename = filename;
    }

	public ArrayList<Team> readExcelFile() throws Exception {
		
		File file = new File(filename);
	    FileInputStream fis = new FileInputStream(file);
	    XSSFWorkbook wb = new XSSFWorkbook(fis);
	    
	    // Get worksheet by index
	    XSSFSheet sh = wb.getSheetAt(0);
	    
	    Integer rowCounter = 2;
	    Integer rowCount = sh.getLastRowNum();

	    System.out.println(new java.util.Date() + "[INFO] Worksheet Name: " + sh.getSheetName());
	    System.out.println(new java.util.Date() + "[INFO] Worksheet has " + (rowCount - 1) + " lines of data.");
		    
        while (rowCounter <= rowCount){
            Row currentRow = sh.getRow(rowCounter);
            processRow(currentRow);
            rowCounter+=1;
        }

        for(int x = 0; x < 7; x++)
            System.out.println(teamList.get(x));

        System.out.println(new java.util.Date() + "[INFO] Processing finished."); // Converted document: \"test.csv\"");

        return teamList;
	}

    private static void processRow(Row currentRow) {

        short columnCount = currentRow.getLastCellNum();
        int columnCounter = 0;

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
            team.setTeamName(teamName);
            team.setYear(year);
            team.setGender(gender);
            team.setGrade(grade);
            team.setLevel(level);

            processRequestConstraints(team, requests);
            //team.setRequests(requests);


            columnCounter+=1;
        }

        teamList.add(team);
    }

    private static void processRequestConstraints(Team team, String requests) {

        String splitToken = ":"; // This needs to be updated with whatever Shane wants to use to separate the requests
        String[] requestArray = requests.split(splitToken);

        List<MatchDate> offDateList = new ArrayList<MatchDate>();
        List<MatchTime> offTimeList = new ArrayList<MatchTime>();
        List<MatchDate> preferredDateList = new ArrayList<MatchDate>();
        List<Integer> playOnceTeamList = new ArrayList<Integer>();
        List<Integer> sharedTeamList = new ArrayList<Integer>();
        boolean likesDoubleHeaders = false;
        boolean likesBackToBack = false;

        for(String request : requestArray) {

            // CANT PLAY ON CERTAIN DATE OR DATE RANGE //
            if(request.startsWith("xd")) {
                //parse the date and use it to create a new MatchDate object
                MatchDate offDate = new MatchDate();
                offDateList.add(offDate);
            }

            // CANT PLAY UNTIL AFTER CERTAIN TIME //
            if(request.startsWith("after")){
                // incomplete; need to ensure that each time has pm or am so that it can be converted to military
                request.replace("after", "");
                request = getMilitaryTime(request);
                MatchTime offTime = new MatchTime("0:00", request);
                offTimeList.add(offTime);
            }

            // CANT PLAY UNTIL BEFORE CERTAIN TIME //
            if(request.startsWith("before")){
                // incomplete; need to ensure that each time has pm or am so that it can be converted to military
                request.replace("before", "");
                request = getMilitaryTime(request);
                MatchTime offTime = new MatchTime(request, "0:00");
                offTimeList.add(offTime);
            }

            // CANT PLAY BETWEEN CERTAIN TIME //
            if(request.startsWith("xr")) {
                MatchTime offTime = new MatchTime("0:00", "0:00");
                offTimeList.add(offTime);
            }

            // TEAM REQUEST TO PLAY ON DAY OTHER THAN PRIMARY DAY //
            if(request.startsWith(" ")) {
                //parse the date and use it to create a new MatchDate object
                MatchDate preferredDate = new MatchDate();
                preferredDateList.add(preferredDate);
            }

            // TEAM REQUEST TO PLAY ANOTHER TEAM ONLY ONCE
            if(request.startsWith(" ")) {
                //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
                Integer teamId = null;
                playOnceTeamList.add(teamId);
            }

            // DOUBLE HEADER PREFERENCE REQUEST (DEFAULTED TO false) //
            if(request.startsWith("DH"))
                likesDoubleHeaders = true;

            // BACK TO BACK PREFERENCE REQUEST (DEFAULTED TO false) //
            if(request.startsWith("B2B"))
                likesBackToBack = true;
        }

        team.setOffDateList(offDateList);
        team.setOffTimeList(offTimeList);
        team.setPreferredDateList(preferredDateList);
        team.setPlayOnceTeamList(playOnceTeamList);
        team.setLikesDoubleHeaders(likesDoubleHeaders);
        team.setLikesBackToBack(likesBackToBack);
    }

    private static String getMilitaryTime(String time) {

        if(time.contains("pm") || time.contains("p.m.")) {
            time = time.replace("pm", "");
            time = time.replace("p.m.", "");

            try {
                Integer timeInt = Integer.parseInt(time);
                timeInt += 12;
                return timeInt.toString();
            }catch (NumberFormatException e) {
                return "";
            }
        }else {
            time = time.replace("am", "");
            time = time.replace("a.m.", "");

            return time;
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
