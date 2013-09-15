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

package org.optaplanner.core.impl.constructionheuristic;

import org.optaplanner.core.impl.constructionheuristic.decider.ConstructionHeuristicDecider;
import org.optaplanner.core.impl.constructionheuristic.placer.EntityPlacer;
import org.optaplanner.core.impl.constructionheuristic.placer.Placement;
import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicSolverPhaseScope;
import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicStepScope;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.phase.AbstractSolverPhase;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

/**
 * Default implementation of {@link ConstructionHeuristicSolverPhase}.
 */
public class DefaultConstructionHeuristicSolverPhase extends AbstractSolverPhase implements ConstructionHeuristicSolverPhase {

    protected EntityPlacer entityPlacer;
    protected ConstructionHeuristicDecider decider;

    protected boolean assertStepScoreFromScratch = false;
    protected boolean assertExpectedStepScore = false;

    public void setEntityPlacer(EntityPlacer entityPlacer) {
        this.entityPlacer = entityPlacer;
    }

    public void setDecider(ConstructionHeuristicDecider decider) {
        this.decider = decider;
    }

    public void setAssertStepScoreFromScratch(boolean assertStepScoreFromScratch) {
        this.assertStepScoreFromScratch = assertStepScoreFromScratch;
    }

    public void setAssertExpectedStepScore(boolean assertExpectedStepScore) {
        this.assertExpectedStepScore = assertExpectedStepScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void solve(DefaultSolverScope solverScope) {
        ConstructionHeuristicSolverPhaseScope phaseScope = new ConstructionHeuristicSolverPhaseScope(solverScope);
        phaseStarted(phaseScope);

        ConstructionHeuristicStepScope stepScope = new ConstructionHeuristicStepScope(phaseScope);
        for (Placement placement : entityPlacer) {
            stepStarted(stepScope);
            decider.decideNextStep(stepScope, placement);
            if (stepScope.getStep() == null) {
                if (termination.isPhaseTerminated(phaseScope)) {
                    logger.trace("    Step index ({}), time spend ({}) terminated without picking a nextStep.",
                            stepScope.getStepIndex(),
                            stepScope.getPhaseScope().calculateSolverTimeMillisSpend());
                } else if (stepScope.getSelectedMoveCount() == 0L) {
                    logger.warn("    No doable selected move at step index ({}), time spend ({})."
                            + " Terminating phase early.",
                            stepScope.getStepIndex(),
                            stepScope.getPhaseScope().calculateSolverTimeMillisSpend());
                } else {
                    throw new IllegalStateException("The step index (" + stepScope.getStepIndex()
                            + ") has selected move count (" + stepScope.getSelectedMoveCount()
                            + ") but failed to pick a nextStep (" + stepScope.getStep() + ").");
                }
                // Although stepStarted has been called, stepEnded is not called for this step
                break;
            }
            doStep(stepScope);
            stepEnded(stepScope);
            phaseScope.setLastCompletedStepScope(stepScope);
            stepScope = new ConstructionHeuristicStepScope(phaseScope);
            if (termination.isPhaseTerminated(phaseScope)) {
                break;
            }
        }
        phaseEnded(phaseScope);
    }

    private void doStep(ConstructionHeuristicStepScope stepScope) {
        ConstructionHeuristicSolverPhaseScope phaseScope = stepScope.getPhaseScope();
        Move nextStep = stepScope.getStep();
        nextStep.doMove(stepScope.getScoreDirector());
        // there is no need to recalculate the score, but we still need to set it
        phaseScope.getWorkingSolution().setScore(stepScope.getScore());
        if (assertStepScoreFromScratch) {
            phaseScope.assertWorkingScoreFromScratch(stepScope.getScore(), nextStep);
        }
        if (assertExpectedStepScore) {
            phaseScope.assertExpectedWorkingScore(stepScope.getScore(), nextStep);
        }
    }

    @Override
    public void solvingStarted(DefaultSolverScope solverScope) {
        super.solvingStarted(solverScope);
        entityPlacer.solvingStarted(solverScope);
        decider.solvingStarted(solverScope);
    }

    public void phaseStarted(ConstructionHeuristicSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        entityPlacer.phaseStarted(phaseScope);
        decider.phaseStarted(phaseScope);
    }

    public void stepStarted(ConstructionHeuristicStepScope stepScope) {
        super.stepStarted(stepScope);
        entityPlacer.stepStarted(stepScope);
        decider.stepStarted(stepScope);
    }

    public void stepEnded(ConstructionHeuristicStepScope stepScope) {
        super.stepEnded(stepScope);
        entityPlacer.stepEnded(stepScope);
        decider.stepEnded(stepScope);
        if (logger.isDebugEnabled()) {
            long timeMillisSpend = stepScope.getPhaseScope().calculateSolverTimeMillisSpend();
            logger.debug("    Step index ({}), time spend ({}), score ({}), selected move count ({})"
                    + " for constructing step ({}).",
                    stepScope.getStepIndex(), timeMillisSpend,
                    stepScope.getScore(),
                    stepScope.getSelectedMoveCount(),
                    stepScope.getStepString());
        }
    }

    public void phaseEnded(ConstructionHeuristicSolverPhaseScope phaseScope) {
        super.phaseEnded(phaseScope);
        Solution newBestSolution = phaseScope.getScoreDirector().cloneWorkingSolution();
        int newBestUninitializedVariableCount = phaseScope.getSolutionDescriptor()
                .countUninitializedVariables(newBestSolution);
        bestSolutionRecaller.updateBestSolution(phaseScope.getSolverScope(),
                newBestSolution, newBestUninitializedVariableCount);
        entityPlacer.phaseEnded(phaseScope);
        decider.phaseEnded(phaseScope);
        logger.info("Phase ({}) constructionHeuristic ended: step total ({}), time spend ({}), best score ({}).",
                phaseIndex,
                phaseScope.getLastCompletedStepScope().getStepIndex() + 1,
                phaseScope.calculateSolverTimeMillisSpend(),
                phaseScope.getBestScore());
    }

    @Override
    public void solvingEnded(DefaultSolverScope solverScope) {
        super.solvingStarted(solverScope);
        entityPlacer.solvingEnded(solverScope);
        decider.solvingEnded(solverScope);
    }

}
