package courtscheduler.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchTime {

    private String startTime;
    private String endTime;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

}
