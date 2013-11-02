package courtscheduler.domain;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourtScheduleInfo {
	private LocalDate startingDay;
	private LocalDate endingDay;
	private int numberOfCourts;
	private int timeslotMidnightOffsetInMinutes;
	private int numberOfTimeSlotsPerDay;
	private int timeslotDurationInMinutes;

 	public CourtScheduleInfo(String filepath) {
		// TODO read these from a file
		startingDay = new LocalDate( 2014, 1, 1);
		endingDay = new LocalDate( 2014, 1, 15);
		numberOfCourts = 3;
		timeslotMidnightOffsetInMinutes = 420;  // 7am
		numberOfTimeSlotsPerDay = 16;  // end at ~8:30pm
		timeslotDurationInMinutes = 50;
		DateConstraint.setInfo(this);
	}

	public int getNumberOfConferenceDays() {
		return Days.daysBetween(startingDay, endingDay).getDays();
	}

	public LocalDate getStartingDay() {
		return startingDay;
	}
	public LocalDate getEndingDay() {
		return endingDay;
	}
	public int getNumberOfTimeSlotsPerDay() {
		return numberOfTimeSlotsPerDay;
	}
	public int getNumberOfCourts() {
		return numberOfCourts;
	}
	public boolean dayIsInConference(LocalDate date) {
		return startingDay.isBefore(date) && endingDay.isAfter(date);
	}

	public int getFinalTimeSlotIndex() {
		return numberOfTimeSlotsPerDay - 1;
	}
}
