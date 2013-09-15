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

package org.optaplanner.core.config.heuristic.selector.value;

import java.util.Comparator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.heuristic.selector.SelectorConfig;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.domain.value.FromEntityPropertyPlanningValueRangeDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.ComparatorSelectionSorter;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionProbabilityWeightFactory;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorter;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterOrder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.WeightFactorySelectionSorter;
import org.optaplanner.core.impl.heuristic.selector.value.EntityIndependentValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.FromEntityPropertyValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.FromSolutionPropertyValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.ValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.CachingValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.EntityDependentSortingValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.InitializedValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.ProbabilityValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.ReinitializeVariableValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.ShufflingValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.decorator.SortingValueSelector;

@XStreamAlias("valueSelector")
public class ValueSelectorConfig extends SelectorConfig {

    protected String variableName = null;

    protected SelectionCacheType cacheType = null;
    protected SelectionOrder selectionOrder = null;

    // TODO filterClass

    protected ValueSorterManner sorterManner = null;
    protected Class<? extends Comparator> sorterComparatorClass = null;
    protected Class<? extends SelectionSorterWeightFactory> sorterWeightFactoryClass = null;
    protected SelectionSorterOrder sorterOrder = null;
    protected Class<? extends SelectionSorter> sorterClass = null;

    protected Class<? extends SelectionProbabilityWeightFactory> probabilityWeightFactoryClass = null;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public SelectionCacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(SelectionCacheType cacheType) {
        this.cacheType = cacheType;
    }

    public SelectionOrder getSelectionOrder() {
        return selectionOrder;
    }

    public void setSelectionOrder(SelectionOrder selectionOrder) {
        this.selectionOrder = selectionOrder;
    }

    public ValueSorterManner getSorterManner() {
        return sorterManner;
    }

    public void setSorterManner(ValueSorterManner sorterManner) {
        this.sorterManner = sorterManner;
    }

    public Class<? extends Comparator> getSorterComparatorClass() {
        return sorterComparatorClass;
    }

    public void setSorterComparatorClass(Class<? extends Comparator> sorterComparatorClass) {
        this.sorterComparatorClass = sorterComparatorClass;
    }

    public Class<? extends SelectionSorterWeightFactory> getSorterWeightFactoryClass() {
        return sorterWeightFactoryClass;
    }

    public void setSorterWeightFactoryClass(Class<? extends SelectionSorterWeightFactory> sorterWeightFactoryClass) {
        this.sorterWeightFactoryClass = sorterWeightFactoryClass;
    }

    public SelectionSorterOrder getSorterOrder() {
        return sorterOrder;
    }

    public void setSorterOrder(SelectionSorterOrder sorterOrder) {
        this.sorterOrder = sorterOrder;
    }

    public Class<? extends SelectionSorter> getSorterClass() {
        return sorterClass;
    }

    public void setSorterClass(Class<? extends SelectionSorter> sorterClass) {
        this.sorterClass = sorterClass;
    }

    public Class<? extends SelectionProbabilityWeightFactory> getProbabilityWeightFactoryClass() {
        return probabilityWeightFactoryClass;
    }

