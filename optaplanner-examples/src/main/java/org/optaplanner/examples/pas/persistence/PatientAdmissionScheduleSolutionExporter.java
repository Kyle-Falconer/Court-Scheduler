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

package org.optaplanner.examples.pas.persistence;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionExporter;
import org.optaplanner.examples.pas.domain.BedDesignation;
import org.optaplanner.examples.pas.domain.Patient;
import org.optaplanner.examples.pas.domain.PatientAdmissionSchedule;

public class PatientAdmissionScheduleSolutionExporter extends AbstractTxtSolutionExporter {

    public static void main(String[] args) {
        new PatientAdmissionScheduleSolutionExporter().convertAll();
    }

    public PatientAdmissionScheduleSolutionExporter() {
        super(new PatientAdmissionScheduleDao());
    }

    public TxtOutputBuilder createTxtOutputBuilder() {
        return new PatientAdmissionScheduleOutputBuilder();
    }

    public class PatientAdmissionScheduleOutputBuilder extends TxtOutputBuilder {

        private PatientAdmissionSchedule patientAdmissionSchedule;

        public void setSolution(Solution solution) {
            patientAdmissionSchedule = (PatientAdmissionSchedule) solution;
        }

        public void writeSolution() throws IOException {
            Collections.sort(patientAdmissionSchedule.getBedDesignationList(), new Comparator<BedDesignation>() {
                public int compare(BedDesignation a, BedDesignation b) {
                    return new CompareToBuilder()
                            .append(a.getAdmissionPart(), b.getAdmissionPart())
                            .append(a.getBed(), b.getBed())
                            .append(a.getId(), b.getId())
                            .toComparison();
                }
            });
            for (Patient patient : patientAdmissionSchedule.getPatientList()) {
                bufferedWriter.write(Long.toString(patient.getId()));
                for (BedDesignation bedDesignation : patientAdmissionSchedule.getBedDesignationList()) {
                    if (bedDesignation.getPatient().equals(patient)) {
                        for (int i = 0; i < bedDesignation.getAdmissionPart().getNightCount(); i++) {
                            bufferedWriter.write(" " + Long.toString(bedDesignation.getBed().getId()));
                        }
                    }
                }
                bufferedWriter.write("\n");
            }
        }
    }

}
