package courtscheduler.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class SharedTeams extends Constraint {

    List<Integer> sharedTeamList = new ArrayList<Integer>();

    public List<Integer> getSharedTeamList() {
        return sharedTeamList;
    }

    public void setSharedTeamList(List<Integer> sharedTeamList) {
        this.sharedTeamList = sharedTeamList;
    }
}
