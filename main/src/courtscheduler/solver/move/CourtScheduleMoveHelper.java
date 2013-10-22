package courtscheduler.solver.move;

import courtscheduler.domain.Match;
import courtscheduler.domain.MatchAssignment;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:30 PM
 */
public class CourtScheduleMoveHelper {
    public static void moveMatch(ScoreDirector scoreDirector, MatchAssignment matchAssignment, Match toMatch) {
        scoreDirector.beforeVariableChanged(matchAssignment, "match");
        matchAssignment.setMatch(toMatch);
        scoreDirector.afterVariableChanged(matchAssignment, "match");
    }

    private CourtScheduleMoveHelper() {
    }
}
