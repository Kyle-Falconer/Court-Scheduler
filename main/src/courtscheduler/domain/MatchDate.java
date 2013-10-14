package courtscheduler.domain;

import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchDate {

    private Calendar cal;
    private DayOfWeek dayOfWeek;

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public Calendar getCal() {
        return cal;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
