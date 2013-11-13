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
    DateConstraint onlyDates;
	SharedTeams notSameTimeAs;



    public MatchAvailability(){
		badDates = new DateConstraint();
		prefDates = new DateConstraint();
		// intentionally leaving onlyDates uninitialized
		notSameTimeAs = new SharedTeams();
    }

    public MatchAvailability(MatchAvailability m1, MatchAvailability m2){
		this();
        // TODO: set the intersection of these two, m1 and m2 to be this MatchAvailability
		// in all things, not just in dates-- a match can't happen at the same time as a match with
		// a team that shares players with one of the teams
        notSameTimeAs = new SharedTeams(m1.getNotSameTimeAs(), m2.getNotSameTimeAs());
		badDates = new DateConstraint(m1.badDates, m2.badDates);
		prefDates = new DateConstraint(m1.prefDates, m2.prefDates);
		onlyDates = mergeOnlyDates(m1.onlyDates, m2.onlyDates);
    }

	private static DateConstraint mergeOnlyDates(DateConstraint only1, DateConstraint only2) {
		if (only1 == null && only2 == null)
			return null;
		else if (only1 == null)
			return only2;
		else if (only2 == null)
			return only1;
		else
			return new DateConstraint(only1, only2);
	}

	public boolean canPlayIn(MatchSlot matchSlot) {
		return badDates.isTrue(matchSlot) && (onlyDates == null || onlyDates.isTrue(matchSlot));
	}

    public SharedTeams getNotSameTimeAs() {
        return notSameTimeAs;
    }
}
