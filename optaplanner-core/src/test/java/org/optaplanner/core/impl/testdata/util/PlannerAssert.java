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

package org.optaplanner.core.impl.testdata.util;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.mockito.Matchers;
import org.optaplanner.core.impl.heuristic.selector.entity.EntitySelector;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelector;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.heuristic.selector.value.EntityIndependentValueSelector;
import org.optaplanner.core.impl.heuristic.selector.value.ValueSelector;
import org.optaplanner.core.impl.move.CompositeMove;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.phase.AbstractSolverPhaseScope;
import org.optaplanner.core.impl.phase.event.SolverPhaseLifecycleListener;
import org.optaplanner.core.impl.phase.step.AbstractStepScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

import static org.mockito.Mockito.*;

public class PlannerAssert extends Assert {

    public static final long DO_NOT_ASSERT_SIZE = Long.MIN_VALUE;

    public static void assertInstanceOf(Class expectedClass, Object actualInstance) {
        assertInstanceOf(null, expectedClass, actualInstance);
    }

    public static void assertInstanceOf(String message, Class expectedClass, Object actualInstance) {
        if (!expectedClass.isInstance(actualInstance)) {
            String cleanMessage = message == null ? "" : message;
            throw new ComparisonFailure(cleanMessage, expectedClass.getName(),
                    actualInstance == null ? "null" : actualInstance.getClass().getName());
        }
    }

    public static void assertNotInstanceOf(Class expectedClass, Object actualInstance) {
        assertNotInstanceOf(null, expectedClass, actualInstance);
    }

    public static void assertNotInstanceOf(String message, Class expectedClass, Object actualInstance) {
        if (expectedClass.isInstance(actualInstance)) {
            String cleanMessage = message == null ? "" : message;
            throw new ComparisonFailure(cleanMessage, "not " + expectedClass.getName(),
                    actualInstance == null ? "null" : actualInstance.getClass().getName());
        }
    }

    // ************************************************************************
    // SolverPhaseLifecycleListener methods
    // ************************************************************************

    public static void verifySolverPhaseLifecycle(SolverPhaseLifecycleListener lifecycleListener,
            int solvingCount, int phaseCount, int stepCount) {
        verify(lifecycleListener, times(solvingCount)).solvingStarted(Matchers.<DefaultSolverScope>any());
        verify(lifecycleListener, times(phaseCount)).phaseStarted(Matchers.<AbstractSolverPhaseScope>any());
        verify(lifecycleListener, times(stepCount)).stepStarted(Matchers.<AbstractStepScope>any());
        verify(lifecycleListener, times(stepCount)).stepEnded(Matchers.<AbstractStepScope>any());
        verify(lifecycleListener, times(phaseCount)).phaseEnded(Matchers.<AbstractSolverPhaseScope>any());
        verify(lifecycleListener, times(solvingCount)).solvingEnded(Matchers.<DefaultSolverScope>any());
    }

    // ************************************************************************
    // CodeAssertable methods
    // ************************************************************************

    private static CodeAssertable convertToCodeAssertable(Object o) {
        assertNotNull(o);
        if (o instanceof CodeAssertable) {
            return (CodeAssertable) o;
        } else if (o instanceof ChangeMove) {
            ChangeMove changeMove = (ChangeMove) o;
            final String code = convertToCodeAssertable(changeMove.getEntity()).getCode()
                    + "=>" + convertToCodeAssertable(changeMove.getToPlanningValue()).getCode();
            return new CodeAssertable() {
                public String getCode() {
                    return code;
                }
            };
        } else if (o instanceof CompositeMove) {
            CompositeMove compositeMove = (CompositeMove) o;
            StringBuilder codeBuilder = new StringBuilder(compositeMove.getMoveList().size() * 80);
            for (Move move : compositeMove.getMoveList()) {
                codeBuilder.append("+").append(convertToCodeAssertable(move).getCode());
            }
            final String code = codeBuilder.substring(1);
            return new CodeAssertable() {
                public String getCode() {
                    return code;
                }
            };
        }
        throw new AssertionError(("o's class (" + o.getClass() + ") cannot be converted to CodeAssertable."));
    }

    public static void assertCode(String expectedCode, Object o) {
        CodeAssertable codeAssertable = convertToCodeAssertable(o);
        assertCode(expectedCode, codeAssertable);
    }

    public static void assertCode(String message, String expectedCode, Object o) {
        CodeAssertable codeAssertable = convertToCodeAssertable(o);
        assertCode(message, expectedCode, codeAssertable);
    }

    public static void assertCode(String expectedCode, CodeAssertable codeAssertable) {
        assertEquals(expectedCode, codeAssertable.getCode());
    }