    public void setProbabilityWeightFactoryClass(Class<? extends SelectionProbabilityWeightFactory> probabilityWeightFactoryClass) {
        this.probabilityWeightFactoryClass = probabilityWeightFactoryClass;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    /**
     *
     * @param configPolicy never null
     * @param entityDescriptor never null
     * @param minimumCacheType never null, If caching is used (different from {@link SelectionCacheType#JUST_IN_TIME}),
     * then it should be at least this {@link SelectionCacheType} because an ancestor already uses such caching
     * and less would be pointless.
     * @param inheritedSelectionOrder never null
     * @return never null
     */
    public ValueSelector buildValueSelector(HeuristicConfigPolicy configPolicy,
            PlanningEntityDescriptor entityDescriptor,
            SelectionCacheType minimumCacheType, SelectionOrder inheritedSelectionOrder) {
        PlanningVariableDescriptor variableDescriptor = deduceVariableDescriptor(entityDescriptor, variableName);
        SelectionCacheType resolvedCacheType = SelectionCacheType.resolve(cacheType, minimumCacheType);
        SelectionOrder resolvedSelectionOrder = SelectionOrder.resolve(selectionOrder,
                inheritedSelectionOrder);

        validateCacheTypeVersusSelectionOrder(resolvedCacheType, resolvedSelectionOrder);
        validateSorting(resolvedSelectionOrder);
        validateProbability(resolvedSelectionOrder);

        // baseValueSelector and lower should be SelectionOrder.ORIGINAL if they are going to get cached completely
        ValueSelector valueSelector = buildBaseValueSelector(configPolicy, variableDescriptor,
                SelectionCacheType.max(minimumCacheType, resolvedCacheType),
                determineBaseRandomSelection(variableDescriptor, resolvedCacheType, resolvedSelectionOrder));

//        valueSelector = applyFiltering(variableDescriptor, resolvedCacheType, resolvedSelectionOrder, valueSelector);
        valueSelector = applyInitializedChainedValueFilter(configPolicy, variableDescriptor,
                resolvedCacheType, resolvedSelectionOrder, valueSelector);
        valueSelector = applySorting(resolvedCacheType, resolvedSelectionOrder, valueSelector);
        valueSelector = applyProbability(resolvedCacheType, resolvedSelectionOrder, valueSelector);
        valueSelector = applyShuffling(resolvedCacheType, resolvedSelectionOrder, valueSelector);
        valueSelector = applyCaching(resolvedCacheType, resolvedSelectionOrder, valueSelector);
        valueSelector = applyReinitializeVariableFiltering(configPolicy, valueSelector);
        return valueSelector;
    }

    protected boolean determineBaseRandomSelection(PlanningVariableDescriptor variableDescriptor,
            SelectionCacheType resolvedCacheType, SelectionOrder resolvedSelectionOrder) {
        switch (resolvedSelectionOrder) {
            case ORIGINAL:
                return false;
            case SORTED:
            case SHUFFLED:
            case PROBABILISTIC:
                // baseValueSelector and lower should be ORIGINAL if they are going to get cached completely
                return false;
            case RANDOM:
                // Predict if caching will occur
                return resolvedCacheType.isNotCached() || (isBaseInherentlyCached(variableDescriptor) && !hasFiltering());
            default:
                throw new IllegalStateException("The selectionOrder (" + resolvedSelectionOrder
                        + ") is not implemented.");
        }
    }

    protected boolean isBaseInherentlyCached(PlanningVariableDescriptor variableDescriptor) {
        return !variableDescriptor.getValueRangeDescriptor().isEntityDependent();
    }

    private ValueSelector buildBaseValueSelector(
            HeuristicConfigPolicy configPolicy, PlanningVariableDescriptor variableDescriptor,
            SelectionCacheType minimumCacheType, boolean randomSelection) {
        if (variableDescriptor.getValueRangeDescriptor().isEntityDependent()) {
            FromEntityPropertyPlanningValueRangeDescriptor valueRangeDescriptor
                    = (FromEntityPropertyPlanningValueRangeDescriptor) variableDescriptor.getValueRangeDescriptor();
            // TODO should we ignore the minimumCacheType so it can be cached on changeMoves too?
            return new FromEntityPropertyValueSelector(valueRangeDescriptor, minimumCacheType, randomSelection);
        } else {
            // FromSolutionPropertyValueSelector caches by design, so it uses the minimumCacheType
            if (variableDescriptor.isPlanningValuesCacheable()) {
                if (minimumCacheType.compareTo(SelectionCacheType.PHASE) < 0) {
                    // TODO we probably want to default this to SelectionCacheType.JUST_IN_TIME
                    minimumCacheType = SelectionCacheType.PHASE;
                }
            } else {
                if (minimumCacheType.compareTo(SelectionCacheType.STEP) < 0) {
                    // TODO we probably want to default this to SelectionCacheType.JUST_IN_TIME
                    minimumCacheType = SelectionCacheType.STEP;
                }
            }
            return new FromSolutionPropertyValueSelector(variableDescriptor, minimumCacheType, randomSelection);
        }
    }

    private boolean hasFiltering() {
        return false; // NOT yet implemented
    }

    protected ValueSelector applyInitializedChainedValueFilter(HeuristicConfigPolicy configPolicy,
            PlanningVariableDescriptor variableDescriptor,
            SelectionCacheType resolvedCacheType, SelectionOrder resolvedSelectionOrder,
            ValueSelector valueSelector) {
        if (configPolicy.isInitializedChainedValueFilterEnabled()
                    && variableDescriptor.isChained()) {
            valueSelector = new InitializedValueSelector(valueSelector);
        }
        return valueSelector;
    }

    private void validateSorting(SelectionOrder resolvedSelectionOrder) {
        if ((sorterManner != null || sorterComparatorClass != null || sorterWeightFactoryClass != null
                || sorterOrder != null || sorterClass != null)
                && resolvedSelectionOrder != SelectionOrder.SORTED) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") with sorterManner ("  + sorterManner
                    + ") and sorterComparatorClass ("  + sorterComparatorClass
                    + ") and sorterWeightFactoryClass ("  + sorterWeightFactoryClass
                    + ") and sorterOrder ("  + sorterOrder
                    + ") and sorterClass ("  + sorterClass
                    + ") has a resolvedSelectionOrder (" + resolvedSelectionOrder
                    + ") that is not " + SelectionOrder.SORTED + ".");
        }
        if (sorterManner != null && sorterComparatorClass != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") has both a sorterManner (" + sorterManner
                    + ") and a sorterComparatorClass (" + sorterComparatorClass + ").");
        }
        if (sorterManner != null && sorterWeightFactoryClass != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") has both a sorterManner (" + sorterManner
                    + ") and a sorterWeightFactoryClass (" + sorterWeightFactoryClass + ").");
        }
        if (sorterManner != null && sorterClass != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") has both a sorterManner (" + sorterManner
                    + ") and a sorterClass (" + sorterClass + ").");
        }
        if (sorterManner != null && sorterOrder != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") with sorterManner (" + sorterManner
                    + ") has a non-null sorterOrder (" + sorterOrder + ").");
        }
        if (sorterComparatorClass != null && sorterWeightFactoryClass != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") has both a sorterComparatorClass (" + sorterComparatorClass
                    + ") and a sorterWeightFactoryClass (" + sorterWeightFactoryClass + ").");
        }
        if (sorterComparatorClass != null && sorterClass != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") has both a sorterComparatorClass (" + sorterComparatorClass
                    + ") and a sorterClass (" + sorterClass + ").");
        }
        if (sorterWeightFactoryClass != null && sorterClass != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") has both a sorterWeightFactoryClass (" + sorterWeightFactoryClass
                    + ") and a sorterClass (" + sorterClass + ").");
        }
        if (sorterClass != null && sorterOrder != null) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") with sorterClass (" + sorterClass
                    + ") has a non-null sorterOrder (" + sorterOrder + ").");
        }
    }

    private ValueSelector applySorting(SelectionCacheType resolvedCacheType, SelectionOrder resolvedSelectionOrder,
            ValueSelector valueSelector) {
        if (resolvedSelectionOrder == SelectionOrder.SORTED) {
            SelectionSorter sorter;
            if (sorterManner != null) {
                sorter = sorterManner.determineSorter(valueSelector.getVariableDescriptor());
            } else if (sorterComparatorClass != null) {
                Comparator<Object> sorterComparator = ConfigUtils.newInstance(this,
                        "sorterComparatorClass", sorterComparatorClass);
                sorter = new ComparatorSelectionSorter(sorterComparator,
                        SelectionSorterOrder.resolve(sorterOrder));
            } else if (sorterWeightFactoryClass != null) {
                SelectionSorterWeightFactory sorterWeightFactory = ConfigUtils.newInstance(this,
                        "sorterWeightFactoryClass", sorterWeightFactoryClass);
                sorter = new WeightFactorySelectionSorter(sorterWeightFactory,
                        SelectionSorterOrder.resolve(sorterOrder));
            } else if (sorterClass != null) {
                sorter = ConfigUtils.newInstance(this, "sorterClass", sorterClass);
            } else {
                throw new IllegalArgumentException("The valueSelectorConfig (" + this
                        + ") with resolvedSelectionOrder ("  + resolvedSelectionOrder
                        + ") needs a sorterManner (" + sorterManner
                        + ") or a sorterComparatorClass (" + sorterComparatorClass
                        + ") or a sorterWeightFactoryClass (" + sorterWeightFactoryClass
                        + ") or a sorterClass (" + sorterClass + ").");
            }
            if (valueSelector.getVariableDescriptor().getValueRangeDescriptor().isEntityDependent()
                    && resolvedCacheType == SelectionCacheType.STEP) {
                valueSelector = new EntityDependentSortingValueSelector(valueSelector,
                        resolvedCacheType, sorter);
            } else {
                if (!(valueSelector instanceof EntityIndependentValueSelector)) {
                    throw new IllegalArgumentException("The valueSelectorConfig (" + this
                            + ") with resolvedCacheType (" + resolvedCacheType
                            + ") and resolvedSelectionOrder (" + resolvedSelectionOrder
                            + ") needs to be based on a EntityIndependentValueSelector (" + valueSelector + ")."
                            + " Check your @" + ValueRangeProvider.class.getSimpleName() + " annotations.");
                }
                valueSelector = new SortingValueSelector((EntityIndependentValueSelector) valueSelector,
                        resolvedCacheType, sorter);
            }
        }
        return valueSelector;
    }

    private void validateProbability(SelectionOrder resolvedSelectionOrder) {
        if (probabilityWeightFactoryClass != null
                && resolvedSelectionOrder != SelectionOrder.PROBABILISTIC) {
            throw new IllegalArgumentException("The valueSelectorConfig (" + this
                    + ") with probabilityWeightFactoryClass (" + probabilityWeightFactoryClass
                    + ") has a resolvedSelectionOrder (" + resolvedSelectionOrder
                    + ") that is not " + SelectionOrder.PROBABILISTIC + ".");
        }
    }

    private ValueSelector applyProbability(SelectionCacheType resolvedCacheType, SelectionOrder resolvedSelectionOrder,
            ValueSelector valueSelector) {
        if (resolvedSelectionOrder == SelectionOrder.PROBABILISTIC) {
            if (probabilityWeightFactoryClass == null) {
                throw new IllegalArgumentException("The valueSelectorConfig (" + this
                        + ") with resolvedSelectionOrder (" + resolvedSelectionOrder
                        + ") needs a probabilityWeightFactoryClass ("
                        + probabilityWeightFactoryClass + ").");
            }
            SelectionProbabilityWeightFactory probabilityWeightFactory = ConfigUtils.newInstance(this,
                    "probabilityWeightFactoryClass", probabilityWeightFactoryClass);
            if (!(valueSelector instanceof EntityIndependentValueSelector)) {
                throw new IllegalArgumentException("The valueSelectorConfig (" + this
                        + ") with resolvedCacheType (" + resolvedCacheType
                        + ") and resolvedSelectionOrder (" + resolvedSelectionOrder
                        + ") needs to be based on a EntityIndependentValueSelector (" + valueSelector + ")."
                        + " Check your @" + ValueRangeProvider.class.getSimpleName() + " annotations.");
            }
            valueSelector = new ProbabilityValueSelector((EntityIndependentValueSelector) valueSelector,
                    resolvedCacheType, probabilityWeightFactory);
        }
        return valueSelector;
    }

    private ValueSelector applyShuffling(SelectionCacheType resolvedCacheType, SelectionOrder resolvedSelectionOrder,
            ValueSelector valueSelector) {
        if (resolvedSelectionOrder == SelectionOrder.SHUFFLED) {
            if (!(valueSelector instanceof EntityIndependentValueSelector)) {
                throw new IllegalArgumentException("The valueSelectorConfig (" + this
                        + ") with resolvedCacheType (" + resolvedCacheType
                        + ") and resolvedSelectionOrder (" + resolvedSelectionOrder
                        + ") needs to be based on a EntityIndependentValueSelector (" + valueSelector + ")."
                        + " Check your @" + ValueRangeProvider.class.getSimpleName() + " annotations.");
            }
            valueSelector = new ShufflingValueSelector((EntityIndependentValueSelector) valueSelector,
                    resolvedCacheType);
        }
        return valueSelector;
    }

    private ValueSelector applyCaching(SelectionCacheType resolvedCacheType, SelectionOrder resolvedSelectionOrder,
            ValueSelector valueSelector) {
        if (resolvedCacheType.isCached() && resolvedCacheType.compareTo(valueSelector.getCacheType()) > 0) {
            if (!(valueSelector instanceof EntityIndependentValueSelector)) {
                throw new IllegalArgumentException("The valueSelectorConfig (" + this
                        + ") with resolvedCacheType (" + resolvedCacheType
                        + ") and resolvedSelectionOrder (" + resolvedSelectionOrder
                        + ") needs to be based on a EntityIndependentValueSelector (" + valueSelector + ")."
                        + " Check your @" + ValueRangeProvider.class.getSimpleName() + " annotations.");
            }
            valueSelector = new CachingValueSelector((EntityIndependentValueSelector) valueSelector, resolvedCacheType,
                    resolvedSelectionOrder.toRandomSelectionBoolean());
        }
        return valueSelector;
    }

    private ValueSelector applyReinitializeVariableFiltering(HeuristicConfigPolicy configPolicy,
            ValueSelector valueSelector) {
        if (configPolicy.isReinitializeVariableFilterEnabled()) {
            valueSelector = new ReinitializeVariableValueSelector(valueSelector);
        }
        return valueSelector;
    }

    public void inherit(ValueSelectorConfig inheritedConfig) {
        super.inherit(inheritedConfig);
        if (variableName == null) {
            variableName = inheritedConfig.getVariableName();
        }
        cacheType = ConfigUtils.inheritOverwritableProperty(cacheType, inheritedConfig.getCacheType());
        selectionOrder = ConfigUtils.inheritOverwritableProperty(selectionOrder, inheritedConfig.getSelectionOrder());
        sorterManner = ConfigUtils.inheritOverwritableProperty(
                sorterManner, inheritedConfig.getSorterManner());
        sorterComparatorClass = ConfigUtils.inheritOverwritableProperty(
                sorterComparatorClass, inheritedConfig.getSorterComparatorClass());
        sorterWeightFactoryClass = ConfigUtils.inheritOverwritableProperty(
                sorterWeightFactoryClass, inheritedConfig.getSorterWeightFactoryClass());
        sorterOrder = ConfigUtils.inheritOverwritableProperty(
                sorterOrder, inheritedConfig.getSorterOrder());
        sorterClass = ConfigUtils.inheritOverwritableProperty(
                sorterClass, inheritedConfig.getSorterClass());
        probabilityWeightFactoryClass = ConfigUtils.inheritOverwritableProperty(
                probabilityWeightFactoryClass, inheritedConfig.getProbabilityWeightFactoryClass());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + variableName + ")";
    }

    /**
     * Build-in ways of sorting.
     */
    public static enum ValueSorterManner {
        INCREASING_STRENGTH;

        public SelectionSorter determineSorter(PlanningVariableDescriptor variableDescriptor) {
            SelectionSorter sorter;
            switch (this) {
                case INCREASING_STRENGTH:
                    sorter = variableDescriptor.getIncreasingStrengthSorter();
                    if (sorter == null) {
                        throw new IllegalArgumentException("The sorterManner (" + this
                                + ") on entity class ("
                                + variableDescriptor.getEntityDescriptor().getPlanningEntityClass()
                                + ")'s variable (" + variableDescriptor.getVariableName()
                                + ") fails because that variable getter's " + PlanningVariable.class.getSimpleName()
                                + " annotation does not declare any strength comparison.");
                    }
                    return sorter;
                default:
                    throw new IllegalStateException("The sorterManner ("
                            + this + ") is not implemented.");
            }
        }
    }

}
