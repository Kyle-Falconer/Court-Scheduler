package org.optaplanner.core.impl.constructionheuristic.decider;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.constructionheuristic.decider.forager.ConstructionHeuristicForager;
import org.optaplanner.core.impl.constructionheuristic.placer.AbstractEntityPlacer;
import org.optaplanner.core.impl.constructionheuristic.placer.Placement;
import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicMoveScope;
import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicSolverPhaseScope;
import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicStepScope;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.termination.Termination;

public class ConstructionHeuristicDecider extends AbstractEntityPlacer {

    protected final Termination termination;
    protected final ConstructionHeuristicForager forager;

    protected boolean assertMoveScoreFromScratch = false;
    protected boolean assertExpectedUndoMoveScore = false;

    public ConstructionHeuristicDecider(Termination termination, ConstructionHeuristicForager forager) {
        this.termination = termination;
        this.forager = forager;
    }

    public void setAssertMoveScoreFromScratch(boolean assertMoveScoreFromScratch) {
        this.assertMoveScoreFromScratch = assertMoveScoreFromScratch;
    }

    public void setAssertExpectedUndoMoveScore(boolean assertExpectedUndoMoveScore) {
        this.assertExpectedUndoMoveScore = assertExpectedUndoMoveScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void solvingStarted(DefaultSolverScope solverScope) {
        forager.solvingStarted(solverScope);
    }

    public void phaseStarted(ConstructionHeuristicSolverPhaseScope phaseScope) {
        forager.phaseStarted(phaseScope);
    }

    public void stepStarted(ConstructionHeuristicStepScope stepScope) {
        forager.stepStarted(stepScope);
    }

    public void stepEnded(ConstructionHeuristicStepScope stepScope) {
        forager.stepEnded(stepScope);
    }

    public void phaseEnded(ConstructionHeuristicSolverPhaseScope phaseScope) {
        forager.phaseEnded(phaseScope);
    }

    public void solvingEnded(DefaultSolverScope solverScope) {
        forager.solvingEnded(solverScope);
    }

    public void decideNextStep(ConstructionHeuristicStepScope stepScope, Placement placement) {
        int moveIndex = 0;
        for (Move move : placement) {
            ConstructionHeuristicMoveScope moveScope = new ConstructionHeuristicMoveScope(stepScope);
            moveScope.setMoveIndex(moveIndex);
            moveIndex++;
            moveScope.setMove(move);
            // TODO use Selector filtering to filter out not doable moves
            if (!move.isMoveDoable(stepScope.getScoreDirector())) {
                logger.trace("        Move index ({}) not doable, ignoring move ({}).", moveScope.getMoveIndex(), move);
            } else {
                doMove(moveScope);
                if (forager.isQuitEarly()) {
                    break;
                }
            }
            if (termination.isPhaseTerminated(stepScope.getPhaseScope())) {
                break;
            }
        }
        stepScope.setSelectedMoveCount((long) moveIndex);
        ConstructionHeuristicMoveScope pickedMoveScope = forager.pickMove(stepScope);
        if (pickedMoveScope != null) {
            Move step = pickedMoveScope.getMove();
            stepScope.setStep(step);
            if (logger.isDebugEnabled()) {
                stepScope.setStepString(step.toString());
            }
            stepScope.setUndoStep(pickedMoveScope.getUndoMove());
            stepScope.setScore(pickedMoveScope.getScore());
        }
    }

    private void doMove(ConstructionHeuristicMoveScope moveScope) {
        ScoreDirector scoreDirector = moveScope.getScoreDirector();
        Move move = moveScope.getMove();
        Move undoMove = move.createUndoMove(scoreDirector);
        moveScope.setUndoMove(undoMove);
        move.doMove(scoreDirector);
        processMove(moveScope);
        undoMove.doMove(scoreDirector);
        if (assertExpectedUndoMoveScore) {
            ConstructionHeuristicSolverPhaseScope phaseScope = moveScope.getStepScope().getPhaseScope();
            phaseScope.assertExpectedUndoMoveScore(move, undoMove);
        }
        logger.trace("        Move index ({}), score ({}) for move ({}).",
                moveScope.getMoveIndex(), moveScope.getScore(), moveScope.getMove());
    }

    private void processMove(ConstructionHeuristicMoveScope moveScope) {
        Score score = moveScope.getStepScope().getPhaseScope().calculateScore();
        if (assertMoveScoreFromScratch) {
            moveScope.getStepScope().getPhaseScope().assertWorkingScoreFromScratch(score, moveScope.getMove());
        }
        moveScope.setScore(score);
        forager.addMove(moveScope);
    }

}
