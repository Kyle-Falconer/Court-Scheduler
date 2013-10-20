/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package courtscheduler.solver.move;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.MatchAssignment;
import courtscheduler.domain.Team;
import courtscheduler.domain.solver.MovableMatchSelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class TeamChangeMoveFactory implements MoveListFactory {

    private MovableMatchSelectionFilter filter = new MovableMatchSelectionFilter();

    public List<Move> createMoveList(Solution solution) {
        CourtSchedule courtSchedule = (CourtSchedule) solution;
        List<Move> moveList = new ArrayList<Move>();
        List<Team> teamList = courtSchedule.getTeamList();
        for (MatchAssignment matchAssignment : courtSchedule.getMatchAssignments()) {
            if (filter.accept(courtSchedule, matchAssignment)) {
                for (Team team : teamList) {
                    moveList.add(new TeamChangeMove(matchAssignment, team));
                }
            }
        }
        return moveList;
    }

}
