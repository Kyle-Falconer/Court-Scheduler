package courtscheduler.domain;

import org.apache.commons.lang.builder.CompareToBuilder;

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

        int i = new CompareToBuilder()
                .append(a.getDay(), b.getDay())
                .append(a.getTime(), b.getTime())
                .toComparison();
        //System.out.println(">> " + a + " " + b + " " + i);
        return i;
        //0 + (int)(Math.random() * ((10 - 0) + 1)); //FIXME
    }

}

