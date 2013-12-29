import courtscheduler.domain.Match;
import courtscheduler.domain.Team;
import courtscheduler.persistence.CourtScheduleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Zetta
 * Date: 12/28/13
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(JUnit4.class)
public class PrimaryDayDetection {
	@Test
	public void runTests() {
		CourtScheduleInfo info = new CourtScheduleInfo("main/tests/primarydayconfig.ini");
		info.configure();

		Team firstGrade1 = new Team();
		firstGrade1.setGrade("1");
		Team firstGrade2 = new Team();
		firstGrade2.setGrade("1");
		Team secondGrade = new Team();
		secondGrade.setGrade("2");
		Team thirdGrade = new Team();
		thirdGrade.setGrade("3");


		Match[] matches = new Match[] {
				new Match(firstGrade1, firstGrade2),
				new Match(firstGrade1, secondGrade),
				new Match(secondGrade, firstGrade2),
				new Match(thirdGrade, firstGrade1)
		};
		Integer[][] results = new Integer[][] {
				{1, 3},
				{3},
				{3},
				{}
		};
		for (int i = 0; i < matches.length; i++) {
			Match m = matches[i];
			Object[] actual = m.getPrimaryDays().toArray();
			Object[] expected = results[i];

			assertArrayEquals("First graders", actual, expected);
		}

	}


}
