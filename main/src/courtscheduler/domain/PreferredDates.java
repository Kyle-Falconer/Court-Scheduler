package courtscheduler.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PreferredDates extends DateConstraint {

    List<MatchDate> preferredDateList = new ArrayList<MatchDate>();

	public PreferredDates(List<MatchDate> preferredDateList) {
		this.preferredDateList = preferredDateList;
	}

    public List<MatchDate> getPreferredDateList() {
        return preferredDateList;
    }
}
