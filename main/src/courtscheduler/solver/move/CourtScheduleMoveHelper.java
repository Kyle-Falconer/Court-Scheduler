package courtscheduler.solver.move;

import courtscheduler.domain.MatchAssignment;
import courtscheduler.domain.Team;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:30 PM
 */
public class CourtScheduleMoveHelper {
    public static void moveTeam(ScoreDirector scoreDirector, MatchAssignment matchAssignment, Team toTeam) {
        scoreDirector.beforeVariableChanged(matchAssignment, "team");
        matchAssignment.setTeam1(toTeam); // FIXME: handle second team?!
        scoreDirector.afterVariableChanged(matchAssignment, "team");
    }

    private CourtScheduleMoveHelper() {
    }
}
