package courtscheduler.solver.move;

import courtscheduler.domain.Match;
import courtscheduler.domain.MatchAssignment;
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
public class MatchChangeMove implements Move {

    private MatchAssignment matchAssignment;
    private Match toMatch;

    public MatchChangeMove(MatchAssignment matchAssignment, Match toMatch){
        this.matchAssignment = matchAssignment;
        this.toMatch = toMatch;
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        // cannot move to same position
        // see http://docs.jboss.org/drools/release/6.0.0.CR5/optaplanner-docs/html/moveAndNeighborhoodSelection.html#d0e5896
        return !ObjectUtils.equals(matchAssignment.getTeam1(), toMatch) ||
                !ObjectUtils.equals(matchAssignment.getTeam2(), toMatch);
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new MatchChangeMove(matchAssignment, toMatch);
    }

    @Override
    public void doMove(ScoreDirector scoreDirector) {
        CourtScheduleMoveHelper.moveMatch(scoreDirector, matchAssignment, toMatch);
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.singletonList(matchAssignment);
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.singletonList(toMatch);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof MatchChangeMove) {
            MatchChangeMove other = (MatchChangeMove) o;
            return new EqualsBuilder()
                    .append(matchAssignment, other.matchAssignment)
                    .append(toMatch, other.toMatch)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(matchAssignment)
                .append(toMatch)
                .toHashCode();
    }

    public String toString() {
        return matchAssignment + " => " + toMatch;
    }
}
