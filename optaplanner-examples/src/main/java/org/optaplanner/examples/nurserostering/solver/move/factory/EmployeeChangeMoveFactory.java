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

package org.optaplanner.examples.nurserostering.solver.move.factory;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.nurserostering.domain.Employee;
import org.optaplanner.examples.nurserostering.domain.NurseRoster;
import org.optaplanner.examples.nurserostering.domain.ShiftAssignment;
import org.optaplanner.examples.nurserostering.domain.solver.MovableShiftAssignmentSelectionFilter;
import org.optaplanner.examples.nurserostering.solver.move.EmployeeChangeMove;

public class EmployeeChangeMoveFactory implements MoveListFactory {

    private MovableShiftAssignmentSelectionFilter filter = new MovableShiftAssignmentSelectionFilter();

    public List<Move> createMoveList(Solution solution) {
        NurseRoster nurseRoster = (NurseRoster) solution;
        List<Move> moveList = new ArrayList<Move>();
        List<Employee> employeeList = nurseRoster.getEmployeeList();
        for (ShiftAssignment shiftAssignment : nurseRoster.getShiftAssignmentList()) {
            if (filter.accept(nurseRoster, shiftAssignment)) {
                for (Employee employee : employeeList) {
                    moveList.add(new EmployeeChangeMove(shiftAssignment, employee));
                }
            }
        }
        return moveList;
    }

}
