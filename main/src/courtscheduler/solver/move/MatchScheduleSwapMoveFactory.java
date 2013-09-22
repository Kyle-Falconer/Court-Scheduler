package courtscheduler.solver.move;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchScheduleSwapMoveFactory implements MoveListFactory {
    /**
     * When it is called depends on the configured {@link org.optaplanner.core.impl.heuristic.selector.common.SelectionCacheType}.
     * <p/>
     * It can never support {@link org.optaplanner.core.impl.heuristic.selector.common.SelectionCacheType#JUST_IN_TIME},
     * because it returns a {@link java.util.List}, not an {@link java.util.Iterator}.
     *
     * @param solution never null, the {@link org.optaplanner.core.impl.solution.Solution} of which the {@link org.optaplanner.core.impl.move.Move}s need to be generated
     * @return never null
     */
    @Override
    public List<Move> createMoveList(Solution solution) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
