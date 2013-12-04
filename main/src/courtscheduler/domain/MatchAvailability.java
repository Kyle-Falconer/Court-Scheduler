/*
 * Copyright 2013 Michael Adams, CJ Done, Charles Eswine, Kyle Falconer,
 *  Will Gorman, Stephen Kaysen, Pat McCroskey and Matthew Swinney
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */


package courtscheduler.domain;

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
		badDates = new DateConstraint(new DateConstraint(), DateConstraint.getStandardDates());
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
			return DateConstraint.getIntersection(only1, only2);
	}

	public boolean canPlayIn(MatchSlot matchSlot) {
		if (onlyDates != null && onlyDates.isTrue(matchSlot)) {
			// onlyDates true means team can't play then
			return false;
		}
		return badDates.isTrue(matchSlot);
	}

	public boolean isPreferredSlot(MatchSlot matchSlot) {
		// again, true means team can't play then
		// I deserve to be mocked for this design decision, it's true
		return !prefDates.isTrue(matchSlot);
	}

    public SharedTeams getNotSameTimeAs() {
        return notSameTimeAs;
    }

    public boolean notScheduleable(){
        DateConstraint hardConstraints;
        if(this.onlyDates!=null){
            hardConstraints=new DateConstraint(this.onlyDates.getInverse(),this.badDates);
        }
        else{
            hardConstraints= this.badDates;
        }

        return hardConstraints.isFull();
    }

}
