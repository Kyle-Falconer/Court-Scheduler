package courtscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningEntity
public class MatchAssignment extends AbstractPersistable {
    private Team team1;
    private Team team2;
    private Match match;

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

    public List<Team> getTeamList() {
        Team[] teams = {team1, team2};
        return Arrays.asList(teams);
    }
}
