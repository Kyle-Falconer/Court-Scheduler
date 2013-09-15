/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.domain.value;

import java.lang.reflect.Method;
import java.util.Collection;

import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.impl.domain.common.DefaultReadMethodAccessor;
import org.optaplanner.core.impl.domain.common.ReadMethodAccessor;
import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.solution.Solution;

public class FromSolutionPropertyPlanningValueRangeDescriptor extends AbstractPlanningValueRangeDescriptor {

    private ReadMethodAccessor rangeReadMethodAccessor;

    public FromSolutionPropertyPlanningValueRangeDescriptor(PlanningVariableDescriptor variableDescriptor,
            Method readMethod) {
        super(variableDescriptor);
        rangeReadMethodAccessor = new DefaultReadMethodAccessor(readMethod);
        ValueRangeProvider valueRangeProviderAnnotation = readMethod.getAnnotation(ValueRangeProvider.class);
        if (valueRangeProviderAnnotation == null) {
            throw new IllegalStateException("The readMethod (" + readMethod
                    + ") must have a valueRangeProviderAnnotation (" + valueRangeProviderAnnotation + ").");
        }
        processValueRangeProviderAnnotation(valueRangeProviderAnnotation);
    }

    private void processValueRangeProviderAnnotation(ValueRangeProvider valueRangeProviderAnnotation) {
        PlanningEntityDescriptor entityDescriptor = variableDescriptor.getEntityDescriptor();
        if (!Collection.class.isAssignableFrom(rangeReadMethodAccessor.getReturnType())) {
            throw new IllegalArgumentException("The planningEntityClass ("
                    + entityDescriptor.getPlanningEntityClass()
                    + ") has a " + PlanningVariable.class.getSimpleName()
                    + " annotated property (" + variableDescriptor.getVariableName()
                    + ") that refers to a " + ValueRangeProvider.class.getSimpleName()
                    + " annotated method (" + rangeReadMethodAccessor.getReadMethod()
                    + ") that does not return a " + Collection.class.getSimpleName() + ".");
        }
    }

    public boolean isEntityDependent() {
        return false;
    }

    public Collection<?> extractAllValues(Solution solution) {
        return (Collection<?>) rangeReadMethodAccessor.read(solution);
    }

    public Collection<?> extractValues(Solution solution, Object entity) {
        return extractAllValues(solution);
    }

    public long getValueCount(Solution solution, Object entity) {
        return extractAllValues(solution).size();
    }

    @Override
    public boolean isValuesCacheable() {
        return true;
    }

}
