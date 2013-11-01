package courtscheduler.domain;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: CJ
 * Date: 10/14/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateConstraint extends Constraint{
    private boolean dates[][];

	private static CourtScheduleInfo info;
	private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("M/d/yy");

    //constructors
    public DateConstraint(){
        dates = new boolean[info.getNumberOfConferenceDays()][info.getNumberOfTimeSlotsPerDay()];
    }
	public DateConstraint(DateConstraint a, DateConstraint b) {
		this();
		this.mergeDates(a, b);
	}

    //get functions
    public boolean getDate(int day, int timeSlot){
		// This is inverted because Java auto-initializes all boolean arrays to hold all false.
		// So, since we assume they can play all times by default, we use "false" to mean it's okay,
		// and "true" to mean they can't play then.
		// Confusing, I know, but the performance benefit is nontrivial. --MS
        return !this.dates[day][timeSlot];
    }
	public boolean isTrue(MatchSlot matchSlot) {
		return this.getDate(matchSlot.getDay(), matchSlot.getTime());
	}


    //set functions
    public void setDate(int day, int timeSlot, boolean canPlay){
        this.dates[day][timeSlot] = !canPlay;
    }

	public void setStringDate(String date, boolean canPlay) {
		LocalDate localDate = LocalDate.parse(date, dateFormat);

		if (info.dayIsInConference(localDate)) {
			int dayOfConference = Days.daysBetween(info.getStartingDay(), localDate).getDays();
			this.markContiguousDays(dayOfConference, !canPlay, 1);
		}
		else {
			System.err.println("Day " + date + " is not in conference");
		}
	}

	public void setStringDates(String start, String end, boolean canPlay) {
		LocalDate periodStartDate = LocalDate.parse(start, dateFormat);
		LocalDate periodEndDate = LocalDate.parse(end, dateFormat);

		if (info.dayIsInConference(periodStartDate) && info.dayIsInConference(periodEndDate)) {
			int dayOfConference = Days.daysBetween(info.getStartingDay(), periodStartDate).getDays();
			int numOfDays = Days.daysBetween(periodStartDate, periodEndDate).getDays();
			this.markContiguousDays(dayOfConference, !canPlay, numOfDays+1);
		}
		else {
			System.err.println("Day " + start + " or " + end + " is not in conference");
		}
	}

	private void markContiguousDays(int dayOfConference, boolean canPlay, int dayCount) {
		for (int i = 0; i < dayCount; i++) {
			boolean[] markedArray = new boolean[info.getNumberOfTimeSlotsPerDay()];
			// mark array as "can't play" for the entire day
			for (int j = 0; j < markedArray.length; j++) {
				markedArray[j] = canPlay;
			}
			dates[dayOfConference+i] = markedArray;
			System.out.println("Can't play on day " + (dayOfConference+i));
		}
	}

    //merge functions
    private void mergeDates(DateConstraint a, DateConstraint b){
		boolean[][] dates1 = a.dates;
		boolean[][] dates2 = b.dates;
        for(int i=0;i<dates1.length;i++){
            for(int j=0;j<dates1[i].length;j++){
                this.dates[i][j]=dates1[i][j] || dates2[i][j];
            }
        }
    }

    //add functions
    //day/general adding
    public void addDate(int day, boolean[] times){
        for(int i=0;i<this.dates[0].length;i++){
            //and if true=ok, or if false=ok
            this.dates[day][i]= (times[i]||this.dates[day][i]);
        }
    }
    //specific slot add
    public void addTime(int day, int time){
        this.dates[day][time]=true;
    }

    //wrappers for other input types
    // no time
    public void addDate(int day){
        boolean[] noTimes = makeTimeArray(0,24);
        this.addDate(day, noTimes);
    }
    //no day
    public void addRestrictedTimes(boolean[] times){
        for(int i=0; i<this.dates.length;i++){
            this.addDate(i,times);
        }
    }
    //array of days
    public void addDates(int days[], boolean[] times){
        for(int i=0;i<days.length;i++){
            this.addDate(days[i], times);
        }
    }
    public void addDates(int days[]){
        for(int i=0;i<days.length;i++){
            this.addDate(days[i]);
        }
    }

    //conversion methods for times
    //int[] (0-24 with 1=1:00, size should not exceed 24, but need not be in order or full) ->boolean[]
    public boolean[] makeTimeArray(int[] times){
        boolean[] timeArray = new boolean[this.dates[0].length];
        for(int i=0;i<times.length;i++){
            timeArray[times[i]]=true;
        }
        return timeArray;
    }
    //start/end times -> boolean[]
    public boolean[] makeTimeArray(int startTime, int endTime){
        int[] times= new int[endTime-startTime];
        for(int i=0;i<(endTime-startTime);i++){
            times[i]=startTime+i;
        }
        return makeTimeArray(times);
    }
    public boolean[] makeTimeArray(String startTime, String endTime){
        String[] start= startTime.split(":");
        String[] end= endTime.split(":");
        return makeTimeArray(Integer.valueOf(start[0]),Integer.valueOf(end[0]));
    }
    public boolean[] makeTimeArray(MatchTime time){
        return makeTimeArray(time.getStartTime(),time.getEndTime());
    }

    //conversion methods for days
    //Calendar date ->int date
    public int findDate(Calendar Date){
        return Date.get(Calendar.DAY_OF_YEAR);
    }

    //matchDate -> int day (calls calendar find date)
    public int findDate(MatchDate Date){
        return findDate(Date.getCal());
    }
    //string date->int day (calls calendar find date)
    public int findDate(String Date){
        Calendar bCal = Calendar.getInstance();
        String[] bDate= Date.split("/");
        bCal.set(Integer.valueOf(bDate[2]),Integer.valueOf(bDate[0]),Integer.valueOf(bDate[1]));
        return findDate(bCal);
    }
    //array of matchDate inputs -> array of int days
    public int[] findDates(List<MatchDate> Dates){
        int[] days= new int[Dates.size()];
        for(int i=0;i<Dates.size();i++){
            days[i]=Dates.get(i).getCal().get(Calendar.DAY_OF_YEAR);
        }
        return days;

    }
    //Calender range -> int[] days
    public int[] findDateRange(Calendar startDate, Calendar endDate){
        int dayCount=endDate.get(Calendar.DAY_OF_YEAR)-startDate.get(Calendar.DAY_OF_YEAR);
        int[] days= new int[dayCount];
        for(int i=0;i<dayCount;i++){
            days[i]=startDate.get(Calendar.DAY_OF_YEAR);
            startDate.add(Calendar.DATE,1);
        }
        return days;
    }
    //string range -> int[] days (calls calendar range)
    public int[] findDateRange(String startDate, String endDate){
        String[] sDate= startDate.split("/");
        String[] eDate= endDate.split("/");
        Calendar sCal= Calendar.getInstance();
        sCal.set(Integer.valueOf(sDate[2]),Integer.valueOf(sDate[0]),Integer.valueOf(sDate[1]));
        Calendar eCal= Calendar.getInstance();
        eCal.set(Integer.valueOf(eDate[2]),Integer.valueOf(eDate[0]),Integer.valueOf(eDate[1]));
        return this.findDateRange(sCal,eCal);
    }

	public static void setInfo(CourtScheduleInfo info) {
		DateConstraint.info = info;
	}
}
