package courtscheduler.domain;


import courtscheduler.persistence.CourtScheduleInfo;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import java.io.Serializable;
import java.util.*;

// import org.apache.poi.hssf.usermodel.HSSFSheet;
//...

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningSolution
public class CourtSchedule extends AbstractPersistable implements Solution<HardSoftScore>, Serializable {

    public static int NUMBER_OF_COURTS;

    private HardSoftScore score;

    private List<Team> teamList;
	private List<Match> matchList;
	private List<MatchSlot> matchSlots;

    // configurables
    private CourtScheduleInfo info;

    public CourtSchedule(){

    }

    public CourtSchedule(List<Team> teamList, CourtScheduleInfo info){
        this.info = info;

        this.teamList = teamList;


        //Round-Robin construction of initial match list.
        matchList = roundRobin(teamList);

        // generate the match slots if needed
        getMatchSlots();
        // make the preliminary schedule
        setPreliminarySchedule();

    }

    private List<Match> roundRobin(List<Team> teamList) {
        List<Match> matches= new ArrayList<Match>();
        for(int i=0;i<teamList.size();i++){
            for(int j=i+1; j<teamList.size();j++){
                if (!Team.cannotPlay(teamList.get(i), teamList.get(j))) {
                    Match nextMatch = new Match(teamList.get(i),teamList.get(j));
                    nextMatch.setMatchSlot(new MatchSlot(-1, -1, -1));
                    matches.add(nextMatch);
                }
            }
        }
        return matches;
    }

    private void setPreliminarySchedule() {

        // generate the match slots if needed
        if (this.matchSlots == null){
            getMatchSlots();
        }

        // in each match in the matchlist assign a matchslot
        // if one of the teams in Match is already playing on day n, increment n until a day is found
        // if a timeslot is not found (already playing on every day) then increment the timeslot by 2 and set n to 0

        for (int matchIndex = 0; matchIndex < this.matchList.size(); matchIndex++) {
            int dayIndex = 0;
            for (int matchslotIndex = 0; matchslotIndex < this.matchSlots.size(); matchslotIndex++) {
                if (this.matchSlots.get(matchslotIndex).getDay() == dayIndex) {
                    if (!eitherTeamIsPlayingOnDay(this.matchList.get(matchIndex), dayIndex)) {
                        this.matchList.get(matchIndex).setMatchSlot(this.matchSlots.get(matchslotIndex));
                        break;
                    }
                    dayIndex++;
                }
            }

        }
        System.out.println("built preliminary schedule");

    }

    private boolean eitherTeamIsPlayingOnDay(Match match, int dayIndex){
        for (Match m : matchList) {
            if (m.containsTeamsFrom(match) && m.getMatchSlot().getDay() == dayIndex) {
                return true;
            }
        }
        return false;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    @ValueRangeProvider(id = "teamRange")
    public List<Team> getTeamList() {
        return teamList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
    public List<Match> getMatchList() {
        return matchList;
    }
    public Match getNextMatch(){
        return matchList.remove(0);
    }

    @PlanningEntityCollectionProperty
    public List<Match> getMatches(){
        return matchList;
    }
    /**
     * Returns the Score of this Solution.
     *
     * @return null if the Solution is uninitialized
     *         or the last calculated Score is dirty the new Score has not yet been recalculated
     */
    @Override
    public HardSoftScore getScore() {
        return score;
    }

    /**
     * Called by the {@link org.optaplanner.core.api.solver.Solver} when the Score of this Solution has been calculated.
     *
     * @param score null if the Solution has changed and the new Score has not yet been recalculated
     */
    @Override
    public void setScore(HardSoftScore score) {
        this.score = score;
    }


    /**
     * Called by the {@link org.optaplanner.core.impl.score.director.drools.DroolsScoreDirector} when the {@link org.optaplanner.core.impl.solution.Solution} needs to be inserted
     * into an empty {@link org.kie.api.runtime.KieSession}.
     * These facts can be used by the score rules.
     * They don't change during planning (except through {@link org.optaplanner.core.impl.solver.ProblemFactChange} events).
     * <p/>
     * Do not include the planning entities as problem facts:
     * they are automatically inserted into the {@link org.kie.api.runtime.KieSession} if and only if they are initialized.
     * When they are initialized later, they are also automatically inserted.
     *
     * @return never null (although an empty collection is allowed),
     *         all the facts of this solution except for the planning entities
     */
    @Override
    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.add(info);
        facts.addAll(teamList);
        facts.addAll(matchList);
        // Do not add the planning entity's (matchAssignmentList) because that will be done automatically
        return facts;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @ValueRangeProvider(id = "matchSlot")
    public List<MatchSlot> getMatchSlots() {
        // FIXME: pass every available match slot so that the list of moves can be used against this list of match slots
        if (matchSlots == null || matchSlots.size() == 0){
            matchSlots = new ArrayList<MatchSlot>();
            for (int dayIndex = 0; dayIndex < info.getNumberOfConferenceDays(); dayIndex++)  {
                for (int slotIndex = 0; slotIndex < info.getNumberOfTimeSlotsPerDay(); slotIndex++)  {
                    for (int courtIndex = 0; courtIndex < info.getNumberOfCourts(); courtIndex++)  {
                        matchSlots.add(new MatchSlot(dayIndex, slotIndex, courtIndex));
                    }
                }
            }
        }
        return matchSlots;
    }
}
