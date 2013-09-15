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

package org.optaplanner.examples.pas.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@XStreamAlias("Room")
public class Room extends AbstractPersistable {

    private String name;

    private Department department;
    private int capacity;
    private GenderLimitation genderLimitation;

    private List<RoomSpecialism> roomSpecialismList;
    private List<RoomEquipment> roomEquipmentList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public GenderLimitation getGenderLimitation() {
        return genderLimitation;
    }

    public void setGenderLimitation(GenderLimitation genderLimitation) {
        this.genderLimitation = genderLimitation;
    }

    public List<RoomSpecialism> getRoomSpecialismList() {
        return roomSpecialismList;
    }

    public void setRoomSpecialismList(List<RoomSpecialism> roomSpecialismList) {
        this.roomSpecialismList = roomSpecialismList;
    }

    public List<RoomEquipment> getRoomEquipmentList() {
        return roomEquipmentList;
    }

    public void setRoomEquipmentList(List<RoomEquipment> roomEquipmentList) {
        this.roomEquipmentList = roomEquipmentList;
    }

    @Override
    public String toString() {
        return department + "_" + name;
    }

    public int countDisallowedAdmissionPart(AdmissionPart admissionPart) {
        return department.countDisallowedAdmissionPart(admissionPart)
                + countDisallowedPatientGender(admissionPart.getPatient())
                + countMissingRequiredRoomProperties(admissionPart.getPatient())
                + countMissingPreferredRoomProperties(admissionPart.getPatient());
        // TODO preferredMaximumRoomCapacity and specialism
    }

    public int countDisallowedPatientGender(Patient patient) {
        switch (genderLimitation) {
            case ANY_GENDER:
                return 0;
            case MALE_ONLY:
                return patient.getGender() == Gender.MALE ? 0 : 50;
            case FEMALE_ONLY:
                return patient.getGender() == Gender.FEMALE ? 0 : 50;
            case SAME_GENDER:
                // scoreRules check this
                return 25;
            default:
                throw new IllegalStateException("The genderLimitation (" + genderLimitation + ") is not implemented.");
        }
    }

    public int countMissingRequiredRoomProperties(Patient patient) {
        int count = 0;
        for (RequiredPatientEquipment requiredPatientEquipment : patient.getRequiredPatientEquipmentList()) {
            Equipment requiredEquipment = requiredPatientEquipment.getEquipment();
            boolean hasRequiredEquipment = false;
            for (RoomEquipment roomEquipment : roomEquipmentList) {
                if (roomEquipment.getEquipment().equals(requiredEquipment)) {
                    hasRequiredEquipment = true;
                }
            }
            if (!hasRequiredEquipment) {
                count += 50;
            }
        }
        return count;
    }

    public int countMissingPreferredRoomProperties(Patient patient) {
        int count = 0;
        for (PreferredPatientEquipment preferredPatientEquipment : patient.getPreferredPatientEquipmentList()) {
            Equipment preferredEquipment = preferredPatientEquipment.getEquipment();
            boolean hasPreferredEquipment = false;
            for (RoomEquipment roomEquipment : roomEquipmentList) {
                if (roomEquipment.getEquipment().equals(preferredEquipment)) {
                    hasPreferredEquipment = true;
                }
            }
            if (!hasPreferredEquipment) {
                count += 20;
            }
        }
        return count;
    }

}
