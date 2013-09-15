package org.optaplanner.core.impl.heuristic.selector.entity.decorator;

import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Behaves as if it was a UninitializedVariableEntityFilter, except when the variable is
 * {@link PlanningVariable#nullable()}.
 */
public class NullValueReinitializeVariableEntityFilter implements SelectionFilter<Object> {

    private final PlanningVariableDescriptor variableDescriptor;

    public NullValueReinitializeVariableEntityFilter(PlanningVariableDescriptor variableDescriptor) {
        this.variableDescriptor = variableDescriptor;
    }

    public boolean accept(ScoreDirector scoreDirector, Object selection) {
        // This does not use variableDescriptor.isInitialized() because if nullable it must also return false
        Object value = variableDescriptor.getValue(selection);
        return value == null;
    }

}
