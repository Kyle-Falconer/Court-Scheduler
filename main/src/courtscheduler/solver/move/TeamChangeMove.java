package courtscheduler.solver.move;

import courtscheduler.domain.MatchAssignment;
import courtscheduler.domain.Team;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:20 PM
 */
public class TeamChangeMove implements Move {

    private MatchAssignment matchAssignment;
    private Team toTeam;

    public TeamChangeMove(MatchAssignment matchAssignment, Team toTeam){
        this.matchAssignment = matchAssignment;
        this.toTeam = toTeam;
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        // cannot move to same position
        // see http://docs.jboss.org/drools/release/6.0.0.CR5/optaplanner-docs/html/moveAndNeighborhoodSelection.html#d0e5896
        return !ObjectUtils.equals(matchAssignment.getTeam1(), toTeam) ||
                !ObjectUtils.equals(matchAssignment.getTeam2(), toTeam);
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new TeamChangeMove(matchAssignment, toTeam);
    }

    @Override
    public void doMove(ScoreDirector scoreDirector) {
        CourtScheduleMoveHelper.moveTeam(scoreDirector, matchAssignment, toTeam);
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.singletonList(matchAssignment);
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.singletonList(toTeam);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof TeamChangeMove) {
            TeamChangeMove other = (TeamChangeMove) o;
            return new EqualsBuilder()
                    .append(matchAssignment, other.matchAssignment)
                    .append(toTeam, other.toTeam)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(matchAssignment)
                .append(toTeam)
                .toHashCode();
    }

    public String toString() {
        return matchAssignment + " => " + toTeam;
    }
}
