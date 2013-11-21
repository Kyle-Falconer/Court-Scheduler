import courtscheduler.domain.DateConstraint;
import courtscheduler.domain.Team;
import courtscheduler.persistence.CourtScheduleIO;
import courtscheduler.persistence.CourtScheduleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 11/11/13
 * Time: 10:24 AM
 */
@RunWith(JUnit4.class)
public class Parser {

    @Test
    public void parser() throws Exception {
        CourtScheduleInfo info = new CourtScheduleInfo("config.ini");
        if(info.configure() == -1){
            System.out.println("config file issue!");
            return;
        }

        CourtScheduleIO  test = new CourtScheduleIO(info);
        List<Team> teams = test.readXlsx("Raw/Excel Files/test/testBook.xlsx", info);
        Team[] team = new Team[9];
        team[0]=team1();
        team[1]=team2();
        team[2]=team3();
        team[3]=team4();
        team[4]=team5();
        team[5]=team6();
        team[6]=team7();
        team[7]=team8();
        team[8]=team9();




        for (int i=0; i<9; i++){
            Team t1= teams.get(i);
            int tId1=t1.getTeamId();
            int tId2=team[i].getTeamId();
            assertEquals("\""+tId1+"\" must be \""+tId2+"\"", tId2, tId1);

            String tName1=t1.getTeamName();
            String tName2=team[i].getTeamName();
            assertEquals("\""+tName1+"\" must be \""+tName2+"\"", tName2, tName1);

            DateConstraint tPDates1=t1.getPreferredDates();
            DateConstraint tPDates2=team[i].getPreferredDates();
            assertEquals("\""+tPDates1+"\" must be \""+tPDates2+"\"", tPDates2, tPDates1);

            DateConstraint tBDates1=t1.getBadDates();
            DateConstraint tBDates2=team[i].getBadDates();
            assertEquals("\""+tBDates1+"\" must be \""+tBDates2+"\"", tBDates2,tBDates1);

            DateConstraint tODates1=t1.getOnlyDates();
            DateConstraint tODates2=team[i].getOnlyDates();
            assertEquals("\""+tODates1+"\" must be \""+tODates2+"\"", tODates2, tODates1);

            int tGrade1=t1.getGrade();
            int tGrade2=team[i].getGrade();
            assertEquals("\""+tGrade1+"\" must be \""+tGrade2+"\"", tGrade2, tGrade1);

            String tGender1=t1.getGender();
            String tGender2=team[i].getGender();
            assertEquals("\""+tGender1+"\" must be \""+tGender2+"\"", tGender2, tGender1);

            String tYear1=t1.getYear();
            String tYear2=team[i].getYear();
            assertEquals("\""+tYear1+"\" must be \""+tYear2+"\"", tYear2, tYear1);

            String tConference1=t1.getConference();
            String tConference2=team[i].getConference();
            assertEquals("\""+tConference1+"\" must be \""+tConference2+"\"", tConference2, tConference1);

            String tLevel1=t1.getLevel();
            String tLevel2=team[i].getLevel();
            assertEquals("\""+tLevel1+"\" must be \""+tLevel2+"\"", tLevel2, tLevel1);
        }
    }

    private Team team1(){
        Team team = new Team();
        team.setTeamId(101);
        team.setTeamName("Evil Douches");

        team.setPreferredDates(new DateConstraint());
        team.setBadDates(new DateConstraint());
        team.setOnlyDates(null);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("R");
        return team;
    }
    private Team team2(){
        Team team = new Team();
        team.setTeamId(102);
        team.setTeamName("No Nonsense");
        DateConstraint dates = new DateConstraint();
        dates.addDates(dates.findDayOfWeek("wednesday"));
        dates.addDate(dates.findDate("1/10/14"));
        dates.addRestrictedTimes(dates.makeTimeArray("17:40", "18:40"));
        team.setPreferredDates(new DateConstraint());
        team.setBadDates(dates);
        team.setOnlyDates(null);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("C");
        return team;
    }
    private Team team3(){
        Team team = new Team();
        team.setTeamId(103);
        team.setTeamName("Only Offensive");
        DateConstraint dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("19:00","20:00"));
        dates.addRestrictedTimes(dates.makeTimeArray("0:00","12:00"));
        dates.addDates(dates.findDayOfWeek("wednesday"));
        team.setPreferredDates(new DateConstraint());
        team.setBadDates(new DateConstraint());
        team.setOnlyDates(dates);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("R");
        return team;
    }
    private Team team4(){
        Team team = new Team();
        //team.setTeamId(101);
        team.setTeamName("Anonymous Arses");

        team.setPreferredDates(new DateConstraint());
        team.setBadDates(new DateConstraint());
        team.setOnlyDates(null);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("I");
        return team;
    }
    private Team team5(){
        Team team = new Team();
        team.setTeamId(105);
        team.setTeamName("Preferred Procreation");
        DateConstraint dates = new DateConstraint();
        dates.addDates(dates.findDayOfWeek("thursday"));
        dates.addRestrictedTimes(dates.makeTimeArray("7:00", "8:00"));
        dates.addRestrictedTimes(dates.makeTimeArray("7:00","23:59"));
        team.setPreferredDates(dates);
        team.setBadDates(new DateConstraint());
        team.setOnlyDates(null);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("R");
        return team;
    }
    private Team team6(){
        Team team = new Team();
        team.setTeamId(106);
        team.setTeamName("Before Battery");
        DateConstraint dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("0:00","15:00"));
        team.setPreferredDates(dates);
        dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("0:00","10:00"));
        team.setBadDates(dates);
        dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("0:00","19:00"));
        team.setOnlyDates(dates);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("C");
        return team;
    }
    private Team team7(){
        Team team = new Team();
        team.setTeamId(107);
        team.setTeamName("After Awkward");

        DateConstraint dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("15:00","23:59"));
        team.setPreferredDates(dates);
        dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("18:00","23:59"));
        team.setBadDates(dates);
        dates = new DateConstraint();
        dates.addRestrictedTimes(dates.makeTimeArray("10:00", "23:59"));
        team.setOnlyDates(dates);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("C");
        return team;
    }
    private Team team8(){
        Team team = new Team();
        team.setTeamId(108);
        team.setTeamName("Date Devils");

        DateConstraint dates = new DateConstraint();
        dates.addDate(dates.findDate("1/12/14"));
        team.setPreferredDates(dates);
        dates = new DateConstraint();
        dates.addDate(dates.findDate("1/1/14"));
        team.setBadDates(new DateConstraint());
        dates = new DateConstraint();
        dates.addDates(dates.findDateRange("1/10/14","1/13/14"));
        team.setOnlyDates(dates);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("C");
        return team;
    }
    private Team team9(){
        Team team = new Team();
        team.setTeamId(109);
        team.setTeamName("Weekday Wonders");

        DateConstraint dates = new DateConstraint();
        dates.addDates(dates.findDayOfWeek("monday"));
        team.setPreferredDates(dates);
        dates = new DateConstraint();
        dates.addDates(dates.findDayOfWeek("tuesday"));
        team.setBadDates(dates);
        dates = new DateConstraint();
        dates.addDates(dates.findDayOfWeek("saturday"));
        dates.addDates(dates.findDayOfWeek("sunday"));
        dates.addDates(dates.findDayOfWeek("monday"));
        team.setOnlyDates(dates);

        team.setGrade(1);
        team.setGender("B");
        team.setYear("");

        team.setConference("1");
        team.setLevel("C");
        return team;
    }

}