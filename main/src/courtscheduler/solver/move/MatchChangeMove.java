package courtscheduler.solver.move;

import courtscheduler.domain.Match;
import courtscheduler.domain.MatchSlot;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:20 PM
 */
public class MatchChangeMove implements Move {

    private Match toMatch;
    private MatchSlot matchSlot;
    public MatchChangeMove(Match toMatch, MatchSlot matchSlot){
        this.toMatch = toMatch;
        this.matchSlot = matchSlot;
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        // cannot move to same position
        // see http://docs.jboss.org/drools/release/6.0.0.CR5/optaplanner-docs/html/moveAndNeighborhoodSelection.html#d0e5896
        return !ObjectUtils.equals(toMatch.getMatchSlot().getDay(), matchSlot.getDay()) ||
                !ObjectUtils.equals(toMatch.getMatchSlot().getTime(), matchSlot.getTime())||
                !ObjectUtils.equals(toMatch.getMatchSlot().getCourt(), matchSlot.getCourt());
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new MatchChangeMove(toMatch, toMatch.getMatchSlot());
    }

    @Override
    public void doMove(ScoreDirector scoreDirector) {
        //TODO: implement move rotation (currently just cycles every thing at once rather than every possibility
        CourtScheduleMoveHelper.moveMatchSlot(scoreDirector, toMatch, matchSlot);
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.singletonList(toMatch);
    }

    @Override
    public List<Integer> getPlanningValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(matchSlot.getDay());
        values.add(matchSlot.getTime());
        values.add(matchSlot.getCourt());
        return values;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof MatchChangeMove) {
            MatchChangeMove other = (MatchChangeMove) o;
            return new EqualsBuilder()
                    .append(toMatch, other.toMatch)
                    .append(matchSlot, other.matchSlot)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(toMatch)
                .append(matchSlot)
                .toHashCode();
    }

    public String toString() {
        return toMatch+"=>"+matchSlot;
    }
}
