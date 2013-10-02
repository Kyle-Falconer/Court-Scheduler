package courtscheduler.domain;

import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchDate {

    private String date;
    private DayOfWeek dayOfWeek;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
