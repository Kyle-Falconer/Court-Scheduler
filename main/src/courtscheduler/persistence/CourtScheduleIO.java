package courtscheduler.persistence;

import courtscheduler.domain.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.util.StringUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.*;

public class CourtScheduleIO {


    private List<Match> matchList;
    private static List<Team> teamList;

    public CourtScheduleIO(){
        matchList = new ArrayList<Match>();
        teamList = new ArrayList<Team>();
    }


    public List<Team> readXlsx(String filename)throws Exception {


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
			if (currentRow != null && currentRow.getLastCellNum() > 0)
            	processRow(currentRow);
            rowCounter+=1;
        }

        for(int x = 0; x < 7; x++)
            System.out.println(teamList.get(x));

        System.out.println(new java.util.Date() + "[INFO] Processing finished."); // Converted document: \"test.csv\"");

        return teamList;
    }


    public void writeXlsx(List<Match> matches, LocalDate firstDay, String filepath) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Conference 1");
        //Create a new row in current sheet
        int rowNumber = 0;
        int cellNumber = 0;
        Row header = sheet.createRow(rowNumber);
        //cell.setCellValue("CourtScheduler");
        header.createCell(0).setCellValue("TEAM");
        header.createCell(1).setCellValue(" ");
        header.createCell(2).setCellValue("OPPONENT");
        header.createCell(3).setCellValue("DAY");
        header.createCell(4).setCellValue("DATE");
        header.createCell(5).setCellValue("TIME");
        header.createCell(6).setCellValue("COURT");

        for(Match match : matches) {
            cellNumber = 0;
            rowNumber++;
            Row dataRow = sheet.createRow(rowNumber);

            String teamName1 = match.getTeam1().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName1);

            String vs = "vs.";
            dataRow.createCell(cellNumber++).setCellValue(vs);

            String teamName2 = match.getTeam2().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName2);

			// date
			Integer matchDateIndex = match.getMatchSlot().getDay();
			LocalDate matchDate = firstDay.plusDays(matchDateIndex);
            String day = matchDate.dayOfWeek().getAsText();
            dataRow.createCell(cellNumber++).setCellValue(day);
            dataRow.createCell(cellNumber++).setCellValue(matchDate.toString());

            Integer matchTime = match.getMatchSlot().getTime();
            dataRow.createCell(cellNumber++).setCellValue(matchTime);

            Integer courtId = match.getMatchSlot().getCourt();
            dataRow.createCell(cellNumber++).setCellValue(courtId);

        }

        try {
            FileOutputStream out =
                    new FileOutputStream(new File(filepath));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully to " + filepath + ".");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
		MatchAvailability availability = team.getAvailability();
        DateConstraint badDates = team.getBadDates();
        DateConstraint prefDates = team.getPreferredDates();
        SharedTeams dontPlay=team.getSharedTeams();
        dontPlay.addSharedTeam(team.getTeamId());
        SharedTeams notSameTime= new SharedTeams();
        for(String request : requestArray) {

            // CANT PLAY ON CERTAIN DATE OR DATE RANGE //
            request=request.toLowerCase();
            request=request.trim();
            if(request.startsWith("xd")) {
                badDates=requestOffDate(request,team,badDates);
            }
            else if(request.contentEquals("")){
            }

            // CANT PLAY UNTIL AFTER CERTAIN TIME //
            else if(request.startsWith("after")){
                badDates=requestAfterTime(request, team, badDates);
            }

            // CANT PLAY UNTIL BEFORE CERTAIN TIME //
            else if(request.startsWith("before")){
                badDates=requestBeforeTime(request,team,badDates);
            }

            // CANT PLAY BETWEEN CERTAIN TIME //
            else if(request.startsWith("xr")) {
                badDates=requestOffTime(request, team, badDates);
            }

            // TEAM REQUEST TO PLAY ON DAY OTHER THAN PRIMARY DAY //
            else if(request.startsWith("pd")) {
                prefDates=requestPreferredDate(request,team,prefDates);
            }
            //DONT PLAY THESE TEAMS
            else if(request.startsWith("xplay")) {
                dontPlay=requestDontPlay(request, team, dontPlay);
            }
            // TEAM REQUEST TO PLAY ANOTHER TEAM ONLY ONCE
            else if(request.startsWith("playonce")) {
                playOnceTeamList=requestPlayOnce(request, team,playOnceTeamList);
            }

            // DOUBLE HEADER PREFERENCE REQUEST (DEFAULTED TO false) //
            else if(request.startsWith("DH"))
                likesDoubleHeaders = true;

                // BACK TO BACK PREFERENCE REQUEST (DEFAULTED TO false) //
            else if(request.startsWith("B2B")){
                likesBackToBack = true;
            }
            else if(request.startsWith("nst")){
                notSameTime=requestNotSameTime(request, team, notSameTime);
            }

            else{
                debug("Unknown constraint:" + request);
            }

        }

        team.setOffTimes(new OffTimes(offTimeList));
        team.setPlayOnceRequests(new PlayOnceRequests(playOnceTeamList));
        team.setDoubleHeaderPreference(new DoubleHeaderPreference(likesDoubleHeaders));
        team.setBackToBackPreference(new BackToBackPreference(likesBackToBack));
    }

    public static DateConstraint requestOffDate(String request, Team team, DateConstraint badDates){
        //parse the date and use it to create a new DateConstraint object
        request=request.replace("xd ","");
        String[] dates = request.split("-");
        if(dates[0].split("/").length<3){
            debug("Team" + team.getTeamId() + "xd constraint date 1 is too short.(" + request);
        }
        if(dates.length>1){
            if(dates[1].split("/").length<3){
                debug("Team" + team.getTeamId() + "xd constraint date 2 is too short." + request);
            }
            badDates.setStringDates(dates[0], dates[1], false);
        }
        else{
            badDates.setStringDate(dates[0], false);
        }
        return badDates;
    }

    public static DateConstraint requestAfterTime(String request, Team team, DateConstraint badDates){
        // incomplete; need to ensure that each time has pm or am so that it can be converted to military
        if(request.contains("pm") || request.contains("p.m.")
                ||request.contains("am")||request.contains("a.m.")){

            debug("Team" + team.getTeamId() + "after constraint time has no am/pm." + request);
        }
        request=request.replace("after ", "");
        request = getMilitaryTime(request);
        MatchTime offTime = new MatchTime("0:00", request);
        //offTimeList.add(offTime);
        badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
        return badDates;
    }

    public static DateConstraint requestBeforeTime(String request, Team team, DateConstraint badDates){
        // incomplete; need to ensure that each time has pm or am so that it can be converted to military
        if(request.contains("pm") || request.contains("p.m.")
                ||request.contains("am")||request.contains("a.m.")){

            debug("Team" + team.getTeamId() + "before constraint time has no am/pm." + request);
        }
        request=request.replace("before ", "");
        request = getMilitaryTime(request);
        MatchTime offTime = new MatchTime(request, "0:00");
        //offTimeList.add(offTime);
        badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
        return badDates;
    }

    public static DateConstraint requestOffTime(String request, Team team, DateConstraint badDates){
        request=request.replace("xr ","");
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
        //offTimeList.add(offTime);
        badDates.addRestrictedTimes(badDates.makeTimeArray(offTime));
        return badDates;
    }

    public static List<Integer> requestPlayOnce(String request, Team team, List<Integer> playOnceTeamList){
        //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
        request=request.replace("playonce ", "");
        int index = request.indexOf(".");
        Integer teamId = Integer.parseInt(request.substring(0,index));
        playOnceTeamList.add(teamId);
        return playOnceTeamList;
    }

    public static DateConstraint requestPreferredDate(String request, Team team, DateConstraint prefDates){
        request=request.replace("pd ","");
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
        return prefDates;

    }

    private static SharedTeams requestDontPlay(String request, Team team, SharedTeams dontPlay){
        //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
        request=request.replace("xplay ", "");
        int index = request.indexOf(".");
        Integer teamId=null;
        try{
            teamId = Integer.parseInt(request.substring(0,index));
        }
        catch(NumberFormatException nfe){
            debug("Team" + team.getTeamId() + "xplay constraint teamID error." + request);
        }
        dontPlay.addSharedTeam(teamId);
        return dontPlay;
    }

    private static SharedTeams requestNotSameTime(String request, Team team, SharedTeams notSameTime){
        request=request.replace("nst ","");
        Integer teamId=null;
        try{
            teamId=Integer.parseInt(request);
        }
        catch(NumberFormatException nfe){
            debug("Team"+team.getTeamId()+"nst constraint teamId error."+request);
        }
        notSameTime.addSharedTeam(teamId);
        return notSameTime;
    }

    private static String getMilitaryTime(String time) {

        if(time.contains("pm") || time.contains("p.m.")) {
            time = time.replace("pm", "");
            time = time.replace("p.m.", "");
            time=time.trim();
            String[] t = time.split(":");
            try {
                Integer timeInt = Integer.parseInt(t[0]);
                if(timeInt != 12){
                    timeInt += 12;
                }
                return timeInt.toString()+":"+t[1];
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
