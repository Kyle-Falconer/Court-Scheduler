package courtscheduler.solver.move;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.Match;
import courtscheduler.domain.MatchAssignment;
import courtscheduler.domain.solver.MovableMatchSelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchChangeMoveFactory  implements MoveListFactory {


    private MovableMatchSelectionFilter filter = new MovableMatchSelectionFilter();

    public List<Move> createMoveList(Solution solution) {
        CourtSchedule courtSchedule = (CourtSchedule) solution;
        List<Move> moveList = new ArrayList<Move>();
        List<Match> matchList = courtSchedule.getMatchList();
        List<Integer> dayList = courtSchedule.getDayList();
        List<Integer> timeList = courtSchedule.getTimeList();
        List<Integer> courtList= courtSchedule.getCourtList();
        for (Match match:matchList) {
            //if (filter.accept(courtSchedule, matchAssignment)) {
            for(Integer day: dayList){
                for(Integer time: timeList){
                    for(Integer court: courtList){
                        moveList.add(new MatchChangeMove(match,day,time,court));
                    }
                }
            }
        }
        return moveList;
    }

}
