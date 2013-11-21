package courtscheduler.persistence;

import courtscheduler.Main;
import courtscheduler.domain.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class CourtScheduleIO {

    private List<Match> matchList;
    private static List<Team> teamList;
	private static DateTimeFormatter normalTime = DateTimeFormat.forPattern("h:mma");
	private static DateTimeFormatter militaryTime = DateTimeFormat.forPattern("H:mm");

    public CourtScheduleIO(CourtScheduleInfo info) {
        matchList = new ArrayList<Match>();
        teamList = new ArrayList<Team>();
    }


    public List<Team> readXlsx(String filename, CourtScheduleInfo info) throws Exception {

        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        // Get worksheet by index
        XSSFSheet sh = wb.getSheetAt(0);

        Integer rowCounter = 2;
        Integer rowCount = sh.getLastRowNum();

        if (Main.LOG_LEVEL >= 1) {
            System.out.println(new java.util.Date() + "[INFO] Worksheet Name: " + sh.getSheetName());
            System.out.println(new java.util.Date() + "[INFO] Worksheet has " + (rowCount - 1) + " lines of data.");
        }

        while (rowCounter <= rowCount) {
            Row currentRow = sh.getRow(rowCounter);
            if (currentRow != null && currentRow.getLastCellNum() > 0) {
                teamList.add(processRow(currentRow, info));
            }
            rowCounter += 1;
        }

        if (Main.LOG_LEVEL >= 1) {
            for (int x = 0; x < teamList.size(); x++) {
                System.out.println(teamList.get(x));
            }
            System.out.println(new java.util.Date() + "[INFO] Processing finished.");
        }

        return teamList;
    }

    public void writeXlsx(List<Match> matches, CourtScheduleInfo info, String filepath) throws IOException {

        Collections.sort(matches);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        sheet = null;
        int rowNumber = 0;
        int cellNumber = 0;
        String conf = "";

        for(Match match : matches) {

            // CONFERENCE
            String conference = match.getConference();

            if (!conference.equals(conf)) {
                // Create a new sheet with titles and headings for each new conference
                sheet = workbook.createSheet(conference);
                rowNumber = 0;
                Row header = sheet.createRow(rowNumber);

                // Set sheet to Landscape so all columns will fit on one page
                XSSFPrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setOrientation(PrintOrientation.LANDSCAPE);

                // Column widths determined by specific sizes of heading strings (further down)
				sheet.setColumnWidth(0, 3072);
                sheet.setColumnWidth(1, 7424);
                sheet.setColumnWidth(2, 1024);
                sheet.setColumnWidth(3, 7424);
                sheet.setColumnWidth(4, 3072);
                sheet.setColumnWidth(5, 2816);
                sheet.setColumnWidth(6, 2816);
                sheet.setColumnWidth(7, 2304);
                sheet.setColumnWidth(8, 1792);

                /* FIXME lines below were attempts to set titles to larger font size and headings to bold
                XSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle = workbook.createCellStyle();
                XSSFFont xSSFFont = workbook.createFont();
                xSSFFont.setFontName(XSSFFont.DEFAULT_FONT_NAME);
                xSSFFont.setFontHeightInPoints((short) 28);
                xSSFFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                hSSFFont.setColor(HSSFColor.GREEN.index);
                cellStyle.setFont(xSSFFont);
                setFontHeight(28);      // end FIXME    */

                header.createCell(1).setCellValue("THE COURTS");

                // FIXME one more line attempted font size // xSSFFont.setFontHeightInPoints((short) 24);

                header.createCell(3).setCellValue("Game Schedule");
                rowNumber = rowNumber + 2;

                header = sheet.createRow(rowNumber);
                header.createCell(0).setCellValue(conference);
                rowNumber = rowNumber + 2;

                header = sheet.createRow(rowNumber);
                header.createCell(1).setCellValue("TEAM");
                header.createCell(2).setCellValue(" ");
                header.createCell(3).setCellValue("OPPONENT");
                header.createCell(4).setCellValue("CONFERENCE");
                header.createCell(5).setCellValue("DAY");
                header.createCell(6).setCellValue("DATE");
                header.createCell(7).setCellValue("TIME");
                header.createCell(8).setCellValue("COURT");
            }

            cellNumber = 0;
            rowNumber++;
            Row dataRow = sheet.createRow(rowNumber);

			dataRow.createCell(cellNumber++).setCellValue(match.getCanPlayInCurrentSlot() ? "" : "WARNING");

            // TEAM
            String teamName1 = match.getTeam1().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName1);

            // VS
            String vs = "vs.";
            dataRow.createCell(cellNumber++).setCellValue(vs);

            // OPPONENT
            String teamName2 = match.getTeam2().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName2);

            // CONFERENCE
            conference = match.getTeam1().getConference();
            
            dataRow.createCell(cellNumber++).setCellValue(conference);

            // DAY
            Integer matchDateIndex = match.getMatchSlot().getDay();
            LocalDate matchDate = info.getConferenceStartDate().plusDays(matchDateIndex);
            String day = matchDate.dayOfWeek().getAsText();
            dataRow.createCell(cellNumber++).setCellValue(day);

            // DATE
            String date = matchDate.toString();
            if (Main.LOG_LEVEL > 1) {
                date = date + " [" + matchDateIndex + "]";
            }
            dataRow.createCell(cellNumber++).setCellValue(date);

            // TIME
            Integer matchTime = match.getMatchSlot().getTime();
            String time = info.getHumanReadableTime(matchTime);
            if (Main.LOG_LEVEL > 1) {
                time = time + " [" + matchTime + "]";
            }
            dataRow.createCell(cellNumber++).setCellValue(time);

            // COURT
            Integer courtId = match.getMatchSlot().getCourt();
            // normal people like their courts indexed from one, not zero,
            // so add one if we're printing for the client
            dataRow.createCell(cellNumber).setCellValue(courtId + (Main.LOG_LEVEL > 1 ? 0 : 1));

            conf = match.getConference();
        }

        Scanner input = new Scanner(System.in);
        String reply = "";
        boolean continueInput = true;
        StackTraceElement[] stackTraceE = new StackTraceElement[100];
        do {
            try {
                FileOutputStream out =
                        new FileOutputStream(new File(filepath));
                workbook.write(out);
                out.close();
                System.out.println("Excel written successfully to " + filepath + ".");
                continueInput = false;

            } catch (FileNotFoundException e) {
                stackTraceE = e.getStackTrace();
                System.out.println("An output file already exists and is open.");
                System.out.println("To overwrite: close it, then hit 'enter'.");
                System.out.println("To create a new output file: enter new name or path.");
                System.out.println("To quit: type 'q'.");
                reply = input.nextLine();
                if (reply.regionMatches(true, 0, "q", 0, 1))
                    return;
                else if (reply.equals(""))
                    continue;
                else
                    filepath = reply;

            } catch (IOException e) {
                stackTraceE = e.getStackTrace();
                System.out.println("Output error.  Please enter new name or path for this output file, or 'q' to quit: ");
                reply = input.nextLine();
                if (reply.regionMatches(true, 0, "q", 0, 1))
                    return;
                else
                    filepath = reply;
            }
        } while (continueInput);
    }

    private Team processRow(Row currentRow, CourtScheduleInfo info) {
        short columnCount = currentRow.getLastCellNum();
        int columnCounter = 0;

        Integer teamId = null;
        String teamName = "";
        Integer conference = null;
        String year = "";
        String gender = "";
        Integer grade = null;
        String level = "";
        String requests = "";
        String notSameTimeAs = "";
        Team team = new Team();

        while (columnCounter < columnCount) {

            Cell cell = currentRow.getCell(columnCounter);


            if (cell == null) {
                columnCounter++;
                continue;  // if the cell is null just jump to the next iteration
            }

            if (columnCounter == 0) {
                try {
                    int index = cell.toString().indexOf(".");
                    teamId = Integer.parseInt(cell.toString().substring(0, index));
                    team.setTeamId(teamId);
                } catch (NumberFormatException e) {
                    //not sure what we should do here, this means a team's id is not being captured
                    e.printStackTrace();
                }
            }
            else if (columnCounter == 1) {
                team.setConference(cell.toString());
            }
            else if (columnCounter == 2) {
                teamName = cell.toString();
                team.setTeamName(teamName);
            }
            else if (columnCounter == 3) {
                year = cell.toString();
                team.setYear(year);
            }
            else if (columnCounter == 4) {
                gender = cell.toString();
                team.setGender(gender);
            }
            else if (columnCounter == 5) {
                try {
                    int index = cell.toString().indexOf(".");
                    grade = Integer.parseInt(cell.toString().substring(0, index));
                    team.setGrade(grade);
                } catch (NumberFormatException e) {
                    // still don't know what to do about this, this is bad
                    e.printStackTrace();
                }
            }
            else if (columnCounter == 6) {
                level = cell.toString();
                team.setLevel(level);
            }
            else if (columnCounter == 7) {
                requests = cell.toString();
                //debug(team.getTeamId().toString()+":"+requests);
                processRequestConstraints(team, requests, info);
            }
            else if (columnCounter == 8) {
                notSameTimeAs = cell.toString();
                String[] tempSplit = notSameTimeAs.split(",");

                for (String teamIdStr : tempSplit) {
                    try {
                        int index = teamIdStr.indexOf(".");
                        if (index > -1) {
                            teamId = Integer.parseInt(teamIdStr.substring(0, index));
                            team.getAvailability().getNotSameTimeAs().addSharedTeam(teamId);
                            team.getDontPlay().addSharedTeam(teamId);
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Unable to add team " + teamIdStr + "to shared team list. Unparsable.");
                    } catch (NullPointerException npe) {
                        System.out.println("team.availability or team.availability.notSameTimeAs is null for " + teamIdStr);
                    }
                }
            }


            columnCounter += 1;
        }
        return team;
    }

    private static void processRequestConstraints(Team team, String requests, CourtScheduleInfo info) {

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
        DateConstraint onlyDates = null;
        SharedTeams dontPlay=team.getDontPlay();
        SharedTeams notSameTime= new SharedTeams();

        for (String request : requestArray) {

            // CANT PLAY ON CERTAIN DATE OR DATE RANGE //
            request = request.toLowerCase();
            request = request.trim();
            //System.out.println(request);
            if (request.equals("")) {
				continue;
            }
            else if (request.startsWith("no")) {
                request = request.replace("no ", "");
                parseDateConstraints(request, team, badDates);
            }
            // TEAM REQUEST TO PLAY ON DAY OTHER THAN PRIMARY DAY/TIME //
            else if (request.startsWith("pref")||request.startsWith("prefer")) {
                request = request.replace("prefer", "");
                request = request.replace("pref", "");
                request = request.trim();
                parseDateConstraints(request, team, prefDates);
            }
            // ONLY TIMES
            else if(request.startsWith("only")){
                request=request.replace("only ", "");
				if (onlyDates == null) {
					onlyDates = new DateConstraint();
				}
                parseDateConstraints(request, team, onlyDates);
            }


            //DONT PLAY THESE TEAMS
            else if (request.startsWith("xplay"))
                dontPlay = requestDontPlay(request, team, dontPlay);

                // TEAM REQUEST TO PLAY ANOTHER TEAM ONLY ONCE
            else if (request.startsWith("playonce"))
                playOnceTeamList = requestPlayOnce(request, team, playOnceTeamList);

                // DOUBLE HEADER PREFERENCE REQUEST (DEFAULTED TO false) //
            else if (request.startsWith("dh"))
                likesDoubleHeaders = true;

                // BACK TO BACK PREFERENCE REQUEST (DEFAULTED TO false) //
            else if (request.startsWith("b2b"))
                likesBackToBack = true;

            else if (request.startsWith("nst"))
                notSameTime = requestNotSameTime(request, team, notSameTime);

            else
                System.out.println("Unknown constraint:" + request);
        }

		// put all conference primary days on prefDates
		String prefDays = info.getPrimaryDays().get(team.getConference());
		if (prefDays != null){
			parseDateConstraints(prefDays, team, prefDates);
        }
		// if there's no primary day marked, then default to doing nothing

		// do nothing with secondary days-- they're neither preferred nor unplayable

        // put all dates that are not conference primary/secondary days on the badDates object
		String badDays = info.getBadConferenceDays().get(team.getConference());
		if (badDays != null && badDays.length() != 7) {
        	parseDateConstraints(badDays, team, badDates);
		}

        team.setOffTimes(new OffTimes(offTimeList));
        team.setPlayOnceRequests(new PlayOnceRequests(playOnceTeamList));
        team.setDoubleHeaderPreference(new DoubleHeaderPreference(likesDoubleHeaders));
        team.setBackToBackPreference(new BackToBackPreference(likesBackToBack));
		team.setOnlyDates(onlyDates);
        team.setBadDates(badDates);
        team.setPreferredDates(prefDates);
    }
    public static DateConstraint parseDateConstraints(String request, Team team, DateConstraint dates){
        if (request == null){
            // FIXME
            // no requests? Probably no configuration for this team!
        }
        if (request.contains("/")) {
            requestDate(request, team, dates);
        }
        else if(request.contains(":")){
            if(request.contains("-")){
                requestOffTime(request, team, dates);
            }
            else if(request.contains("before")){
                requestBeforeTime(request, team, dates);
            }
            else if(request.contains("after")){
                requestAfterTime(request, team, dates);
            }
        }
        else{
            requestDayOfWeek(request, team, dates);
        }
        return dates;
    }

    public static void requestDayOfWeek(String request, Team team, DateConstraint dates){
        request = request.trim();
        String[] reSplit = request.split(" ");
        for(int i = 0; i < reSplit.length; i++) {
            dates.addDates(dates.findDayOfWeek(reSplit[i]));
        }
    }
    public static void requestDate(String request, Team team, DateConstraint date){
        //parse the date and use it to create a new DateConstraint object
        String[] dates = request.split("-");
        if (dates[0].split("/").length < 3) {
            System.out.println("Team" + team.getTeamId() + " request date 1 is too short." + request);
        }
        if (dates.length > 1) {
            if (dates[1].split("/").length < 3) {
                System.out.println("Team" + team.getTeamId() + " request date 2 is too short." + request);
            }
            date.addDates(date.findDateRange(dates[0], dates[1]));
        } else {
            date.addDate(date.findDate(dates[0]));
        }
    }

    public static void requestAfterTime(String request, Team team, DateConstraint badDates){
        // incomplete; need to ensure that each time has pm or am so that it can be converted to military
		request = request.replace("after ", "");
        if (isAfternoon(request) == null) {
            System.out.println("Team" + team.getTeamId() + "after constraint time has no am/pm." + request);
        }
        request = getMilitaryTime(request);
        //System.out.println(request);
        badDates.addRestrictedTimes(badDates.makeTimeArray(request, "23:59"));
    }

    public static void requestBeforeTime(String request, Team team, DateConstraint badDates){
        // incomplete; need to ensure that each time has pm or am so that it can be converted to military
		request = request.replace("before ", "");
        if (isAfternoon(request) == null) {
            System.out.println("Team" + team.getTeamId() + "before constraint time has no am/pm." + request);
        }
		LocalTime t = normalTime.parseLocalTime(request);
		// if we're saying not before an exact hour, we need to not schedule them to start AT that hour
		request = militaryTime.print(t.minusMinutes(1));
        badDates.addRestrictedTimes(badDates.makeTimeArray(DateConstraint.getInfo().getHumanReadableTime(0), request));
    }

    public static void requestOffTime(String request, Team team, DateConstraint badDates){
        String[] times = request.split("-");
        for (int i = 0; i < times.length; i++){
            if (isAfternoon(times[i]) == null) {
                System.out.println("Team" + team.getTeamId() + "xr constraint time has no am/pm on time "+i+"." + request);  // FIXME: is this the right error message?
            }
        }
        times[0] = getMilitaryTime(times[0]);
        times[1] = getMilitaryTime(times[1]);
        //System.out.println(times[0]+"vs"+times[1]);
        badDates.addRestrictedTimes(badDates.makeTimeArray(times[0], times[1]));
    }

    public static List<Integer> requestPlayOnce(String request, Team team, List<Integer> playOnceTeamList) {
        //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
        request = request.replace("playonce ", "");
        int index = request.indexOf(".");
        Integer teamId = Integer.parseInt(request.substring(0, index));
        playOnceTeamList.add(teamId);
        return playOnceTeamList;
    }


    private static SharedTeams requestDontPlay(String request, Team team, SharedTeams dontPlay) {
        //parse the request for the teams Id or name or whatever Shane wants to use (ID would be best for us)
        request = request.replace("xplay ", "");
        Integer teamId = null;
        try {
            teamId = Integer.parseInt(request);
        } catch (NumberFormatException nfe) {
            System.out.println("Team" + team.getTeamId() + "xplay constraint teamID error." + request); // FIXME: this is not human readable enough!
        }
        dontPlay.addSharedTeam(teamId);
        return dontPlay;
    }

    private static SharedTeams requestNotSameTime(String request, Team team, SharedTeams notSameTime) {
        request = request.replace("nst ", "");
        Integer teamId = null;
        try {
            teamId = Integer.parseInt(request);
        } catch (NumberFormatException nfe) {
            System.out.println("Team" + team.getTeamId() + "nst constraint teamId error." + request); // FIXME: this is not human readable enough!
        }
        notSameTime.addSharedTeam(teamId);
        return notSameTime;
    }

    public static Boolean isAfternoon(String time){
        boolean isPM = time.matches("[0-9: ]*([pP].?[mM].?)");
        boolean isAM = time.matches("[0-9: ]*([aA].?[mM].?)");

        if (!isPM && !isAM){
            // string contains neither a.m. nor p.m.
            String hourString = time.split(":")[0];
            int hour;
            try {
                hour = Integer.parseInt(hourString);
                isPM = hour >= 12;
            } catch (NumberFormatException nfe){
                System.out.println("Time not formatted correctly? " +
                        "Could not read a number from: \"" + hourString + "\", " +
                        "given a time of " + time);
                return null;
            }
            if (!isPM && hour != 0){
                // could not decide!
                return null;
            } else if (hour == 0){
                return false;
            }
        }
        return isPM;
    }
    public static String getMilitaryTime(String time) {

        // Pattern pattern = Pattern.compile("am|a.m.|pm|p.m.", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        // time = time.replaceAll(pattern.toString(), "");
        Boolean isPM = isAfternoon(time);

        time = time.replaceAll("([apAP].?[mM].?)", "");
        time = time.trim();

        if (isPM != null && isPM) {

            String[] t = time.split(":");
            if (t.length < 2) {
                System.out.println("Time not formatted correctly: " + time);
                return "";
            }
            try {
                Integer timeInt = Integer.parseInt(t[0]);
                if (timeInt < 12) {
                    timeInt += 12;
                }
                return timeInt.toString() + ":" + t[1];
            } catch (NumberFormatException e) {
                System.out.println("Time not formatted correctly? " +
                        "Could not read a number from: \"" + t[0] + "\", " +
                        "given a time of " + time);
                if (Main.LOG_LEVEL > 1) {
                    e.printStackTrace();
                }
                return "";
            }
        } else {

            String[] t = time.split(":");
            if (t.length < 2) {
                System.out.println("Time not formatted correctly: " + time);
                return "";
            }
            try {
                Integer timeInt = Integer.parseInt(t[0]);
                if (timeInt == 12) {
                    timeInt = 0;
                }
                return timeInt + ":" + t[1];
            } catch (NumberFormatException e) {
                System.out.println("Time not formatted correctly? " +
                        "Could not read a number from: \"" + t[0] + "\", " +
                        "given a time of " + time);
                if (Main.LOG_LEVEL > 1) {
                    e.printStackTrace();
                }
                return "";
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

        while (rowCounter <= rowCount) {
            Row currentRow = sh.getRow(rowCounter);
            short columnCount = currentRow.getLastCellNum();

            if (columnCount > columnWidth)
                columnWidth = columnCount;
        }

        return columnWidth;
    }

}
