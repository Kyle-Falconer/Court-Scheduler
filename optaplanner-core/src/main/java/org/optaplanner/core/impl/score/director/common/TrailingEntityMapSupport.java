package org.optaplanner.core.impl.score.director.common;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.solution.Solution;

public class TrailingEntityMapSupport {

    protected final SolutionDescriptor solutionDescriptor;

    protected boolean hasChainedVariables;
    // TODO it's unproven that this caching system is actually faster:
    // it happens for every step for every move, but is only needed for every step (with correction for composite moves)
    protected Map<PlanningVariableDescriptor, Map<Object, Set<Object>>> trailingEntitiesMap;

    public TrailingEntityMapSupport(SolutionDescriptor solutionDescriptor) {
        this.solutionDescriptor = solutionDescriptor;
        Collection<PlanningVariableDescriptor> chainedVariableDescriptors = solutionDescriptor
                .getChainedVariableDescriptors();
        hasChainedVariables = !chainedVariableDescriptors.isEmpty();
        trailingEntitiesMap = new LinkedHashMap<PlanningVariableDescriptor, Map<Object, Set<Object>>>(
                chainedVariableDescriptors.size());
        for (PlanningVariableDescriptor chainedVariableDescriptor : chainedVariableDescriptors) {
            trailingEntitiesMap.put(chainedVariableDescriptor, null);
        }
    }

    public void resetTrailingEntityMap(Solution workingSolution) {
        if (hasChainedVariables) {
            List<Object> entityList = solutionDescriptor.getEntityList(workingSolution);
            for (Map.Entry<PlanningVariableDescriptor, Map<Object, Set<Object>>> entry
                    : trailingEntitiesMap.entrySet()) {
                entry.setValue(new IdentityHashMap<Object, Set<Object>>(entityList.size()));
            }
            // TODO Remove when all starting entities call afterEntityAdded too
            for (Object entity : entityList) {
                insertInTrailingEntityMap(solutionDescriptor.getEntityDescriptor(entity.getClass()), entity);
            }
        }
    }

    public void insertInTrailingEntityMap(PlanningEntityDescriptor entityDescriptor, Object entity) {
        if (hasChainedVariables) {
            for (PlanningVariableDescriptor variableDescriptor : entityDescriptor.getVariableDescriptors()) {
                if (variableDescriptor.isChained()) {
                    insertInTrailingEntityMap(variableDescriptor, entity);
                }
            }
        }
    }

    public void insertInTrailingEntityMap(PlanningVariableDescriptor variableDescriptor, Object entity) {
        if (hasChainedVariables && variableDescriptor.isChained()) {
            Map<Object, Set<Object>> valueToTrailingEntityMap = trailingEntitiesMap.get(variableDescriptor);
            if (valueToTrailingEntityMap == null) {
                throw new IllegalStateException("The ScoreDirector (" + getClass() + ") is bugged,"
                        + " because the chained planningVariable (" + variableDescriptor.getVariableName()
                        + ") was not found in the trailingEntitiesMap.");
            }
            Object value = variableDescriptor.getValue(entity);
            Set<Object> trailingEntities = valueToTrailingEntityMap.get(value);
            if (trailingEntities == null) {
                trailingEntities = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
                valueToTrailingEntityMap.put(value, trailingEntities);
            }
            boolean addSucceeded = trailingEntities.add(entity);
            if (!addSucceeded) {
                throw new IllegalStateException("The ScoreDirector (" + getClass() + ") is corrupted,"
                        + " because the entity (" + entity + ") for chained planningVariable ("
                        + variableDescriptor.getVariableName()
                        + ") cannot be inserted: it was already inserted.");
            }
        }
    }

    public void retractFromTrailingEntityMap(PlanningEntityDescriptor entityDescriptor, Object entity) {
        if (hasChainedVariables) {
            for (PlanningVariableDescriptor variableDescriptor : entityDescriptor.getVariableDescriptors()) {
                if (variableDescriptor.isChained()) {
                    retractFromTrailingEntityMap(variableDescriptor, entity);
                }
            }
        }
    }

    public void retractFromTrailingEntityMap(PlanningVariableDescriptor variableDescriptor, Object entity) {
        if (hasChainedVariables && variableDescriptor.isChained()) {
            Map<Object, Set<Object>> valueToTrailingEntityMap = trailingEntitiesMap.get(variableDescriptor);
            if (valueToTrailingEntityMap == null) {
                throw new IllegalStateException("The ScoreDirector (" + getClass() + ") is bugged,"
                        + " because the chained planningVariable (" + variableDescriptor.getVariableName()
                        + ") was not found in the trailingEntitiesMap.");
            }
            Object value = variableDescriptor.getValue(entity);
            Set<Object> trailingEntities = valueToTrailingEntityMap.get(value);
            boolean removeSucceeded = trailingEntities != null && trailingEntities.remove(entity);
            if (!removeSucceeded) {
                throw new IllegalStateException("The ScoreDirector (" + getClass() + ") is corrupted,"
                        + " because the entity (" + entity + ") for chained planningVariable ("
                        + variableDescriptor.getVariableName()
                        + ") cannot be retracted: it was never inserted.");
            }
            if (trailingEntities.isEmpty()) {
                valueToTrailingEntityMap.put(value, null);
            }
        }
    }

    public Object getTrailingEntity(PlanningVariableDescriptor chainedVariableDescriptor, Object planningValue) {
        Set<Object> trailingEntities = trailingEntitiesMap.get(chainedVariableDescriptor)
                .get(planningValue);
        if (trailingEntities == null) {
            return null;
        }
        // trailingEntities can never be an empty list
        if (trailingEntities.size() > 1) {
            throw new IllegalStateException("The planningValue (" + planningValue
                    + ") has multiple trailing entities (" + trailingEntities
                    + ") pointing to it for chained planningVariable ("
                    + chainedVariableDescriptor.getVariableName() + ").");
        }
        return trailingEntities.iterator().next();
    }

}
