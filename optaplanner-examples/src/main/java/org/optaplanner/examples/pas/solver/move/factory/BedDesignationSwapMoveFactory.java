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

package org.optaplanner.examples.pas.solver.move.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.pas.domain.BedDesignation;
import org.optaplanner.examples.pas.domain.PatientAdmissionSchedule;
import org.optaplanner.examples.pas.solver.move.BedDesignationSwapMove;

public class BedDesignationSwapMoveFactory implements MoveListFactory {

    public List<Move> createMoveList(Solution solution) {
        PatientAdmissionSchedule patientAdmissionSchedule = (PatientAdmissionSchedule) solution;
        List<BedDesignation> bedDesignationList = patientAdmissionSchedule.getBedDesignationList();
        List<Move> moveList = new ArrayList<Move>();
        for (ListIterator<BedDesignation> leftIt = bedDesignationList.listIterator(); leftIt.hasNext();) {
            BedDesignation leftBedDesignation = leftIt.next();
            for (ListIterator<BedDesignation> rightIt = bedDesignationList.listIterator(leftIt.nextIndex());
                    rightIt.hasNext();) {
                BedDesignation rightBedDesignation = rightIt.next();
                if (leftBedDesignation.getAdmissionPart().calculateSameNightCount(rightBedDesignation.getAdmissionPart()) > 0) {
                    moveList.add(new BedDesignationSwapMove(leftBedDesignation, rightBedDesignation));
                }
            }
        }
        return moveList;
    }

}
