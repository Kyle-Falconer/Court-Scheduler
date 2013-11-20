import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.Match;
import courtscheduler.domain.MatchSlot;
import courtscheduler.domain.Team;
import courtscheduler.persistence.CourtScheduleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
/**
 * Created with IntelliJ IDEA.
 * User: CJ
 * Date: 11/20/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JUnit4.class)
public class teamIsPlayingOnDay {
    @Test
    public void eitherTeamToday() {
        ArrayList<Team> teams = new ArrayList<Team>();
        for(int i=0;i<10;i++){
            Team team = new Team();
            team.setTeamName("T"+i);
            team.setTeamId(i);
            teams.add(team);
        }
        CourtScheduleInfo info = new CourtScheduleInfo("config.ini");
        CourtSchedule test = new CourtSchedule(teams, info);
        List<Match> matches = test.getMatchList();
        bulkTest(0,0,matches, test);
        bulkTest(0,1,matches, test);


    }
    private void bulkTest(int daySet, int dayTest, List<Match> matches, CourtSchedule test){
        for(Match m : matches){
            //set times
            MatchSlot day = new MatchSlot(daySet,daySet,daySet);
            m.setMatchSlot(day);
        }
        boolean[] tests = new boolean[matches.size()];
        boolean[] answers = new boolean[matches.size()];
        for(int i=0;i<matches.size();i++){
            tests[i]=test.eitherTeamIsPlayingOnDay(matches.get(i), dayTest);
            answers[i]=daySet==dayTest;
            assertEquals("\" date:"+matches.get(i).getDate()+
                    " teams:"+matches.get(i).getTeam1().getTeamName()+":"+ matches.get(i).getTeam1().getTeamName()+
                    "\" must be \""+answers[i]+"\"", answers[i], tests[i]);
        }
    }
}
