package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Conference {

    Team team;
    Integer conference;

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setConference(Integer conference) {
        this.conference = conference;
    }

    public Integer getConference() {
        return conference;
    }
}
