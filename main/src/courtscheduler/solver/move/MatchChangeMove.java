package courtscheduler.solver.move;

import courtscheduler.domain.Match;
import courtscheduler.domain.MatchAssignment;
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
    private Integer day;
    private Integer time;
    private Integer court;
    public MatchChangeMove(Match toMatch, Integer day, Integer time, Integer court){
        this.toMatch = toMatch;
        this.day=day;
        this.time=time;
        this.court=court;
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        // cannot move to same position
        // see http://docs.jboss.org/drools/release/6.0.0.CR5/optaplanner-docs/html/moveAndNeighborhoodSelection.html#d0e5896
        return !ObjectUtils.equals(toMatch.getDay(), day) ||
                !ObjectUtils.equals(toMatch.getTime(),time )||
                !ObjectUtils.equals(toMatch.getCourt(),court);
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new MatchChangeMove(toMatch,day,time,court);
    }

    @Override
    public void doMove(ScoreDirector scoreDirector) {
        //TODO: implement move rotation (currently just cycles every thing at once rather than every possibility
        CourtScheduleMoveHelper.moveDay(scoreDirector, toMatch, day);
        CourtScheduleMoveHelper.moveTime(scoreDirector, toMatch, time);
        CourtScheduleMoveHelper.moveCourt(scoreDirector, toMatch, court);
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.singletonList(toMatch);
    }

    @Override
    public List<Integer> getPlanningValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(day);
        values.add(time);
        values.add(court);
        return values;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof MatchChangeMove) {
            MatchChangeMove other = (MatchChangeMove) o;
            return new EqualsBuilder()
                    .append(toMatch, other.toMatch)
                    .append(day, other.day)
                    .append(time, other.time)
                    .append(court, other.court)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(toMatch)
                .append(day)
                .append(time)
                .append(court)
                .toHashCode();
    }

    public String toString() {
        return toMatch+"=>"+day+":"+time+":"+court;
    }
}
