package courtscheduler.domain;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Charles
 * Date: 10/3/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchStrengthComparator implements Comparator<MatchSlot> {
    public int compare(MatchSlot a, MatchSlot b) {
        /* This method will compare two teams based on their constraints.
        * Teams with fewer constraints should sort "higher" than teams with more constraints,
        * since teams with fewer constraints are more likely to fit into a given match.
        * See 4.3.5.3 in the OptaPlanner 6.0 documentation for more. --MS */
        return 0 + (int)(Math.random() * ((10 - 0) + 1)); //FIXME
    }

}

