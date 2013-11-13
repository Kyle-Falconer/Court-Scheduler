import courtscheduler.persistence.CourtScheduleInfo;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static courtscheduler.persistence.CourtScheduleIO.getMilitaryTime;
import static courtscheduler.persistence.CourtScheduleIO.isAfternoon;
import static courtscheduler.persistence.CourtScheduleInfo.parseDateString;
import static courtscheduler.persistence.CourtScheduleInfo.timeStringToMinutes;
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
    public void afternoon(){
        String[][] tests = {
                {"12:00 a.m.", "false"},
                {"8:00 a.m.", "false"},
                {"8:01 PM", "true"},
                {"4:00pm", "true"},
                {"11:59 p.m.", "true"},
                {"14:00", "true"},
                {"12:00", "true"},
                {"0:00", "false"}
        };
        for (String[] test : tests){
            assertEquals("\""+test[0]+"\" must be "+test[1], test[1], isAfternoon(test[0])+"");
        }
    }

    @Test
    public void timeToMinutes(){
        String[][] tests = {
                {"12:00 a.m.", "0"},
                {"8:00 a.m.", "480"},
                {"8:01 PM", "1201"},
                {"4:00pm", "960"},
                {"11:59 p.m.", "1439"},
                {"11:59", "719"},
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

    @Test
    public void timeIndices(){
        CourtScheduleInfo info = new CourtScheduleInfo("");
        info.setTimeslotDuration("60");
        info.setTimeslotMidnightOffset("4:00pm");
        info.setNumberOfTimeSlotsPerDay("4");

        String[][] ceilTests = {
                {"6:00pm", "2"},
                {"4:00pm", "0"},
                {"12:00am", "-1"},
                {"8:00pm", "-1"},
				{"4:30pm", "0"},
				{"6:30pm", "2"}
        };
        for (String[] test : ceilTests){
            assertEquals("\""+test[0]+"\" must be "+test[1], test[1], info.getCeilingTimeIndex(test[0]).toString());
        }
		String[][] floorTests = {
				{"6:00pm", "2"},
				{"4:00pm", "0"},
				{"12:00am", "-1"},
				{"8:00pm", "-1"},
				{"4:30pm", "1"},
				{"6:30pm", "3"}
		};
		for (String[] test : floorTests){
			assertEquals("\""+test[0]+"\" must be "+test[1], test[1], info.getFloorTimeIndex(test[0]).toString());
		}
    }

}
