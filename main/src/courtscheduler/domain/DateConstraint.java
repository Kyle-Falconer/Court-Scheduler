package courtscheduler.domain;

import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

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
    private boolean restrictedDates[][];

    //constructors
    public DateConstraint(){
        restrictedDates= new boolean[365][24];
    }
    public DateConstraint(int days, int times){
        restrictedDates= new boolean[days][times];
    }

    //get functions
    public boolean[][] getRestrictedDates(){
        return this.restrictedDates;
    }



    //set functions
    public void setRestrictedDates(boolean[][] badDates){
        this.restrictedDates=badDates;
    }


    //add functions
    //day/general adding
    public void addRestrictedDate(int day, boolean[] times){
        for(int i=0;i<this.restrictedDates.length;i++){
            //and if true=ok, or if false=ok
            this.restrictedDates[day][i]= (times[i]&&this.restrictedDates[day][i]);
        }
    }
    //specific slot add
    public void addRestrictedTime(int day, int time){
        this.restrictedDates[day][time]=false;
    }

    //wrappers for other input types
    // no time
    public void addRestrictedDate(int day){
        boolean[] noTimes  =new boolean[this.restrictedDates.length];
        for(int i=0; i<noTimes.length;i++){
            noTimes[i]=false;
        }
        this.addRestrictedDate(day,noTimes);
    }
    //no day
    public void addRestrictedTimes(boolean[] times){
        for(int i=0; i<this.restrictedDates.length;i++){
            this.addRestrictedDate(i,times);
        }
    }
    //array of days
    public void addRestrictedDates(int days[], boolean[] times){
        for(int i=0;i<days.length;i++){
            this.addRestrictedDate(days[i],times);
        }
    }

    //conversion methods for times
    //int[] (0-24 with 1=1:00, size should not exceed 24, but need not be in order or full) ->boolean[]
    public boolean[] makeTimeArray(int[] times){
        boolean[] timeArray = new boolean[this.restrictedDates[0].length];
        for(int i=0;i<times.length;i++){
            timeArray[times[i]]=false;
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

    //conversion methods for days
    //Calendar date ->int date
    public int findDate(Calendar badDate){
        return badDate.get(Calendar.DAY_OF_YEAR);
    }

    //matchDate -> int day (calls calendar find date)
    public int findDate(MatchDate badDate){
        return findDate(badDate.getCal());
    }
    //string date->int day (calls calendar find date)
    public int getDate(String badDate){
        Calendar bCal = Calendar.getInstance();
        String[] bDate= badDate.split("/");
        bCal.set(Integer.valueOf(bDate[2]),Integer.valueOf(bDate[0]),Integer.valueOf(bDate[1]));
        return findDate(bCal);
    }
    //array of matchDate inputs -> array of int days
    public int[] findDates(List<MatchDate> badDates){
        int[] days= new int[badDates.size()];
        for(int i=0;i<badDates.size();i++){
            days[i]=badDates.get(i).getCal().get(Calendar.DAY_OF_YEAR);
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
}
