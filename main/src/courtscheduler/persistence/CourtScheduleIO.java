/*
 * Copyright 2013 Michael Adams, CJ Done, Charles Eswine, Kyle Falconer,
 *  Will Gorman, Stephen Kaysen, Pat McCroskey and Matthew Swinney
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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

	private static int rowNumber;

    public CourtScheduleIO(CourtScheduleInfo info) {
        matchList = new ArrayList<Match>();
        teamList = new ArrayList<Team>();
    }


    public List<Team> readXlsx(String filename, CourtScheduleInfo info) throws Exception {

        File file = new File(filename);
        if (!file.exists()){
            return null;
        }

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        // Get worksheet by index
        XSSFSheet sh = wb.getSheetAt(0);

        rowNumber = 2;
        Integer rowCount = sh.getLastRowNum();

        if (Main.LOG_LEVEL >= 1) {
            System.out.println(new java.util.Date() + "[INFO] Worksheet Name: " + sh.getSheetName());
            System.out.println(new java.util.Date() + "[INFO] Worksheet has " + (rowCount - 1) + " lines of data.");
        }

        while (rowNumber <= rowCount) {
            Row currentRow = sh.getRow(rowNumber);
            if (currentRow != null && currentRow.getLastCellNum() > 0) {
				Team nextTeam = processRow(currentRow, info);
				if (nextTeam != null && nextTeam.getTeamId() != null) {
                	teamList.add(nextTeam);
				}
				else
					break;
            }
            rowNumber += 1;
        }

        if (Main.LOG_LEVEL >= 1) {
            /*for (int x = 0; x < teamList.size(); x++) {
                System.out.println(teamList.get(x));
            }*/
            System.out.println(new java.util.Date() + " [INFO] Input parsed. Constructing possible matches...");
        }

        return teamList;
    }

    public String writeXlsx(List<Match> matches, CourtScheduleInfo info, String filepath) throws IOException {



        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        sheet = null;
        rowNumber = 0;
        int cellNumber = 0;
        String lastConf = "";

		// print out master schedule
		Collections.sort(matches, Match.timeComparator);
		sheet = initializePage(workbook, "Master");
		for (Match match : matches) {
			rowNumber++;
			printMatch(sheet.createRow(rowNumber), match, info);
		}

		// print out individual conferences
		rowNumber = 0;
		Collections.sort(matches, Match.conferenceComparator);

        for(Match match : matches) {
            if (!match.getConference().equals(lastConf)) {
				sheet = initializePage(workbook, match.getConference());
            }

            rowNumber++;
            printMatch(sheet.createRow(rowNumber), match, info);
            lastConf = match.getConference();
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
                    return null;
                else if (reply.equals(""))
                    continue;
                else
                    filepath = reply;

            } catch (IOException e) {
                stackTraceE = e.getStackTrace();
                System.out.println("Output error.  Please enter new name or path for this output file, or 'q' to quit: ");
                reply = input.nextLine();
                if (reply.regionMatches(true, 0, "q", 0, 1))
                    return null;
                else
                    filepath = reply;
            }
        } while (continueInput);
		return filepath;
    }

	private void printMatch(XSSFRow dataRow, Match match, CourtScheduleInfo info) {
		int cellNumber = 0;
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
		String conference = match.getConference();

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
		dataRow.createCell(cellNumber++).setCellValue(courtId + (Main.LOG_LEVEL > 1 ? 0 : 1));

		dataRow.createCell(cellNumber).setCellValue(match.getCanPlayInCurrentSlot() ? "" : "WARNING: Team is scheduled when they cannot play");
	}

	private XSSFSheet initializePage(XSSFWorkbook workbook, String conference) {
		// Create a new sheet with titles and headings for each new conference
		XSSFSheet sheet = workbook.createSheet(conference);
		rowNumber = 0;
		Row header = sheet.createRow(rowNumber);

		// Set sheet to Landscape so all columns will fit on one page
		XSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setOrientation(PrintOrientation.LANDSCAPE);

		// Column widths determined by specific sizes of heading strings (further down)
		sheet.setColumnWidth(0, 7424);
		sheet.setColumnWidth(1, 1024);
		sheet.setColumnWidth(2, 7424);
		sheet.setColumnWidth(3, 3072);
		sheet.setColumnWidth(4, 2816);
		sheet.setColumnWidth(5, 2816);
		sheet.setColumnWidth(6, 2403);
		sheet.setColumnWidth(7, 1792);

		header.createCell(0).setCellValue("THE COURTS");

		header.createCell(2).setCellValue("Game Schedule");
		rowNumber = rowNumber + 2;

		header = sheet.createRow(rowNumber);
		header.createCell(0).setCellValue("Conference " + conference);
		rowNumber = rowNumber + 2;

		header = sheet.createRow(rowNumber);
		header.createCell(0).setCellValue("TEAM");
		header.createCell(1).setCellValue(" ");
		header.createCell(2).setCellValue("OPPONENT");
		header.createCell(3).setCellValue("CONFERENCE");
		header.createCell(4).setCellValue("DAY");
		header.createCell(5).setCellValue("DATE");
		header.createCell(6).setCellValue("TIME");
		header.createCell(7).setCellValue("COURT");

		return sheet;
	}

	private String getStringValueOfInt(String cell) {
		int index = cell.indexOf(".");
		if (index > -1)  {
			return cell.substring(0, index);
		}
		else {
			return cell;
		}
	}

    private Team processRow(Row currentRow, CourtScheduleInfo info) {
        short columnCount = currentRow.getLastCellNum();
        int columnCounter = 0;

        Integer teamId = null;
        String teamName = "";
        Integer conference = null;
        String year = "";
        String gender = "";
        String grade = "";
        String level = "";
        String requests = "";
        String notSameTimeAs = "";
        Team team = new Team();

        while (columnCounter < columnCount) {

            Cell cell = currentRow.getCell(columnCounter);


            if (cell == null) {
				if (teamId == null) {
					System.out.println("================================================================================");
					break;
				}
				else {
                	columnCounter++;
                	continue;  // if the cell is null just jump to the next iteration
				}
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
				team.setConference(getStringValueOfInt(cell.toString()));
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
                team.setGrade(getStringValueOfInt(cell.toString()));
				if (team.getGrade().trim().equals("")) {
					System.out.println("ERROR: Team " + teamId + " has no grade!");
				}
            }
            else if (columnCounter == 6) {
                level = cell.toString();
                team.setLevel(level);
            }
            else if (columnCounter == 7) {
                requests = cell.toString();
                //debug(team.getTeamId().toString()+":"+requests);
				System.out.println(team.getTeamId() + ": " + requests);
                processRequestConstraints(team, requests, info);
            }
            else if (columnCounter == 8) {
                notSameTimeAs = cell.toString();
                String[] tempSplit = notSameTimeAs.split(",");

                for (String otherTeamName : tempSplit) {
					if (otherTeamName.trim().equals("")) {
						continue;
					}
                    team.getAvailability().getNotSameTimeAs().addSharedTeam(otherTeamName);
                	team.getDontPlay().addSharedTeam(otherTeamName);
					System.out.println(teamId + " won't play " + otherTeamName);
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
                System.out.println("Unknown constraint in row " + (rowNumber+1) + ": " + request);
        }

		// put all conference primary days on prefDates
		// FIXME
		String prefDays = info.getPrimaryDays().get(team.getGrade());
		if (prefDays != null){
			parseDateConstraints(prefDays, team, prefDates);
        }
		// if there's no primary day marked, then default to doing nothing

		// do nothing with secondary days-- they're neither preferred nor unplayable

        // put all dates that are not conference primary/secondary days on the badDates object
		String badDays = info.getBadConferenceDays().get(team.getGrade());
		if (badDays != null && badDays.length() != 7) {
        	parseDateConstraints(badDays, team, badDates);
		} else {
			System.out.println("WARNING: team " + team.getTeamId() + " has no conference days! Is this intentional?");
		}

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
			else {
				System.out.printf("ERROR: Unknown time-based constraint for team %d [%s]\n", team.getTeamId(), request);
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
        dontPlay.addSharedTeam(request.trim());
        return dontPlay;
    }

    private static SharedTeams requestNotSameTime(String request, Team team, SharedTeams notSameTime) {
        request = request.replace("nst ", "");
        notSameTime.addSharedTeam(request.trim());
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
