package courtscheduler.domain;

import org.joda.time.LocalDate;
import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchDate implements Comparable<MatchDate>{

    private LocalDate date;
    private DayOfWeek dayOfWeek;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }


    @Override
    public int compareTo(MatchDate otherDate) {
        return this.date.compareTo(otherDate.getDate());  // FIXME:  is this sufficient?
    }
}
