import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static courtscheduler.domain.CourtScheduleInfo.timeStringToMinutes;
import static courtscheduler.persistence.CourtScheduleIO.getMilitaryTime;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 11/11/13
 * Time: 10:24 AM
 */
@RunWith(JUnit4.class)
public class time {

    @Test
    public void military() {
        assertEquals("6:00pm must be 18:00 military time", "18:00", getMilitaryTime("6:00pm"));
        assertEquals("12:00am must be 0:00 military time", "0:00", getMilitaryTime("12:00am"));
        assertEquals("12:00 a.m. must be 0:00 military time", "0:00", getMilitaryTime("12:00 a.m."));
        assertEquals("11:59 p.m. must be 23:59 military time", "23:59", getMilitaryTime("11:59 p.m."));
    }

    @Test
    public void timeToMinutes(){
        assertEquals("12:00 a.m. must be 0 minutes", 0, timeStringToMinutes("12:00 a.m."));
        assertEquals("8:00 a.m. must be 480 minutes", 480, timeStringToMinutes("8:00 a.m."));
        assertEquals("4:00pm must be 480 minutes", 960, timeStringToMinutes("4:00pm"));
        assertEquals("11:59 p.m. must be 1439 minutes", 1439, timeStringToMinutes("11:59 p.m."));
    }
}
