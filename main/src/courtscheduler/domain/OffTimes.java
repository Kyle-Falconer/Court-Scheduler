package courtscheduler.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OffTimes extends Constraint{

    List<MatchTime> offTimeList = new ArrayList<MatchTime>();

    public List<MatchTime> getOffTimeList() {
        return offTimeList;
    }

    public void setOffTimeList(List<MatchTime> offTimeList) {
        this.offTimeList = offTimeList;
    }
}
