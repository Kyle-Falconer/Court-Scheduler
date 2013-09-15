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

package org.optaplanner.examples.curriculumcourse.solver.move;

import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;

public class CurriculumCourseMoveHelper {

    public static void movePeriod(ScoreDirector scoreDirector, Lecture lecture, Period period) {
        scoreDirector.beforeVariableChanged(lecture, "period");
        lecture.setPeriod(period);
        scoreDirector.afterVariableChanged(lecture, "period");
    }

    public static void moveRoom(ScoreDirector scoreDirector, Lecture lecture, Room room) {
        scoreDirector.beforeVariableChanged(lecture, "room");
        lecture.setRoom(room);
        scoreDirector.afterVariableChanged(lecture, "room");
    }

    public static void moveLecture(ScoreDirector scoreDirector, Lecture lecture, Period period, Room room) {
        scoreDirector.beforeVariableChanged(lecture, "period");
        scoreDirector.beforeVariableChanged(lecture, "room");
        lecture.setPeriod(period);
        lecture.setRoom(room);
        scoreDirector.afterVariableChanged(lecture, "period");
        scoreDirector.afterVariableChanged(lecture, "room");
    }

    private CurriculumCourseMoveHelper() {
    }

}
