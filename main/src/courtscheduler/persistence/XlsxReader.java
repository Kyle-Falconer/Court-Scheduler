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
import java.util.Calendar;
import java.util.List;

public abstract class XlsxReader {

    private static ArrayList<Team> teamList = new ArrayList<Team>();

	public static CourtSchedule readExcelFile(String filename) throws Exception {
		
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

        return generateCourtScheduleFromTeamList(teamList);
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
            DateConstraint badDates = team.getDateConstraint();
            if(request.startsWith("xd")) {
                //parse the date and use it to create a new DateConstraint object
                String[] dates = request.split("-");

                if(dates.length>1){
                    badDates.addRestrictedDates(badDates.findDateRange(dates[0],dates[1]));
                }
                else{
                    badDates.addRestrictedDate(badDates.findDate(dates[0]));
                }
                team.setDateConstraint(badDates);

            }

            // CANT PLAY UNTIL AFTER CERTAIN TIME //
            if(request.startsWith("after")){
                // incomplete; need to ensure that each time has pm or am so that it can be converted to military
                request.replace("after", "");
                request = getMilitaryTime(request);
                MatchTime offTime = new MatchTime("0:00", request);
                offTimeList.add(offTime);
                badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
            }

            // CANT PLAY UNTIL BEFORE CERTAIN TIME //
            if(request.startsWith("before")){
                // incomplete; need to ensure that each time has pm or am so that it can be converted to military
                request.replace("before", "");
                request = getMilitaryTime(request);
                MatchTime offTime = new MatchTime(request, "0:00");
                offTimeList.add(offTime);
                badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
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

    private static CourtSchedule generateCourtScheduleFromTeamList(List<Team> teamList) {
        CourtSchedule schedule = new CourtSchedule();
        schedule.setTeamList(teamList);
        //time
        MatchTime[] time = new MatchTime[24];
        for(int i=0;i<24;i++){
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
            Calendar cal = Calendar.getInstance();
            cal.set(2013, 9, i);
            day.setCal(cal);
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

        for(int i=0; i<31;i++){
			for(int j=0;j<24;j++){
                for(int k=1;k<6;k++){
					Match match = new Match();
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
		schedule.setMatchAssignmentList(new ArrayList<MatchAssignment>());
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
