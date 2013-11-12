package courtscheduler.domain;

import java.util.List;

/**
 * "I can play on these timeslots and these days"
 * User: Kyle
 * Date: 10/21/13
 * Time: 9:21 PM
 */
public class MatchAvailability {

    Constraint[] constraints;
    DateConstraint badDates;
    DateConstraint prefDates;
	SharedTeams notSameTimeAs;



    public MatchAvailability(){
		badDates = new DateConstraint();
		prefDates = new DateConstraint();
		notSameTimeAs = new SharedTeams();
    }

    public MatchAvailability(MatchAvailability m1, MatchAvailability m2){
		this();
        // TODO: set the intersection of these two, m1 and m2 to be this MatchAvailability
		// in all things, not just in dates-- a match can't happen at the same time as a match with
		// a team that shares players with one of the teams
        notSameTimeAs = new SharedTeams(m1.getNotSameTimeAs(), m2.getNotSameTimeAs());
		badDates = new DateConstraint(m1.badDates, m2.badDates);
    }

	public boolean canPlayIn(MatchSlot matchSlot) {
		return badDates.isTrue(matchSlot);
	}

    public SharedTeams getNotSameTimeAs() {
        return notSameTimeAs;
    }
}
