package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Match {

    private Team t1;
    private Team t2;
    MatchAvailability avail;   // calculated based on the intersection of the teams' availabilities

    public Match(Team t1, Team t2){
        this.t1 = t1;
        this.t2 = t2;
        avail = new MatchAvailability(t1.getAvailability(), t2.getAvailability());
    }

}
