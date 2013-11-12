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

    private List<Integer> sharedTeamList;

	public SharedTeams() {
		sharedTeamList = new ArrayList<Integer>();
	}

    public SharedTeams(SharedTeams s1, SharedTeams s2) {
        this();

        for (Integer teamId : s1.getSharedTeamList())
            addSharedTeam(teamId);

        for (Integer teamId : s2.getSharedTeamList())
            if(!this.sharedTeamList.contains(teamId))
                addSharedTeam(teamId);
    }

    public List<Integer> getSharedTeamList() {
        return sharedTeamList;
    }

    public void addSharedTeam(Integer teamId){
        this.sharedTeamList.add(teamId);
    }
}
