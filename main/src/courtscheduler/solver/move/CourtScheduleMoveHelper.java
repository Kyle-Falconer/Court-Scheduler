package courtscheduler.solver.move;

import courtscheduler.domain.Match;
import courtscheduler.domain.MatchSlot;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:30 PM
 */
public class CourtScheduleMoveHelper {
    public static void moveMatchSlot(ScoreDirector scoreDirector, Match match, MatchSlot matchSlot) {
        scoreDirector.beforeVariableChanged(match, "matchSlot");
        match.setMatchSlot(matchSlot);
        scoreDirector.afterVariableChanged(match, "matchSlot");
    }

    private CourtScheduleMoveHelper() {
    }
}
