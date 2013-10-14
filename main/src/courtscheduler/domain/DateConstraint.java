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
    private List<MatchDate> restrictedDates;

    public List<MatchDate> getRestrictedDates(){
        return this.restrictedDates;
    }
    public void setRestrictedDates(List<MatchDate> badDates){
        this.restrictedDates=badDates;
    }
    public void addRestrictedDate(MatchDate badDate){
        this.restrictedDates.add(badDate);
    }
    public void addRestrictedDates(List<MatchDate> badDates){
        for(int i=0;i<badDates.size();i++){
            addRestrictedDate(badDates.get(i));
        }
    }
    public void addRestrictedDates(Calendar startDate, Calendar endDate){
        MatchDate badDate = new MatchDate();
        while(!(startDate.get(Calendar.MONTH)==endDate.get(Calendar.MONTH) &&
                !(startDate.get(Calendar.DATE)>endDate.get(Calendar.DATE))&&
                startDate.get(Calendar.YEAR)==endDate.get(Calendar.YEAR))){
            badDate.setCal(startDate);
            switch(startDate.get(Calendar.DAY_OF_WEEK)){
                case 1:
                    badDate.setDayOfWeek(DayOfWeek.SUNDAY);
                    break;
                case 2:
                    badDate.setDayOfWeek(DayOfWeek.MONDAY);
                    break;
                case 3:
                    badDate.setDayOfWeek(DayOfWeek.TUESDAY);
                    break;
                case 4:
                    badDate.setDayOfWeek(DayOfWeek.WEDNESDAY);
                    break;
                case 5:
                    badDate.setDayOfWeek(DayOfWeek.THURSDAY);
                    break;
                case 6:
                    badDate.setDayOfWeek(DayOfWeek.FRIDAY);
                    break;
                case 7:
                    badDate.setDayOfWeek(DayOfWeek.SATURDAY);
            }
            this.restrictedDates.add(badDate);
            startDate.add(Calendar.DATE,1);

        }
    }
}
