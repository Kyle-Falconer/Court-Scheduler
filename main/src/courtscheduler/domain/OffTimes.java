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

    List<MatchTime> offTimeList;

    public OffTimes(){
        offTimeList=new ArrayList<MatchTime>();
    }
    public OffTimes(List<MatchTime> offTimeList){
        this.offTimeList=offTimeList;
    }
	public void setOffTimes(List<MatchTime> offTimeList) {
		offTimeList.addAll(offTimeList);
	}

    public List<MatchTime> getOffTimeList() {
        return offTimeList;
    }
}
