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
    public static void moveDay(ScoreDirector scoreDirector, Match match, Integer day) {
        scoreDirector.beforeVariableChanged(match, "day");
        match.setDay(day);
        scoreDirector.afterVariableChanged(match, "day");
    }
    public static void moveTime(ScoreDirector scoreDirector,Match match, Integer time) {
        scoreDirector.beforeVariableChanged(match, "time");
        match.setTime(time);
        scoreDirector.afterVariableChanged(match, "time");
    }
    public static void moveCourt(ScoreDirector scoreDirector, Match match, Integer court) {
        scoreDirector.beforeVariableChanged(match, "court");
        match.setCourt(court);
        scoreDirector.afterVariableChanged(match, "court");
    }

    private CourtScheduleMoveHelper() {
    }
}