    public static void assertCode(String message, String expectedCode, CodeAssertable codeAssertable) {
        assertEquals(message, expectedCode, codeAssertable.getCode());
    }

    public static <O> void assertCodesOfIterator(Iterator<O> iterator, String... codes) {
        assertNotNull(iterator);
        for (String code : codes) {
            assertTrue(iterator.hasNext());
            assertCode(code, iterator.next());
        }
    }

    public static <O> void assertAllCodesOfIterator(Iterator<O> iterator, String... codes) {
        assertCodesOfIterator(iterator, codes);
        assertFalse(iterator.hasNext());
    }

    public static void assertAllCodesOfMoveSelector(MoveSelector moveSelector, String... codes) {
        assertAllCodesOfMoveSelector(moveSelector, (long) codes.length, codes);
    }

    public static void assertAllCodesOfMoveSelector(MoveSelector moveSelector, long size, String... codes) {
        assertAllCodesOfIterator(moveSelector.iterator(), codes);
        assertEquals(false, moveSelector.isContinuous());
        assertEquals(false, moveSelector.isNeverEnding());
        if (size != DO_NOT_ASSERT_SIZE) {
            assertEquals(size, moveSelector.getSize());
        }
    }

    public static void assertCodesOfNeverEndingMoveSelector(MoveSelector moveSelector, String... codes) {
        assertCodesOfNeverEndingMoveSelector(moveSelector, (long) codes.length, codes);
    }

    public static void assertCodesOfNeverEndingMoveSelector(MoveSelector moveSelector, long size, String... codes) {
        Iterator<Move> iterator = moveSelector.iterator();
        assertCodesOfIterator(iterator, codes);
        assertTrue(iterator.hasNext());
        assertEquals(false, moveSelector.isContinuous());
        assertEquals(true, moveSelector.isNeverEnding());
        if (size != DO_NOT_ASSERT_SIZE) {
            assertEquals(size, moveSelector.getSize());
        }
    }

    public static void assertEmptyNeverEndingMoveSelector(MoveSelector moveSelector) {
        assertEmptyNeverEndingMoveSelector(moveSelector, 0L);
    }

    public static void assertEmptyNeverEndingMoveSelector(MoveSelector moveSelector, long size) {
        Iterator<Move> iterator = moveSelector.iterator();
        assertFalse(iterator.hasNext());
        assertEquals(false, moveSelector.isContinuous());
        assertEquals(true, moveSelector.isNeverEnding());
        if (size != DO_NOT_ASSERT_SIZE) {
            assertEquals(size, moveSelector.getSize());
        }
    }

    public static void assertAllCodesOfEntitySelector(EntitySelector entitySelector, String... codes) {
        assertAllCodesOfEntitySelector(entitySelector, (long) codes.length, codes);
    }

    public static void assertAllCodesOfEntitySelector(EntitySelector entitySelector, long size, String... codes) {
        assertAllCodesOfIterator(entitySelector.iterator(), codes);
        assertEquals(false, entitySelector.isContinuous());
        assertEquals(false, entitySelector.isNeverEnding());
        if (size != DO_NOT_ASSERT_SIZE) {
            assertEquals(size, entitySelector.getSize());
        }
    }

    public static void assertAllCodesOfValueSelector(EntityIndependentValueSelector valueSelector,
            String... codes) {
        assertAllCodesOfValueSelector(valueSelector, (long) codes.length, codes);
    }

    public static void assertAllCodesOfValueSelector(EntityIndependentValueSelector valueSelector, long size,
            String... codes) {
        assertAllCodesOfIterator(valueSelector.iterator(), codes);
        assertEquals(false, valueSelector.isContinuous());
        assertEquals(false, valueSelector.isNeverEnding());
        if (size != DO_NOT_ASSERT_SIZE) {
            assertEquals(size, valueSelector.getSize());
        }
    }

    public static void assertAllCodesOfValueSelectorForEntity(ValueSelector valueSelector, Object entity,
            String... codes) {
        assertAllCodesOfValueSelectorForEntity(valueSelector, entity, (long) codes.length, codes);
    }

    public static void assertAllCodesOfValueSelectorForEntity(ValueSelector valueSelector, Object entity,
            long size,  String... codes) {
        assertAllCodesOfIterator(valueSelector.iterator(entity), codes);
        assertEquals(false, valueSelector.isContinuous());
        assertEquals(false, valueSelector.isNeverEnding());
        if (size != DO_NOT_ASSERT_SIZE) {
            assertEquals(size, valueSelector.getSize(entity));
        }
    }

    private PlannerAssert() {
    }

}
