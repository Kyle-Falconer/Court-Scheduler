package courtscheduler.domain.solver;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.MatchAssignment;
import courtscheduler.domain.MatchDate;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 10/20/13
 * Time: 5:01 PM
 */
public class MovableMatchSelectionFilter implements SelectionFilter<MatchAssignment> {

    public boolean accept(ScoreDirector scoreDirector, MatchAssignment matchAssignment) {
        CourtSchedule courtSchedule = (CourtSchedule) scoreDirector.getWorkingSolution();
        return accept(courtSchedule, matchAssignment);
    }

    public boolean accept(CourtSchedule courtSchedule, MatchAssignment matchAssignment) {
        MatchDate matchDate = matchAssignment.getMatch().getMatchDate();
        return courtSchedule.getCourtScheduleInfo().isInPlanningWindow(matchDate);
    }


}
