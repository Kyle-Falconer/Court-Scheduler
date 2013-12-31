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

import courtscheduler.domain.DateConstraint;
import courtscheduler.domain.Match;
import courtscheduler.domain.Team;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static courtscheduler.Main.error;
import static courtscheduler.Main.warning;
import static courtscheduler.persistence.CourtScheduleIO.getMilitaryTime;
import static courtscheduler.persistence.CourtScheduleIO.parseDateConstraints;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourtScheduleInfo {
    private static final String EOL = System.getProperty("line.separator");
    private String filepath;
    private LocalDate conferenceStartDate;
    private LocalDate conferenceEndDate;
    private int numberOfCourts;
    private int timeslotMidnightOffsetInMinutes;
    private int numberOfTimeSlotsPerDay;
    private int timeslotDurationInMinutes;
    private List<LocalDate> holidays;
    private Map<String, List<Integer>> primaryDays;
    private Map<String, List<Integer>> secondaryDays;
    private Map<String, List<Integer>> badConferenceDays;
    private String inputFileLocation;
    private String outputFolderLocation;

    private List<String> raw_lines;

    private static final String MONDAY = "monday";
    private static final String TUESDAY = "tuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";
    private static final String SUNDAY = "sunday";

	// Joda Time counts its weekdays from one, not from zero, so we have to pad
	// plus I can't resist a pun
	private static final String NONEDAY = "";

	public static final String[] LONG_DAYS = {NONEDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
	public static final String[] SHORT_DAYS = {NONEDAY, "M", "T", "W", "R", "F", "S", "U"};

	// TODO:
    // Make numberOfTimeSlotsPerDay, timeslotDurationInMinutes,
    // and timeslotMidnightOffsetInMinutes configurable for each day of the week (7 days).
    // this is needed because Shane wants to make a particular day shorter than the others, so this
    // would be a more general solution.

    public CourtScheduleInfo(String filepath) {
        this.filepath = filepath;
		this.badConferenceDays = new HashMap<String, List<Integer>>();
		this.primaryDays = new HashMap<String, List<Integer>>();
		this.secondaryDays = new HashMap<String, List<Integer>>();
		this.holidays = new ArrayList<LocalDate>();

        DateConstraint.setInfo(this);
		Match.setInfo(this);
    }

    public int configure() {
        raw_lines = slurpConfigFile(this.filepath);
        if (raw_lines.size() == 0) {
            error(true, "Could not read anything from the configuration file."+EOL +
                    "Expected the configuration file to be found at: " + FileSystems.getDefault().getPath(filepath).toAbsolutePath());
        }
		String[] scheduleDescription = new String[0];
        for (String line : raw_lines) {
            if (line.startsWith(";", 0) || line.trim().length() == 0) {
                // this line is a comment or empty line
                continue;
            }
            String[] lineComponents = line.split("=");
            if (lineComponents.length < 2) {
                System.out.println("could not interpret line: " + line);
                continue;
            }

            String key = lineComponents[0].trim();
            String value = lineComponents[1].trim();

            if (key.equals("conference_start")) {
                this.conferenceStartDate = parseDateString(value);
            } else if (key.equals("conference_end")) {
                this.conferenceEndDate = parseDateString(value);
            } else if (key.equals("court_count")) {
                this.numberOfCourts = Integer.parseInt(value);
            } else if (key.equals("timeslots_count")) {
                this.numberOfTimeSlotsPerDay = Integer.parseInt(value);
            } else if (key.equals("timeslot_duration_minutes")) {
                this.timeslotDurationInMinutes = Integer.parseInt(value);
            } else if (key.equals("timeslots_start")) {
                this.timeslotMidnightOffsetInMinutes = timeStringToMinutes(value);
            } else if (key.startsWith("conference")) {
                this.parseConferenceDays(value);
            } else if (key.startsWith("holiday")) {
				if (!value.contains("-")) {
					// one date
					holidays.add(LocalDate.parse(value, DateConstraint.dateFormat));
				}
				else {
					// date range
					LocalDate start = LocalDate.parse(value.substring(0, value.indexOf("-")), DateConstraint.dateFormat);
					LocalDate end = LocalDate.parse(value.substring(value.indexOf("-")+1), DateConstraint.dateFormat);
					LocalDate next = start;
					while (next.isBefore(end)) {
						holidays.add(next);
						next = next.plusDays(1);
					}
					holidays.add(end);
				}
			}
			else if (key.startsWith("schedule_description")) {
				scheduleDescription = value.split(", ");
			}

            else if (key.startsWith("input_file")){
                inputFileLocation = value;
            }

            else if (key.startsWith("output_folder")){
                outputFolderLocation = value;
            }
        }
		DateConstraint.setStandardDates(this.createStandardSchedule(scheduleDescription));
        return 0;
    }

    public String getInputFileLocation(){
        return this.inputFileLocation;
    }

    public String getOutputFolderLocation(){
        return this.outputFolderLocation;
    }


    public Map<String, List<Integer>> getBadConferenceDays(){
        return this.badConferenceDays;
    }
	public Map<String, List<Integer>> getPrimaryDays() {
		return this.primaryDays;
	}
	public Map<String, List<Integer>> getSecondaryDays() {
		return this.secondaryDays;
	}

    public static int timeStringToMinutes(String timeString) {
        timeString = getMilitaryTime(timeString);
        String[] hourMin = timeString.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

    private void parseConferenceDays(String conferenceString){
        // example line 'conference=11-MW:FS'
        String conference = conferenceString.substring(0, conferenceString.indexOf("-"));
		String primaryShortDays, secondaryShortDays;
		if (conferenceString.indexOf(":") != -1) {
        	primaryShortDays = conferenceString.split(":")[0];
        	secondaryShortDays = conferenceString.split(":")[1];
		}
		else {
			primaryShortDays = conferenceString;
			secondaryShortDays = "";
		}
        List<Integer> primaryDaysList = new ArrayList<Integer>();
		List<Integer> secondaryDaysList = new ArrayList<Integer>();
		List<Integer> badDaysList = new ArrayList<Integer>();
        String[] result = new String[4];

        for (int i = 1; i < SHORT_DAYS.length; i++) {
            String shortDay = SHORT_DAYS[i];
            String longDay = LONG_DAYS[i];
            if (primaryShortDays.contains(shortDay)) {
                primaryDaysList.add(i);
            }
            else if (secondaryShortDays.contains(shortDay)) {
                secondaryDaysList.add(i);
            }
            else {
                badDaysList.add(i);
            }
        }

		this.badConferenceDays.put(conference, badDaysList);
		this.primaryDays.put(conference, primaryDaysList);
		this.secondaryDays.put(conference, secondaryDaysList);
    }

    public static LocalDate parseDateString(String dateString) {
        String[] dateComponentStrings = dateString.split("[-//]");
        int[] dateComponentInts = new int[dateComponentStrings.length];
        int year, month, day;
        if (dateComponentStrings.length != 3) {
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

    private List<String> slurpConfigFile(String filename) {
        List<String> lines = new ArrayList<String>();
        Path path = FileSystems.getDefault().getPath(filename);
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException x) {
            error(String.format("Could not read the configuration file with file name: %s", filename));
        }
        return lines;
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

    public void setTimeslotMidnightOffset(String value){
        this.timeslotMidnightOffsetInMinutes = timeStringToMinutes(value);
    }

    public void setTimeslotDuration(String value){
        this.timeslotDurationInMinutes = Integer.parseInt(value);
    }

    public void setNumberOfTimeSlotsPerDay(String value){
        this.numberOfTimeSlotsPerDay = Integer.parseInt(value);
    }

    public boolean dayIsInConference(LocalDate date) {
        return conferenceStartDate.isBefore(date) && conferenceEndDate.isAfter(date);
    }

    public int getFinalTimeSlotIndex() {
        return getNumberOfTimeSlotsPerDay() - 1;
    }

    public String getHumanReadableTime(int index) {
        int minutesFromMidnight = timeslotMidnightOffsetInMinutes + (timeslotDurationInMinutes * index);
        int hours = minutesFromMidnight / 60;
        int minutes = minutesFromMidnight % 60;
        String suffix;
        if (hours < 12) {
            suffix = "a.m.";
        } else {
            suffix = "p.m.";
            if (hours > 12)
                hours -= 12;
        }
        return String.format("%d:%02d " + suffix, hours, minutes);
    }

    /**
     * @param time The human-readable time string.
     * @return
     */
    public Integer getCeilingTimeIndex(String time) {
        int minutes = timeStringToMinutes(time);
        int index = (int) Math.ceil((minutes - this.timeslotMidnightOffsetInMinutes) / this.timeslotDurationInMinutes);
        if (index < 0 ) {
            warning("Configuration time " + time + " is before the start time.");
            return 0;
        } else if (index >= this.numberOfTimeSlotsPerDay){
            warning("Configuration time " + time + " is after the end time.");
            return this.numberOfTimeSlotsPerDay-1;
        }
        return index;
    }
	public Integer getFloorTimeIndex(String time) {
		int minutes = timeStringToMinutes(time);
		int index = (int) Math.ceil((minutes - this.timeslotMidnightOffsetInMinutes) / this.timeslotDurationInMinutes);
		if (index < 0 ) {
            warning("Configuration time " + time + " is before the start time.");
			return 0;
		} else if (index >= this.numberOfTimeSlotsPerDay){
            warning("Configuration time " + time + " is after the end time.");
			return this.numberOfTimeSlotsPerDay-1;
		}
		return index;
	}

    public LocalDate[] getHolidays() {
        return this.holidays.toArray(new LocalDate[this.holidays.size()]);
    }

    /**
     * toString method which outputs the values stored in the same format in which it was read in.
     * This is to say that the output could also be used as the input.
     *
     * @return
     */
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("; " + FileSystems.getDefault().getPath(filepath).toAbsolutePath() + EOL);

        if (conferenceStartDate != null)
            result.append("conference_start=" + conferenceStartDate.toString() + EOL);

        if (conferenceEndDate != null)
            result.append("conference_end=" + conferenceEndDate.toString() + EOL);

        result.append("court_count=" + numberOfCourts + EOL);
        result.append("timeslots_start=" + timeslotMidnightOffsetInMinutes + EOL);
        result.append("timeslots_count=" + numberOfTimeSlotsPerDay + EOL);
        result.append("timeslot_duration_minutes=" + timeslotDurationInMinutes + EOL);

        for (LocalDate holiday : holidays) {
            result.append("holiday = " + holiday.toString() +EOL);
        }

        return result.toString();
    }

	public int getDayOfWeek(int index) {
		return this.conferenceStartDate.plusDays(index).getDayOfWeek();
	}

	public DateConstraint createStandardSchedule(String[] description) {
		DateConstraint standardSchedule = new DateConstraint();
		for (LocalDate holiday : holidays)
			standardSchedule.addDate(standardSchedule.findDate(holiday));
		Team noTeam = null;
		ArrayList<DateConstraint> components = new ArrayList<DateConstraint>();
		for (String request : description) {
			request = request.trim().toLowerCase();
			for (int i = 1; i < LONG_DAYS.length; i++) {
				String longDay = LONG_DAYS[i];
				DateConstraint nextComponent = null;
				if (request.contains(longDay) && request.contains(":")) {
					// special constraint: [day] before/after [time]
					// restrict if both on this day AND at this time-- so take the intersection
					DateConstraint day = parseDateConstraints(longDay, noTeam, new DateConstraint());
					DateConstraint time = parseDateConstraints(cleanRequest(request, longDay), noTeam, new DateConstraint());
					nextComponent = DateConstraint.getIntersection(day, time);
					components.add(nextComponent);
				}
			}
		}
		for (int i = 0; i < components.size(); i+=2) {
			// this means the description NEEDS to be paired-- the two constraints describing a day need to be adjacent
			DateConstraint c1 = components.get(i);
			DateConstraint c2 = components.get(i+1);
			DateConstraint c = DateConstraint.getIntersection(c1, c2);
			// and then OR all the components together
			standardSchedule = new DateConstraint(standardSchedule, c);
		}

		// FIXME -- bandaid fix, first day is always completely open?
		//standardSchedule.blockFirstDay();

		return standardSchedule;
	}
	private String cleanRequest(String r, String longDay) {
		String request = r.replace(longDay, "").trim();
		// don't even ask
		/*if (request.contains("before")) {
			request = request.replace("before", "after");
			if (request.substring(request.length() - 3, request.length()-2).equals("0")) {
				request = request.substring(0, request.length()-3) + "1" + request.substring(request.length()-2);
			}
		}
		else {
			request = request.replace("after", "before");
		}*/
		return request;
	}
}
