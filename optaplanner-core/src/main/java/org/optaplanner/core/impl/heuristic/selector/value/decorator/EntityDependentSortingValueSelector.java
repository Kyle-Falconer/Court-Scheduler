/*
 * Copyright 2012 JBoss Inc
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

package org.optaplanner.core.impl.heuristic.selector.value.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorter;
import org.optaplanner.core.impl.heuristic.selector.value.AbstractValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.ValueSelector;
import org.optaplanner.core.impl.phase.AbstractSolverPhaseScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class EntityDependentSortingValueSelector extends AbstractValueSelector {

    protected final ValueSelector childValueSelector;
    protected final SelectionCacheType cacheType;
    protected final SelectionSorter sorter;

    protected ScoreDirector scoreDirector = null;

    public EntityDependentSortingValueSelector(ValueSelector childValueSelector, SelectionCacheType cacheType,
            SelectionSorter sorter) {
        this.childValueSelector = childValueSelector;
        this.cacheType = cacheType;
        this.sorter = sorter;
        if (childValueSelector.isNeverEnding()) {
            throw new IllegalStateException("The selector (" + this
                    + ") has a childValueSelector (" + childValueSelector
                    + ") with neverEnding (" + childValueSelector.isNeverEnding() + ").");
        }
        if (cacheType != SelectionCacheType.STEP) {
            throw new IllegalArgumentException("The selector (" + this
                    + ") does not support the cacheType (" + cacheType + ").");
        }
        solverPhaseLifecycleSupport.addEventListener(childValueSelector);
    }

    public ValueSelector getChildValueSelector() {
        return childValueSelector;
    }

    @Override
    public SelectionCacheType getCacheType() {
        return cacheType;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void phaseStarted(AbstractSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        scoreDirector = phaseScope.getScoreDirector();
    }

    @Override
    public void phaseEnded(AbstractSolverPhaseScope phaseScope) {
        super.phaseEnded(phaseScope);
        scoreDirector = null;
    }

    public PlanningVariableDescriptor getVariableDescriptor() {
        return childValueSelector.getVariableDescriptor();
    }

    public long getSize(Object entity) {
        return childValueSelector.getSize(entity);
    }

    public boolean isContinuous() {
        return false;
    }

    public boolean isNeverEnding() {
        return false;
    }

    public Iterator<Object> iterator(Object entity) {
        long childSize = childValueSelector.getSize(entity);
        if (childSize > (long) Integer.MAX_VALUE) {
            throw new IllegalStateException("The selector (" + this
                    + ") has a childValueSelector (" + childValueSelector
                    + ") with childSize (" + childSize
                    + ") which is higher than Integer.MAX_VALUE.");
        }
        List<Object> cachedValueList = new ArrayList<Object>((int) childSize);
        CollectionUtils.addAll(cachedValueList, childValueSelector.iterator(entity));
        logger.trace("    Created cachedValueList with size ({}) in valueSelector ({}).",
                cachedValueList.size(), this);
        sorter.sort(scoreDirector, cachedValueList);
        logger.trace("    Sorted cachedValueList with size ({}) in valueSelector({}).",
                cachedValueList.size(), this);
        return cachedValueList.iterator();
    }

    @Override
    public String toString() {
        return "Sorting(" + childValueSelector + ")";
    }

}
