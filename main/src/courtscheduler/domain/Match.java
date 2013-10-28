package courtscheduler.domain;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningEntity
public class Match {

    // defining properties: teams
    private Team team1;
    private Team team2;
    MatchAvailability avail;   // calculated based on the intersection of the teams' availabilities
    private MatchSlot matchSlot;

    public void setMatchSlot(MatchSlot matchSlot) {
        this.matchSlot = matchSlot;
    }
    @PlanningVariable(valueRangeProviderRefs = "matchSlot", strengthComparatorClass = MatchStrengthComparator.class)
    public MatchSlot getMatchSlot() {
        return this.matchSlot;
    }
    public Match() {}
    public Match(Team team1, Team team2){
        this.team1 = team1;
        this.team2 = team2;
        avail = new MatchAvailability(team1.getAvailability(), team2.getAvailability());
    }

    public Team getT1(){
        return this.team1;
    }
    public Team getT2(){
        return this.team2;
    }
    public MatchDate getMatchDate(Calendar dateScale){
        MatchDate date = new MatchDate();
        dateScale.add(Calendar.DATE,matchSlot.getDay());
        date.setCal(dateScale);
        date.setDayOfWeek(DayOfWeek.valueOfCalendar(dateScale.get(Calendar.DAY_OF_WEEK)));
        return date;
    }
}
