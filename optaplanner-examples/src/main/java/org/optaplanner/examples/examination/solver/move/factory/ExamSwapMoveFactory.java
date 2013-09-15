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

package org.optaplanner.examples.examination.solver.move.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.examination.domain.Exam;
import org.optaplanner.examples.examination.domain.Examination;
import org.optaplanner.examples.examination.solver.move.ExamSwapMove;

public class ExamSwapMoveFactory implements MoveListFactory {

    public List<Move> createMoveList(Solution solution) {
        Examination examination = (Examination) solution;
        List<Exam> examList = examination.getExamList();
        List<Move> moveList = new ArrayList<Move>();
        for (ListIterator<Exam> leftIt = examList.listIterator(); leftIt.hasNext();) {
            Exam leftExam = leftIt.next();
            for (ListIterator<Exam> rightIt = examList.listIterator(leftIt.nextIndex()); rightIt.hasNext();) {
                Exam rightExam = rightIt.next();
                moveList.add(new ExamSwapMove(leftExam, rightExam));
            }
        }
        return moveList;
    }

}
