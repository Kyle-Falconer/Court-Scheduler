package courtscheduler.domain.solver;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.Match;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:01 PM
 */
public class MovableMatchSelectionFilter implements SelectionFilter<Match> {

    public boolean accept(ScoreDirector scoreDirector, Match match) {
        CourtSchedule courtSchedule = (CourtSchedule) scoreDirector.getWorkingSolution();
        return accept(courtSchedule, match);
    }

    public boolean accept(CourtSchedule courtSchedule, Match match) {
		return true; // FIXME do we even still need this?
    }


}
