package courtscheduler.domain;

import courtscheduler.persistence.CourtScheduleInfo;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
        try {
            return !this.dates[day][timeSlot];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Date/time requested is outside the start-end dates of the conference");
            return true;
        }
    }
	public boolean isTrue(MatchSlot matchSlot) {
		return this.getDate(matchSlot.getDay(), matchSlot.getTime());
	}


    //set functions
    public void setDate(int day, int timeSlot, boolean canPlay){
        this.dates[day][timeSlot] = !canPlay;
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
            try {
                this.dates[day][i] = (times[i]||this.dates[day][i]);
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Date/time requested is outside the start-end dates of the conference");
            }
        }
    }

    //specific slot add
    public void addTime(int day, int time){
        try {
            this.dates[day][time]=true;
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Date/time requested is outside the start-end dates of the conference");
        }
    }

    //wrappers for other input types
    // no time
    public void addDate(Integer day){
        if(day!=null){
            boolean[] noTimes = makeTimeArray(0,dates[0].length);
            this.addDate(day, noTimes);
        }
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
            //System.out.println(days[i]);
            this.addDate(days[i]);
        }
    }

    //conversion methods for times
    //int[] (0-24 with 1=1:00, size should not exceed 24, but need not be in order or full) ->boolean[]
    public boolean[] makeTimeArray(int[] times){
        boolean[] timeArray = new boolean[this.dates[0].length];
        for(int i=0;i<times.length;i++){
            //System.out.println(i+":"+times[i]);
            timeArray[times[i]]=true;
        }
        return timeArray;
    }
    //start/end times -> boolean[]
    public boolean[] makeTimeArray(int startTime, int endTime){
        int timeRange =Math.abs(endTime-startTime);
        //System.out.println(timeRange);
        int[] times= new int[timeRange];
        for(int i=0;i<timeRange;i++){
            int timeslot=startTime+i;
            //System.out.println(startTime+i);
            if(timeslot>=0){
                times[i]=timeslot;
            }
        }
        return makeTimeArray(times);
    }
    public boolean[] makeTimeArray(String startTime, String endTime){
        int start=info.getTimeIndex(startTime);
        int end=info.getTimeIndex(endTime);
        //System.out.println(start+":"+end);
        return makeTimeArray(start, end);
    }
    public boolean[] makeTimeArray(MatchTime time){
        return makeTimeArray(time.getStartTime(),time.getEndTime());
    }

    //conversion methods for days

    //matchDate -> int day (calls calendar find date)
    public int findDate(MatchDate Date){
        return findDate(Date.getDate());
    }

    //string date->int day (calls calendar find date)
    public Integer findDate(String date){
        LocalDate ldate= LocalDate.parse(date, dateFormat);
        return findDate(ldate);
    }

    public Integer findDate(LocalDate date){
        //System.out.println(Days.daysBetween(info.getStartingDay(), Date).getDays());
        int day = Days.daysBetween(info.getConferenceStartDate(), date).getDays();
        int conferenceLength= info.getNumberOfConferenceDays();
        if(day< 0){
            System.out.println("ERROR: date "+date+" is before conference start.");
            return null;
        }
        if(day>conferenceLength){
            System.out.println("ERROR: date "+date+" is after conference end.");
            return null;
        }
        return day-1;
    }
    //array of matchDate inputs -> array of int days
    public int[] findDates(List<MatchDate> Dates){
        int[] days= new int[Dates.size()];
        for(int i=0;i<Dates.size();i++){
            days[i]=findDate(Dates.get(i).getDate());
        }
        return days;

    }


    //string range -> int[] days (calls calendar range)
    public int[] findDateRange(String startDate, String endDate){
        LocalDate sdate= LocalDate.parse(startDate, dateFormat);
        LocalDate edate= LocalDate.parse(endDate, dateFormat);
        return this.findDateRange(sdate,edate);
    }

    public int[] findDateRange(LocalDate sdate, LocalDate edate){
        int dayCount=Days.daysBetween(sdate, edate).getDays();
        //System.out.println(dayCount);
        int[] days= new int[dayCount+1];
        for(int i=0; i<=dayCount; i++){
            Integer day= findDate(sdate.plusDays(i));
            if(day !=null){
                days[i]=day;
            }
        }
        return days;
    }

    public int[] findDayOfWeek(String weekday){
        Integer i=null;
        if(weekday.equals("monday")){
            i=1;
        }
        else if(weekday.equals("tuesday")){
            i=2;
        }
        else if(weekday.equals("wednesday")){
            i=3;
        }
        else if(weekday.equals("thursday")){
            i=4;
        }
        else if(weekday.equals("friday")){
            i=5;
        }
        else if(weekday.equals("saturday")){
            i=6;
        }
        else if(weekday.equals("sunday")){
            i=7;
        }
        if(i!=null){
            return findDayOfWeek(i);
        }
        else{
            System.out.println("Error: day of week "+weekday+" is not recognized");
            int[] fail = new int[0];
            return fail;
        }
    }

    public int[] findDayOfWeek(int weekday){
        LocalDate firstDay=info.getConferenceStartDate().withDayOfWeek(weekday);;
        int[] days= new int[Weeks.weeksBetween(firstDay, info.getConferenceEndDate()).getWeeks()];
        for(int i=0; i<days.length;i++){
            days[i]= findDate(firstDay.plusWeeks(i));
        }
        return days;
    }

	public static void setInfo(CourtScheduleInfo info) {
		DateConstraint.info = info;
	}
}
