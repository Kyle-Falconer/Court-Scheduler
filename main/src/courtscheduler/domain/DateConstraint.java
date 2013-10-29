package courtscheduler.domain;

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

    //constructors
    public DateConstraint(){
        dates = new boolean[365][24];
    }
    public DateConstraint(int days, int times){
        dates = new boolean[days][times];
    }

    //get functions
    public boolean[][] getDates(){
        return this.dates;
    }



    //set functions
    public void setDates(boolean[][] badDates){
        this.dates =badDates;
    }

    //merge functions
    public DateConstraint mergeDates(boolean[][] dates1, boolean[][] dates2){
        DateConstraint merge= new DateConstraint(dates1.length,dates1[0].length);
        for(int i=0;i<dates1.length;i++){
            for(int j=0;j<dates1[i].length;j++){
                //Shouldnt this below be "and" instead of "or"?
                //Trying to find out when two teams are available should be AND instead of OR -Will and Michael
                merge.dates[i][j]=dates1[i][j]&&this.dates[i][j];
            }
        }
        return merge;
    }

    //add functions
    //day/general adding
    public void addDate(int day, boolean[] times){
        //We changed this to dates[0].length because i was being used to represent the time dimension below.
        //dates[0] will return an array of times, which will allow i to go from 0 to 24 - Will & Michael
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
    //We changed the name to fit in with the addTime format. addTimes makes more sense
    //when describing what the method actually does - W&M
    public void addTimes(boolean[] times){
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
        return makeTimeArray(Integer.getInteger(start[0]),Integer.getInteger(end[0]));
    }
    public boolean[] makeTimeArray(MatchTime time){
        String[] start= time.getStartTime().split(":");
        String[] end= time.getEndTime().split(":");
        return makeTimeArray(Integer.getInteger(start[0]),Integer.getInteger(end[0]));
    }

    //conversion methods for days
    //Calendar date ->int date
    public int findDate(Calendar date){
        return date.get(Calendar.DAY_OF_YEAR);
    }

    //matchDate -> int day (calls calendar find date)
    public int findDate(MatchDate date){
        return findDate(date.getCal());
    }

    //string date->int day (calls calendar find date)
    public int findDate(String date){
        Calendar bCal = Calendar.getInstance();
        String[] bDate= date.split("/");
        bCal.set(Integer.valueOf(bDate[2]),Integer.valueOf(bDate[0]),Integer.valueOf(bDate[1]));
        return findDate(bCal);
    }
    //array of matchDate inputs -> array of int days
    public int[] findDates(List<MatchDate> dates){
        int[] days = new int[dates.size()];
        for(int i = 0; i < dates.size(); i++){
            days[i] = dates.get(i).getCal().get(Calendar.DAY_OF_YEAR);
        }
        return days;

    }
    //Calender range -> int[] days
    public int[] findDateRange(Calendar startDate, Calendar endDate){
        int dayCount=endDate.get(Calendar.DAY_OF_YEAR)-startDate.get(Calendar.DAY_OF_YEAR);
        int[] days = new int[dayCount];
        for(int i = 0;i<dayCount;i++){
            days[i] = startDate.get(Calendar.DAY_OF_YEAR);
            startDate.add(Calendar.DATE,1);
        }
        return days;
    }
    //string range -> int[] days (calls calendar range)
    public int[] findDateRange(String startDate, String endDate){
        String[] sDate= startDate.split("/");
        String[] eDate= endDate.split("/");
        Calendar sCal= Calendar.getInstance();
        //W&M - added try-catch block
        try {
            sCal.set(Integer.valueOf(sDate[2]),Integer.valueOf(sDate[0]),Integer.valueOf(sDate[1]));
            Calendar eCal= Calendar.getInstance();
            eCal.set(Integer.valueOf(eDate[2]),Integer.valueOf(eDate[0]),Integer.valueOf(eDate[1]));
            return this.findDateRange(sCal,eCal);
        }
        catch(NumberFormatException e){
            //this may need to be handled better - W&M
            e.printStackTrace();
            return new int[0];
        }

    }
}
