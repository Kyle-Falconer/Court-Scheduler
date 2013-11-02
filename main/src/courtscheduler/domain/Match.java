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

    public Match() {
    }

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        avail = new MatchAvailability(team1.getAvailability(), team2.getAvailability());
    }

    public Team getTeam1() {
        return this.team1;
    }

    public Integer getTeam1Id(){
        return this.team1.getTeamId();
    }

    public Team getTeam2() {
        return this.team2;
    }

    public Integer getTeam2Id(){
        return this.team2.getTeamId();
    }

    public MatchDate getMatchDate(Calendar dateScale) {
        MatchDate date = new MatchDate();
        dateScale.add(Calendar.DATE, matchSlot.getDay());
        date.setCal(dateScale);
        date.setDayOfWeek(DayOfWeek.valueOfCalendar(dateScale.get(Calendar.DAY_OF_WEEK)));
        return date;
    }

    public Integer getTime() {
        return this.getMatchSlot().getTime();
    }
	public Integer getDate() {
        return this.getMatchSlot().getDay();
    }
	public Integer getCourt() {
		return this.getMatchSlot().getCourt();
	}
	public String getGender() {
		return team1.getGender(); // FIXME
	}

	public Match getThis() {
		// Why in the hell do we need this? So that the Drools Rules engine can access this.
		// Yeah, it sucks, but it's a lot easier than writing the arcane invocations of DRL, so. --MS
		return this;
	}

    public boolean containsTeamsFrom(Match other) {
        return this.team1.equals(other.team1) || this.team1.equals(other.team2) ||
                this.team2.equals(other.team1) || this.team2.equals(other.team2);
    }

	public int getBasketHeight() {
		return team1.getGrade(); // FIXME
	}

	public String toString() {
		return new StringBuilder()
				.append("[")
				.append(team1.getTeamId())
				.append("v")
				.append(team2.getTeamId())
				.append(" ")
				.append(matchSlot)
				.append("]")
				.toString();
	}

	public boolean getCanPlayInCurrentSlot() {
		return avail.canPlayIn(matchSlot);
	}
}
