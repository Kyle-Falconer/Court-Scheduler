package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Match {

    private MatchTime matchTime;
    private MatchDate matchDate;
	Integer courtId;

    public void setMatchTime(MatchTime matchTime) {
        this.matchTime = matchTime;
    }

    public MatchTime getMatchTime() {
        return matchTime;
    }

    public void setMatchDate(MatchDate matchDate) {
        this.matchDate = matchDate;
    }

    public MatchDate getMatchDate() {
        return matchDate;
    }

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Integer getCourtId() {
		return courtId;
	}
}
