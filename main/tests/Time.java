import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static courtscheduler.persistence.CourtScheduleInfo.parseDateString;
import static courtscheduler.persistence.CourtScheduleInfo.timeStringToMinutes;
import static courtscheduler.persistence.CourtScheduleIO.getMilitaryTime;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 11/11/13
 * Time: 10:24 AM
 */
@RunWith(JUnit4.class)
public class Time {

    @Test
    public void military() {

        String[][] tests = {
                {"6:00pm", "18:00"},
                {"12:00am", "0:00"},
                {"12:00 a.m.", "0:00"},
                {"11:59 p.m.", "23:59"}
        };
        for (String[] test : tests){
            assertEquals("\""+test[0]+"\" must be \""+test[1]+"\"", test[1], getMilitaryTime(test[0]).toString());
        }
    }

    @Test
    public void timeToMinutes(){
        String[][] tests = {
                {"12:00 a.m.", "0"},
                {"8:00 a.m.", "480"},
                {"4:00pm", "960"},
                {"11:59 p.m.", "1439"}
        };
        for (String[] test : tests){
            assertEquals("\""+test[0]+"\" must be "+test[1], test[1], timeStringToMinutes(test[0])+"");
        }
    }

    @Test
    public void dateParse(){
        String[][] tests = {
                {"1/1/2014", new LocalDate(2014, 1, 1).toString()},
                {"1-1-2014", new LocalDate(2014, 1, 1).toString()},
                {"12-31-2014", new LocalDate(2014, 12, 31).toString()}
        };
        for (String[] test : tests){
            assertEquals("\""+test[0]+"\" must be \""+test[1]+"\"", test[1], parseDateString(test[0]).toString());
        }
    }
}
