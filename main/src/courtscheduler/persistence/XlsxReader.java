package courtscheduler.persistence;

import courtscheduler.domain.*;
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
                team.setTeamId(teamId);
            }
            else if(columnCounter == 1)
                x = cell.toString();

            else if(columnCounter == 2){
                teamName = cell.toString();
                team.setTeamName(teamName);
            }
            else if(columnCounter == 3) {
                year =  cell.toString();
                team.setYear(year);
            }
            else if(columnCounter == 4) {
                gender = cell.toString();
                team.setGender(gender);
            }
            else if(columnCounter == 5){
                int index = cell.toString().indexOf(".");
                grade = Integer.parseInt(cell.toString().substring(0,index));
                team.setGrade(grade);
            }
            else if(columnCounter == 6){
                level = cell.toString();
                team.setLevel(level);
            }
            else if(columnCounter == 7){
                requests = cell.toString();
                //debug(team.getTeamId().toString()+":"+requests);
                processRequestConstraints(team, requests);
            }
            else if(columnCounter == 8)
                notSameTimeAs = cell.toString();









            //team.setRequests(requests);


            columnCounter+=1;
        }

        teamList.add(team);
    }

    private static void processRequestConstraints(Team team, String requests) {

        String splitToken = ","; // This needs to be updated with whatever Shane wants to use to separate the requests
        String[] requestArray = requests.split(splitToken);
        //debug(requests);

        List<MatchTime> offTimeList = new ArrayList<MatchTime>();//dateConstraint, deprecate
        List<Integer> playOnceTeamList = new ArrayList<Integer>();
        boolean likesDoubleHeaders = false;//flat bool
        boolean likesBackToBack = false;//flat bool
        DateConstraint badDates = team.getDateConstraint();
        PreferredDates prefDates = team.getPreferredDates();
        SharedTeams dontPlay=team.getSharedTeams();
        dontPlay.addSharedTeam(team.getTeamId());
        SharedTeams notSameTime= new SharedTeams();
        for(String request : requestArray) {

            // CANT PLAY ON CERTAIN DATE OR DATE RANGE //

            if(request.startsWith("xd")) {
                //parse the date and use it to create a new DateConstraint object
                String[] dates = request.split("-");
                if(dates[0].split("/").length<3){
					debug("Team" + team.getTeamId() + "xd constraint date 1 is too short.(" + request);
                }
                if(dates.length>1){
                    if(dates[1].split("/").length<3){
						debug("Team" + team.getTeamId() + "xd constraint date 2 is too short." + request);
                    }
                    badDates.addDates(badDates.findDateRange(dates[0],dates[1]));
                }
                else{
                    badDates.addDate(badDates.findDate(dates[0]));
                }
                team.setDateConstraint(badDates);

            }
            else if(request.contentEquals("")){

            }

            // CANT PLAY UNTIL AFTER CERTAIN TIME //
            else if(request.startsWith("after")){
                // incomplete; need to ensure that each time has pm or am so that it can be converted to military
                if(request.contains("pm") || request.contains("p.m.")
                        ||request.contains("am")||request.contains("a.m.")){

					debug("Team" + team.getTeamId() + "after constraint time has no am/pm." + request);
                }
                request.replace("after", "");
                request = getMilitaryTime(request);
                MatchTime offTime = new MatchTime("0:00", request);
                offTimeList.add(offTime);
                badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
            }

            // CANT PLAY UNTIL BEFORE CERTAIN TIME //
            else if(request.startsWith("before")){
                // incomplete; need to ensure that each time has pm or am so that it can be converted to military
                if(request.contains("pm") || request.contains("p.m.")
                    ||request.contains("am")||request.contains("a.m.")){

					debug("Team" + team.getTeamId() + "before constraint time has no am/pm." + request);
                }
                request.replace("before", "");
                request = getMilitaryTime(request);
                MatchTime offTime = new MatchTime(request, "0:00");
                offTimeList.add(offTime);
                badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
            }

            // CANT PLAY BETWEEN CERTAIN TIME //
            else if(request.startsWith("xr")) {
                String[] times = request.split("-");
                if(times[0].contains("pm") || times[0].contains("p.m.")
                        ||times[0].contains("am")||times[0].contains("a.m.")){

                    debug("Team"+team.getTeamId()+"xr constraint time has no am/pm on time 1."+request);
                }
                if(times[1].contains("pm") || times[1].contains("p.m.")
                        ||times[1].contains("am")||times[1].contains("a.m.")){

					debug("Team" + team.getTeamId() + "xr constraint time has no am/pm on time 2." + request);
                }
                times[0]=getMilitaryTime(times[0]);
                times[1]=getMilitaryTime(times[1]);
                MatchTime offTime = new MatchTime(times[0], times[1]);
                offTimeList.add(offTime);
                badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
            }

            // TEAM REQUEST TO PLAY ON DAY OTHER THAN PRIMARY DAY //
            else if(request.startsWith("pd")) {
                String[] dates = request.split("-");
                if(dates[0].split("/").length<3){
					debug("Team" + team.getTeamId() + "pd constraint date 1 is too short.(" + request);
                }
                if(dates.length>1){
                    if(dates[1].split("/").length<3){
                        debug("Team"+team.getTeamId()+"pd constraint date 2 is too short."+request);
                    }
                    prefDates.addDates(prefDates.findDateRange(dates[0],dates[1]));
                }
                else{
                    prefDates.addDate(prefDates.findDate(dates[0]));
                }
                team.setDateConstraint(prefDates);
            }
            //DONT PLAY THESE TEAMS
            else if(request.startsWith("xplay")) {
                //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
                request.replace("xplay", "");
                int index = request.indexOf(".");
                Integer teamId=null;
                try{
                    teamId = Integer.parseInt(request.substring(0,index));
                }
                catch(NumberFormatException nfe){
					debug("Team" + team.getTeamId() + "xplay constraint teamID error." + request);
                }
                dontPlay.addSharedTeam(teamId);
            }
            // TEAM REQUEST TO PLAY ANOTHER TEAM ONLY ONCE
            else if(request.startsWith("playOnce")) {
                //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
                request.replace("playOnce", "");
                int index = request.indexOf(".");
                Integer teamId = Integer.parseInt(request.substring(0,index));
                playOnceTeamList.add(teamId);
            }

            // DOUBLE HEADER PREFERENCE REQUEST (DEFAULTED TO false) //
            else if(request.startsWith("DH"))
                likesDoubleHeaders = true;

            // BACK TO BACK PREFERENCE REQUEST (DEFAULTED TO false) //
            else if(request.startsWith("B2B")){
                likesBackToBack = true;
            }
            else if(request.startsWith("nst")){
                request.replace("nst","");
                Integer teamId=null;
                try{
                    teamId=Integer.parseInt(request);
                }
                catch(NumberFormatException nfe){
                    debug("Team"+team.getTeamId()+"nst constraint teamId error."+request);
                }
                notSameTime.addSharedTeam(teamId);
            }

            else{
				debug("Unknown constraint:" + request);
            }

        }

        team.setOffTimes(new OffTimes(offTimeList));
        team.setPreferredDates(prefDates);
		team.setDateConstraint(badDates);
        team.setPlayOnceRequests(new PlayOnceRequests(playOnceTeamList));
        team.setDoubleHeaderPreference(new DoubleHeaderPreference(likesDoubleHeaders));
        team.setBackToBackPreference(new BackToBackPreference(likesBackToBack));
		team.setSharedTeams(dontPlay);
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

	private static void debug(String debugMessage) {
		System.out.println(debugMessage);
	}
}
