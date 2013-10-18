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
    public void addRestrictedDate(Calendar badDate){
        MatchDate bDate = new MatchDate();
        bDate.setCal(badDate);
		bDate.setDayOfWeek(getDayOfWeek(badDate));
        this.restrictedDates.add(bDate);
    }
    public void addRestrictedDate(String badDate){
        Calendar bCal = Calendar.getInstance();
        String[] bDate= badDate.split("/");
        bCal.set(Integer.valueOf(bDate[2]),Integer.valueOf(bDate[0]),Integer.valueOf(bDate[1]));
        this.addRestrictedDate(bCal);
    }
    public void addRestrictedDates(List<MatchDate> badDates){
        for(int i=0;i<badDates.size();i++){
            addRestrictedDate(badDates.get(i));
        }
    }
    public void addRestrictedDates(Calendar startDate, Calendar endDate){
        MatchDate badDate = new MatchDate();
        while(startDate.before(endDate)){
            badDate.setCal(startDate);
			badDate.setDayOfWeek(getDayOfWeek(startDate));
            this.restrictedDates.add(badDate);
            startDate.add(Calendar.DATE,1);

        }
    }
    public void addRestrictedDates(String startDate, String endDate){
        String[] sDate= startDate.split("/");
        String[] eDate= endDate.split("/");
        Calendar sCal= Calendar.getInstance();
        sCal.set(Integer.valueOf(sDate[2]),Integer.valueOf(sDate[0]),Integer.valueOf(sDate[1]));
        Calendar eCal= Calendar.getInstance();
        eCal.set(Integer.valueOf(eDate[2]),Integer.valueOf(eDate[0]),Integer.valueOf(eDate[1]));
        this.addRestrictedDates(sCal,eCal);
    }
	private DayOfWeek getDayOfWeek(Calendar date) {
		// DayOfWeek class is zero-indexed, and Java's Calendar is one-indexed
		return DayOfWeek.values()[date.get(Calendar.DAY_OF_WEEK) - 1];
	}
}
