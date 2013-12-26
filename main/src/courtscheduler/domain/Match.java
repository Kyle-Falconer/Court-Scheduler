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

import courtscheduler.persistence.CourtScheduleInfo;
import org.joda.time.DateTimeConstants;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningEntity
public class Match {

    // defining properties: teams
    private Team team1;
    private Team team2;
    MatchAvailability avail;   // calculated based on the intersection of the teams' availabilities
    private MatchSlot matchSlot;

	private static CourtScheduleInfo info;

    public void setMatchSlot(MatchSlot matchSlot) {
        this.matchSlot = matchSlot;
    }

    @PlanningVariable(valueRangeProviderRefs = "matchSlot", strengthComparatorClass = MatchSlotStrengthComparator.class)
    public MatchSlot getMatchSlot() {
        return this.matchSlot;
    }

    public Match() {
    }

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        avail = new MatchAvailability(team1.getAvailability(), team2.getAvailability());
    }

    public Team getTeam1() {
        return this.team1;
    }

    public Integer getTeam1Id(){
        return this.team1.getTeamId();
    }

    public Team getTeam2() {
        return this.team2;
    }

    public Integer getTeam2Id(){
        return this.team2.getTeamId();
    }

    public Integer getTime() {
        return this.getMatchSlot().getTime();
    }
	public Integer getDate() {
        return this.getMatchSlot().getDay();
    }
	public Integer getCourt() {
		return this.getMatchSlot().getCourt();
	}
	public String getGender() {
		return team1.getGender(); // FIXME
	}

	public Match getThis() {
		// Why in the hell do we need this? So that the Drools Rules engine can access this.
		// Yeah, it sucks, but it's a lot easier than writing the arcane invocations of DRL, so. --MS
		return this;
	}

	public boolean containsTeamsFrom(Match other) {
		return this.team1.equals(other.team1) || this.team1.equals(other.team2)
				|| this.team2.equals(other.team1) || this.team2.equals(other.team2);
	}

    public boolean canBeOnSameDateWith(Match other) {
        boolean okay = true;
        if (this.team1.equals(other.team1) || this.team1.equals(other.team2)) {
            okay = team1.getBackToBack() || team1.getDoubleHeader();
        }
        if (this.team2.equals(other.team1) || this.team2.equals(other.team2)) {
            okay = okay && (team2.getBackToBack() || team2.getDoubleHeader());
        }
        return okay;
    }

	public String getBasketHeight() {
		return team1.getGrade(); // FIXME
	}

	public String toString() {
		return new StringBuilder()
				.append("[")
				.append(team1.getTeamId())
				.append("v")
				.append(team2.getTeamId())
				.append(" ")
				.append(matchSlot)
				.append("]")
				.toString();
	}

	public boolean getCanPlayInCurrentSlot() {
		return avail.canPlayIn(matchSlot);
	}
	public boolean getPreferToPlayInCurrentSlot() {
		return avail.isPreferredSlot(matchSlot);
	}

	public List<Integer> getNotSameTimeAs() {
		return avail.notSameTimeAs.getSharedTeamList();
	}

    public int compareDateTimes(Match o) {
        Integer thisDate = this.getDate();
        Integer otherDate = o.getDate();
        int dateCompare = thisDate.compareTo(otherDate);

        if (dateCompare != 0) {
            return dateCompare;
        } else {
            thisDate = this.getTime();
            otherDate = o.getTime();

            int timeCompare = thisDate.compareTo(otherDate);
            if (timeCompare != 0){
                return timeCompare;
            } else {
                return this.getCourt().compareTo(o.getCourt());
            }

        }
    }

	// sorts by time
	public static Comparator<Match> timeComparator = new Comparator<Match>() {
		public int compare(Match m1, Match m2) {
			return m1.compareDateTimes(m2);
		}
	};
	// sorts by conference
	public static Comparator<Match> conferenceComparator = new Comparator<Match>() {
		public int compare(Match m1, Match m2) {
			int confCompare = m1.getConference().compareTo(m2.getConference());
			if (confCompare != 0)
				return confCompare;
			else
				return m1.compareDateTimes(m2);
		}
	};

	public boolean overlapsWith(MatchSlot other) {
		return matchSlot.getDay().equals(other.getDay()) && matchSlot.getTime().equals(other.getTime());
	}

    public boolean containsTeam(int teamId) {
        return this.team1.getTeamId() == teamId || this.team2.getTeamId() == teamId;
    }

	public String getConference() {
		if (!team1.getConference().equals(team2.getConference())) {
			System.err.println("WARNING: Match " + this + " has teams from different conferences.");
		}
		return team1.getConference();
	}

	public boolean getIsOnWeekend() {
		int weekday = info.getDayOfWeek(this.getMatchSlot().getDay());
		return weekday == DateTimeConstants.SATURDAY || weekday == DateTimeConstants.SUNDAY;
	}

	public static void setInfo(CourtScheduleInfo info) {
		Match.info = info;
	}

	public List<Integer> getPrimaryDays() {
		List<Integer> team1Primary = info.getPrimaryDays().get(team1.getGrade());
		if (team1.getGrade().equals(team2.getGrade())) {
			return team1Primary;
		}
		List<Integer> team2Primary = info.getPrimaryDays().get(team2.getGrade());
		List<Integer> team1Secondary = info.getSecondaryDays().get(team1.getGrade());
		List<Integer> team2Secondary = info.getSecondaryDays().get(team2.getGrade());
		// TODO
		System.out.println(team1.getTeamName() + " and " + team2.getTeamName() + " don't share the same grade; determine shared primary days (defaulting to team 1's)");
		return team1Primary;
	}
}
