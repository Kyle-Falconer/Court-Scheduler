package courtscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningEntity
public class Match extends AbstractPersistable {
    private Team team1;
    private Team team2;
    private Match match;
    private MatchTime matchTime;
    private MatchDate matchDate;
    Integer courtId;

    @PlanningVariable(valueRangeProviderRefs = {"teamRange"}, strengthComparatorClass = TeamStrengthComparator.class)
    public Team getTeam1(){
        return team1;
    }
    public void setTeam1(Team newTeam){
        team1 = newTeam;
    }

    @PlanningVariable(valueRangeProviderRefs = {"teamRange"}, strengthComparatorClass = TeamStrengthComparator.class)
    public Team getTeam2(){
        return team2;
    }
    public void setTeam2(Team newTeam){
        team2 = newTeam;
    }

    public Match getMatch() {
        return match;
    }
    public void setMatch(Match newMatch) {
        match = newMatch;
    }

    public void setMatchTime(MatchTime matchTime) {
        this.matchTime = matchTime;
    }

    public MatchTime getMatchTime() {
        return matchTime;
    }

    public void setMatchDate(MatchDate matchDate) {
        this.matchDate = matchDate;
    }

    public MatchDate getMatchDate() {
        return matchDate;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }
}
