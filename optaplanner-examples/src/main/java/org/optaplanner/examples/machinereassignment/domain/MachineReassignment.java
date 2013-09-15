/*
 * Copyright 2011 JBoss Inc
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

package org.optaplanner.examples.machinereassignment.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.buildin.hardsoftlong.HardSoftLongScoreDefinition;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.machinereassignment.domain.solver.MrServiceDependency;
import org.optaplanner.persistence.xstream.XStreamScoreConverter;

@PlanningSolution
@XStreamAlias("MachineReassignment")
public class MachineReassignment extends AbstractPersistable implements Solution<HardSoftLongScore> {

    private MrGlobalPenaltyInfo globalPenaltyInfo;
    private List<MrResource> resourceList;
    private List<MrNeighborhood> neighborhoodList;
    private List<MrLocation> locationList;
    private List<MrMachine> machineList;
    private List<MrMachineCapacity> machineCapacityList;
    private List<MrService> serviceList;
    private List<MrProcess> processList;
    private List<MrBalancePenalty> balancePenaltyList;

    private List<MrProcessAssignment> processAssignmentList;

    @XStreamConverter(value = XStreamScoreConverter.class, types = {HardSoftLongScoreDefinition.class})
    private HardSoftLongScore score;

    public MrGlobalPenaltyInfo getGlobalPenaltyInfo() {
        return globalPenaltyInfo;
    }

    public void setGlobalPenaltyInfo(MrGlobalPenaltyInfo globalPenaltyInfo) {
        this.globalPenaltyInfo = globalPenaltyInfo;
    }

    public List<MrResource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<MrResource> resourceList) {
        this.resourceList = resourceList;
    }

    public List<MrNeighborhood> getNeighborhoodList() {
        return neighborhoodList;
    }

    public void setNeighborhoodList(List<MrNeighborhood> neighborhoodList) {
        this.neighborhoodList = neighborhoodList;
    }

    public List<MrLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<MrLocation> locationList) {
        this.locationList = locationList;
    }

    @ValueRangeProvider(id = "machineRange")
    public List<MrMachine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<MrMachine> machineList) {
        this.machineList = machineList;
    }

    public List<MrMachineCapacity> getMachineCapacityList() {
        return machineCapacityList;
    }

    public void setMachineCapacityList(List<MrMachineCapacity> machineCapacityList) {
        this.machineCapacityList = machineCapacityList;
    }

    public List<MrService> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<MrService> serviceList) {
        this.serviceList = serviceList;
    }

    public List<MrProcess> getProcessList() {
        return processList;
    }

    public void setProcessList(List<MrProcess> processList) {
        this.processList = processList;
    }

    public List<MrBalancePenalty> getBalancePenaltyList() {
        return balancePenaltyList;
    }

    public void setBalancePenaltyList(List<MrBalancePenalty> balancePenaltyList) {
        this.balancePenaltyList = balancePenaltyList;
    }

    @PlanningEntityCollectionProperty
    public List<MrProcessAssignment> getProcessAssignmentList() {
        return processAssignmentList;
    }

    public void setProcessAssignmentList(List<MrProcessAssignment> processAssignmentList) {
        this.processAssignmentList = processAssignmentList;
    }

    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.add(globalPenaltyInfo);
        facts.addAll(resourceList);
        facts.addAll(neighborhoodList);
        facts.addAll(locationList);
        facts.addAll(machineList);
        facts.addAll(machineCapacityList);
        facts.addAll(serviceList);
        facts.addAll(createServiceDependencyList());
        facts.addAll(processList);
        facts.addAll(balancePenaltyList);
        // Do not add the planning entity's (bedDesignationList) because that will be done automatically
        return facts;
    }

    private List<MrServiceDependency> createServiceDependencyList() {
        List<MrServiceDependency> serviceDependencyList = new ArrayList<MrServiceDependency>(serviceList.size() * 5);
        for (MrService service : serviceList) {
            for (MrService toService : service.getToDependencyServiceList()) {
                MrServiceDependency serviceDependency = new MrServiceDependency();
                serviceDependency.setFromService(service);
                serviceDependency.setToService(toService);
                serviceDependencyList.add(serviceDependency);
            }
        }
        return serviceDependencyList;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (id == null || !(o instanceof MachineReassignment)) {
            return false;
        } else {
            MachineReassignment other = (MachineReassignment) o;
            if (processAssignmentList.size() != other.processAssignmentList.size()) {
                return false;
            }
            for (Iterator<MrProcessAssignment> it = processAssignmentList.iterator(),
                    otherIt = other.processAssignmentList.iterator(); it.hasNext();) {
                MrProcessAssignment processAssignment = it.next();
                MrProcessAssignment otherProcessAssignment = otherIt.next();
                // Notice: we don't use equals()
                if (!processAssignment.solutionEquals(otherProcessAssignment)) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        for (MrProcessAssignment processAssignment : processAssignmentList) {
            // Notice: we don't use hashCode()
            hashCodeBuilder.append(processAssignment.solutionHashCode());
        }
        return hashCodeBuilder.toHashCode();
    }

}
