package org.optaplanner.examples.projectjobscheduling.solver.score;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.impl.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.optaplanner.examples.projectjobscheduling.domain.Allocation;
import org.optaplanner.examples.projectjobscheduling.domain.ExecutionMode;
import org.optaplanner.examples.projectjobscheduling.domain.JobType;
import org.optaplanner.examples.projectjobscheduling.domain.Project;
import org.optaplanner.examples.projectjobscheduling.domain.ResourceRequirement;
import org.optaplanner.examples.projectjobscheduling.domain.Schedule;
import org.optaplanner.examples.projectjobscheduling.domain.resource.Resource;
import org.optaplanner.examples.projectjobscheduling.solver.score.capacity.NonrenewableResourceCapacityTracker;
import org.optaplanner.examples.projectjobscheduling.solver.score.capacity.RenewableResourceCapacityTracker;
import org.optaplanner.examples.projectjobscheduling.solver.score.capacity.ResourceCapacityTracker;

public class ProjectJobSchedulingIncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<Schedule> {

    private Map<Resource, ResourceCapacityTracker> resourceCapacityTrackerMap;
    private Map<Project, Integer> projectDelayMap;
    private Map<Project, Set<Allocation>> allocationsPerProjectMap;

    private int hardScore;
    private int mediumScore;
    private int softScore;
    
    private int minimalReleaseDate;
    private int maximalEndDate;

    public void resetWorkingSolution(Schedule schedule) {
        List<Resource> resourceList = schedule.getResourceList();
        resourceCapacityTrackerMap = new HashMap<Resource, ResourceCapacityTracker>(resourceList.size());
        for (Resource resource : resourceList) {
            resourceCapacityTrackerMap.put(resource, resource.isRenewable()
                    ? new RenewableResourceCapacityTracker(resource)
                    : new NonrenewableResourceCapacityTracker(resource));
        }
        hardScore = 0;
        mediumScore = 0;
        softScore = 0;
        totalProjectDelay = 0;
        minimalReleaseDate = Integer.MAX_VALUE;
        maximalEndDate = 0;
        for (Project p: schedule.getProjectList()) {
            minimalReleaseDate = Math.min(p.getReleaseDate(), minimalReleaseDate); 
        }
        projectDelayMap = new HashMap<Project, Integer>();
        allocationsPerProjectMap = new HashMap<Project, Set<Allocation>>();
        for (Allocation allocation : schedule.getAllocationList()) {
            insert(allocation);
        }
    }

    public void beforeEntityAdded(Object entity) {
        // Do nothing
    }

    public void afterEntityAdded(Object entity) {
        insert((Allocation) entity);
    }

    public void beforeVariableChanged(Object entity, String variableName) {
        retract((Allocation) entity);
    }

    public void afterVariableChanged(Object entity, String variableName) {
        insert((Allocation) entity);
    }

    public void beforeEntityRemoved(Object entity) {
        retract((Allocation) entity);
    }

    public void afterEntityRemoved(Object entity) {
        // Do nothing
    }
    
    private int totalProjectDelay;
    
    private int getTotalProjectDelay(Allocation allocation) {
        Project p = allocation.getJob().getProject(); 
        // first remove previous delay
        int previousProjectDelay = projectDelayMap.containsKey(p) ? projectDelayMap.get(p) : 0;
        totalProjectDelay -= previousProjectDelay;
        Allocation sink = allocation.getSinkAllocation();
        Integer endDate = sink.getEndDate();
        // now calculate and add new delay
        if (endDate == null) {
            projectDelayMap.put(p, 0);
        } else {
            int delay = endDate - p.getReleaseDate() - p.getCriticalPathDuration();
            totalProjectDelay += delay;
            projectDelayMap.put(p, delay);
        }
        return totalProjectDelay;
    }

    private void insert(Allocation allocation) {
        trackAllocation(allocation);
        // identify the maximal end date to calculate total makespan
        Integer endDate = allocation.getEndDate();
        if (endDate != null && endDate > maximalEndDate) {
            maximalEndDate = endDate;
            softScore = -(maximalEndDate - minimalReleaseDate);
        }
        // calculate total project delay
        mediumScore = -getTotalProjectDelay(allocation);
        // track capacity
        ExecutionMode executionMode = allocation.getExecutionMode();
        if (executionMode != null && allocation.getJob().getJobType() == JobType.STANDARD) {
            for (ResourceRequirement resourceRequirement : executionMode.getResourceRequirementList()) {
                ResourceCapacityTracker tracker = resourceCapacityTrackerMap.get(
                        resourceRequirement.getResource());
                hardScore -= tracker.getHardScore();
                tracker.insert(resourceRequirement, allocation);
                hardScore += tracker.getHardScore();
            }
        }
    }
    
    private void trackAllocation(Allocation a) {
        Project p = a.getJob().getProject();
        if (!allocationsPerProjectMap.containsKey(p)) {
            allocationsPerProjectMap.put(p, new HashSet<Allocation>());
        }
        allocationsPerProjectMap.get(p).add(a);
    }
    
    private int getMaximalEndDate() {
        int max = 0;
        for (Set<Allocation> allocations: allocationsPerProjectMap.values()) {
            for (Allocation allocation: allocations) {
                Integer otherEndDate = allocation.getEndDate();
                if (otherEndDate != null) {
                    max = Math.max(otherEndDate, max);
                }
            }
        }
        return max;
    }

    private void retract(Allocation allocation) {
        allocationsPerProjectMap.get(allocation.getJob().getProject()).remove(allocation);
        // identify the maximal end date to calculate total makespan
        Integer endDate = allocation.getEndDate();
        if (endDate != null && endDate >= maximalEndDate) {
            maximalEndDate = getMaximalEndDate();
            softScore = -(maximalEndDate - minimalReleaseDate);
        }
        // calculate total project delay
        mediumScore = -getTotalProjectDelay(allocation);
        // track capacity
        ExecutionMode executionMode = allocation.getExecutionMode();
        if (executionMode != null && allocation.getJob().getJobType() == JobType.STANDARD) {
            for (ResourceRequirement resourceRequirement : executionMode.getResourceRequirementList()) {
                ResourceCapacityTracker tracker = resourceCapacityTrackerMap.get(
                        resourceRequirement.getResource());
                hardScore -= tracker.getHardScore();
                tracker.retract(resourceRequirement, allocation);
                hardScore += tracker.getHardScore();
            }
        }
    }

    public Score calculateScore() {
        return BendableScore.valueOf(new int[] {hardScore}, new int[] {mediumScore, softScore});
    }

}
