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

package org.optaplanner.examples.nurserostering.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@XStreamAlias("NurseRosterInfo")
public class NurseRosterInfo extends AbstractPersistable {

    private ShiftDate firstShiftDate;
    private ShiftDate lastShiftDate;

    private ShiftDate planningWindowStart;

    public ShiftDate getFirstShiftDate() {
        return firstShiftDate;
    }

    public void setFirstShiftDate(ShiftDate firstShiftDate) {
        this.firstShiftDate = firstShiftDate;
    }

    public ShiftDate getLastShiftDate() {
        return lastShiftDate;
    }

    public void setLastShiftDate(ShiftDate lastShiftDate) {
        this.lastShiftDate = lastShiftDate;
    }

    public int getFirstShiftDateDayIndex() {
        return firstShiftDate.getDayIndex();
    }

    public int getLastShiftDateDayIndex() {
        return lastShiftDate.getDayIndex();
    }

    public ShiftDate getPlanningWindowStart() {
        return planningWindowStart;
    }

    public void setPlanningWindowStart(ShiftDate planningWindowStart) {
        this.planningWindowStart = planningWindowStart;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public boolean isInPlanningWindow(ShiftDate shiftDate) {
        return planningWindowStart.getDayIndex() <= shiftDate.getDayIndex();
    }

    @Override
    public String toString() {
        return firstShiftDate + " - " + lastShiftDate;
    }

}
