package org.courtscheduler;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.solution.Solution;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 9/21/13
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Courts implements Solution {

    /**
     * Returns the Score of this Solution.
     *
     * @return null if the Solution is uninitialized
     *         or the last calculated Score is dirty the new Score has not yet been recalculated
     */
    @Override
    public Score getScore() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called by the {@link org.optaplanner.core.api.solver.Solver} when the Score of this Solution has been calculated.
     *
     * @param score null if the Solution has changed and the new Score has not yet been recalculated
     */
    @Override
    public void setScore(Score score) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called by the {@link org.optaplanner.core.impl.score.director.drools.DroolsScoreDirector} when the {@link org.optaplanner.core.impl.solution.Solution} needs to be inserted
     * into an empty {@link org.kie.api.runtime.KieSession}.
     * These facts can be used by the score rules.
     * They don't change during planning (except through {@link org.optaplanner.core.impl.solver.ProblemFactChange} events).
     * <p/>
     * Do not include the planning entities as problem facts:
     * they are automatically inserted into the {@link org.kie.api.runtime.KieSession} if and only if they are initialized.
     * When they are initialized later, they are also automatically inserted.
     *
     * @return never null (although an empty collection is allowed),
     *         all the facts of this solution except for the planning entities
     */
    @Override
    public Collection<?> getProblemFacts() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
