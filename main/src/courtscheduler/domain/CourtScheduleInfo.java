package courtscheduler.domain;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourtScheduleInfo {
    private String filepath;
    private LocalDate conferenceStartDate;
	private LocalDate conferenceEndDate;
	private int numberOfCourts;
	private int timeslotMidnightOffsetInMinutes;
	private int numberOfTimeSlotsPerDay;
	private int timeslotDurationInMinutes;
    private List<LocalDate> holidays;

    private List<String> raw_lines;

 	public CourtScheduleInfo(String filepath) {
		// TODO read these from a file
        this.filepath = filepath;
        conferenceStartDate = new LocalDate(2014, 1, 1);
        conferenceEndDate = new LocalDate(2014, 1, 10);
        conferenceEndDate = conferenceEndDate.plusDays(1);
        numberOfCourts = 3;
        timeslotMidnightOffsetInMinutes = 420;  // 7am
        numberOfTimeSlotsPerDay = 16;  // end at ~8:30pm
        timeslotDurationInMinutes = 50;
        holidays = new ArrayList<LocalDate>();

        DateConstraint.setInfo(this);
	}

    public int configure(){
        raw_lines = slurpConfigFile(this.filepath);
        if (raw_lines.size() == 0){
            printStandardErrorMessage("Could not read anything from the configuration file.\n" +
                    "Expected the configuration file to be found at: " + FileSystems.getDefault().getPath(filepath).toAbsolutePath());
            return -1;
        }
        for (String line : raw_lines){
            if (line.startsWith(";", 0)){
                continue;
            }
            String[] lineComponents = line.split("=");
            if (lineComponents.length < 2){
                System.out.println("could not interpret line: "+ line);
                continue;
            }
            if (lineComponents[0].trim().equals("conference_start")){
                conferenceStartDate = parseDateString(lineComponents[1].trim());
            }
        }
        return 0;
    }

    private LocalDate parseDateString(String dateString){
        String[] dateComponentStrings = dateString.split("[-//]");
        int[] dateComponentInts = new int[dateComponentStrings.length];
        int year, month, day;
        if (dateComponentStrings.length != 3){
            // do something else
        } else {
            dateComponentInts[0] = Integer.parseInt(dateComponentStrings[0]);
            dateComponentInts[1] = Integer.parseInt(dateComponentStrings[1]);
            dateComponentInts[2] = Integer.parseInt(dateComponentStrings[2]);

            day = dateComponentInts[1];
            month = dateComponentInts[0];
            year = dateComponentInts[2];
            return new LocalDate(year, month, day);
        }
        return null;
    }
    private List<String> slurpConfigFile(String filename){
        List<String> lines = new ArrayList<String>();
        Path path = FileSystems.getDefault().getPath(filename);
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException x) {
            printStandardErrorMessage(String.format("IOException: %s%n", x));
        }
        return lines;
    }

    private void printStandardErrorMessage(String message){
        System.out.println("ERROR:\n"+
                message + "\n"+
                "Try rebuilding the configuration file using the provided configuration utility.");
    }

	public int getNumberOfConferenceDays() {
		return Days.daysBetween(conferenceStartDate, conferenceEndDate).getDays();
	}

	public LocalDate getConferenceStartDate() {
		return conferenceStartDate;
	}
	public LocalDate getConferenceEndDate() {
		return conferenceEndDate;
	}
	public int getNumberOfTimeSlotsPerDay() {
		return numberOfTimeSlotsPerDay;
	}
	public int getNumberOfCourts() {
		return numberOfCourts;
	}
	public boolean dayIsInConference(LocalDate date) {
		return conferenceStartDate.isBefore(date) && conferenceEndDate.isAfter(date);
	}

	public int getFinalTimeSlotIndex() {
		return numberOfTimeSlotsPerDay - 1;
	}

	public String getHumanReadableTime(int index) {
		int minutesFromMidnight = timeslotMidnightOffsetInMinutes + (timeslotDurationInMinutes * index);
		int hours = minutesFromMidnight / 60;
		int minutes = minutesFromMidnight % 60;
		String suffix;
		if (hours < 12) {
			suffix = "AM";
		}
		else {
			suffix = "PM";
			if (hours > 12)
				hours -= 12;
		}
		return String.format("%d:%02d " + suffix, hours, minutes);
	}

    public LocalDate[] getHolidays(){
        return this.holidays.toArray(new LocalDate[this.holidays.size()]);
    }

    public String toString(){
        StringBuilder result = new StringBuilder();

        for (LocalDate holiday : holidays){
            result.append("holiday = " + holiday.toString());
        }
        return result.toString();
    }
}
