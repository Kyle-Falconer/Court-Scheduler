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
