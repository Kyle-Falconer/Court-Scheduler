package courtscheduler.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayOnceRequests extends Constraint {

    List<Integer> playOnceTeamList = new ArrayList<Integer>();

	public PlayOnceRequests(List<Integer> playOnceTeamList) {
		this.playOnceTeamList = playOnceTeamList;
	}

    public List<Integer> getPlayOnceTeamList() {
        return playOnceTeamList;
    }
}
