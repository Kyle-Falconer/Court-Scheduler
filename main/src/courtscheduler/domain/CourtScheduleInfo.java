package courtscheduler.domain;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourtScheduleInfo {

    private Match firstMatchDateTime;
    private Match lastMatchDateTime;
    private Calendar firstDay;

    public void setFirstMatchDateTime(Match firstMatchDateTime) {
        this.firstMatchDateTime = firstMatchDateTime;
    }
    public void setLastMatchDateTime(Match lastMatchDateTime){
        this.lastMatchDateTime = lastMatchDateTime;
    }
    public void setFirstDay(Calendar firstDay){
        this.firstDay=firstDay;
    }


    public boolean isInPlanningWindow(MatchDate matchDate) {
        return  firstMatchDateTime.getMatchDate(firstDay).compareTo(matchDate)>= 0  && lastMatchDateTime.getMatchDate(firstDay).compareTo(matchDate) < 1  ;
    }


}
