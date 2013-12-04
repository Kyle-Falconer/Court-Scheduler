/*
 * Copyright 2013 Michael Adams, CJ Done, Charles Eswine, Kyle Falconer,
 *  Will Gorman, Stephen Kaysen, Pat McCroskey and Matthew Swinney
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package courtscheduler.solver.move;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.Match;
import courtscheduler.domain.MatchSlot;
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
public class MatchChangeMoveFactory implements MoveListFactory {


    private MovableMatchSelectionFilter filter = new MovableMatchSelectionFilter();

    public List<Move> createMoveList(Solution solution) {
        CourtSchedule courtSchedule = (CourtSchedule) solution;
        List<Move> moveList = new ArrayList<Move>();
        List<Match> matchList = courtSchedule.getMatchList();
        List<MatchSlot> slotList = courtSchedule.getMatchSlots();
        for (Match match : matchList) {
            // if (filter.accept(courtSchedule, match)) {
            for (MatchSlot slot : slotList) {
                moveList.add(new MatchChangeMove(match, slot));
            }
            //}
        }
        return moveList;
    }

}
