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

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;

public class PeriodChangeMove implements Move {

    private Lecture lecture;
    private Period toPeriod;

    public PeriodChangeMove(Lecture lecture, Period toPeriod) {
        this.lecture = lecture;
        this.toPeriod = toPeriod;
    }

    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        return !ObjectUtils.equals(lecture.getPeriod(), toPeriod);
    }

    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new PeriodChangeMove(lecture, lecture.getPeriod());
    }

    public void doMove(ScoreDirector scoreDirector) {
        CurriculumCourseMoveHelper.movePeriod(scoreDirector, lecture, toPeriod);
    }

    public Collection<? extends Object> getPlanningEntities() {
        return Collections.singletonList(lecture);
    }

    public Collection<? extends Object> getPlanningValues() {
        return Collections.singletonList(toPeriod);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof PeriodChangeMove) {
            PeriodChangeMove other = (PeriodChangeMove) o;
            return new EqualsBuilder()
                    .append(lecture, other.lecture)
                    .append(toPeriod, other.toPeriod)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(lecture)
                .append(toPeriod)
                .toHashCode();
    }

    public String toString() {
        return lecture + " => " + toPeriod;
    }

}
