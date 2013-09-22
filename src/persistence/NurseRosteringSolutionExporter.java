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

package org.optaplanner.examples.nurserostering.persistence;

import java.io.BufferedWriter;
import java.io.IOException;

import org.jdom.Element;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractXmlSolutionExporter;
import org.optaplanner.examples.nurserostering.domain.NurseRoster;
import org.optaplanner.examples.nurserostering.domain.Shift;
import org.optaplanner.examples.nurserostering.domain.ShiftAssignment;

public class NurseRosteringSolutionExporter extends AbstractXmlSolutionExporter {

    public static void main(String[] args) {
        new NurseRosteringSolutionExporter().convertAll();
    }

    public NurseRosteringSolutionExporter() {
        super(new NurseRosteringDao());
    }

    public XmlOutputBuilder createXmlOutputBuilder() {
        return new NurseRosteringOutputBuilder();
    }

    public class NurseRosteringOutputBuilder extends XmlOutputBuilder {

        private NurseRoster nurseRoster;

        public void setSolution(Solution solution) {
            nurseRoster = (NurseRoster) solution;
        }

        public void writeSolution() throws IOException {


            BufferedWriter writer =  new java.io.BufferedWriter(new java.io.FileWriter("test.csv", true));
            writer.newLine();

            writer.write("date, employee, shift type\n");

            for (ShiftAssignment shiftAssignment : nurseRoster.getShiftAssignmentList()) {
                Shift shift = shiftAssignment.getShift();
                if (shift != null) {
                    writer.write(shift.getShiftDate().getDateString() +","+shiftAssignment.getEmployee().getCode()+","+shift.getShiftType().getCode()+"\n");

                }
            }

            writer.close();
        }
    }

}
