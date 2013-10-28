package courtscheduler.domain;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

import java.io.*;
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
    private CourtScheduleInfo courtScheduleInfo;

    private List<Team> teamList;  // Value Range Providers
    private List<MatchTime> matchTimeList;
    private List<MatchDate> matchDateList;

    private List<Conference> conferenceList;
    //private List<Gender> genderList;
    //private List<Grade> gradeList;
    //private List<Level> levelList;
    //private List<Requests> requestsList;

    // configurables
    private LocalDate conferenceStartDate;
    private LocalDate conferenceEndDate;
    private int numberOfCourts;
    private int timeslotMidnightOffsetInMinutes;
    private int numberOfTimeSlotsPerDay;
    private int timeslotDurationInMinutes;

    public List<Match> matchAssignmentList;

    private Match[][][] schedule;
    private List<Match> matchList;
    private List<Integer> dayList;
    private List<Integer> timeList;
    private List<Integer> courtList;
    private Calendar firstDay;

    public CourtSchedule(){

    }

    public CourtSchedule(List<Team> teamList){

        // FIXME -- these should be configurable!
        conferenceStartDate = new LocalDate( 2014, 01, 1);
        conferenceEndDate = new LocalDate( 2014, 06, 1);
        numberOfCourts = 3;
        timeslotMidnightOffsetInMinutes = 420;  // 7am
        numberOfTimeSlotsPerDay = 16;  // end at ~8:30pm
        timeslotDurationInMinutes = 50;

        schedule = new Match[getNumberOfConferenceDays()][numberOfTimeSlotsPerDay][numberOfCourts];


        this.teamList = teamList;
        //time
        MatchTime[] time = new MatchTime[24];
        for(int i=0;i<24;i++){
            MatchTime hourly= new MatchTime();
            hourly.setStartTime(i+":00");
            hourly.setEndTime(i+":50");
            time[i]=hourly;
        }
        setMatchTimeList(Arrays.asList(time));

        //date
        MatchDate[] date = new MatchDate[31];
        for(int i=0;i<31;i++){
            MatchDate day= new MatchDate();
            Calendar cal = Calendar.getInstance();
            cal.set(2013, 9, i);
            day.setCal(cal);
            switch(i%7){
                case(0):
                    day.setDayOfWeek(DayOfWeek.MONDAY);
                    break;
                case(1):
                    day.setDayOfWeek(DayOfWeek.TUESDAY);
                    break;
                case(2):
                    day.setDayOfWeek(DayOfWeek.WEDNESDAY);
                    break;
                case(3):
                    day.setDayOfWeek(DayOfWeek.THURSDAY);
                    break;
                case(4):
                    day.setDayOfWeek(DayOfWeek.FRIDAY);
                    break;
                case(5):
                    day.setDayOfWeek(DayOfWeek.SATURDAY);
                    break;
                case(6):
                    day.setDayOfWeek(DayOfWeek.SUNDAY);
            }
            date[i]=day;
        }
        setMatchDateList(Arrays.asList(date));

        //Match
        List<Match> matches= new ArrayList<Match>();
        for(int i=0;i<teamList.size();i++){
            for(int j=0; j<teamList.size();j++){
				if (Team.canPlay(teamList.get(i), teamList.get(j))) {
                    Match nextMatch = new Match(teamList.get(i),teamList.get(j));
                    nextMatch.setMatchSlot(new MatchSlot(-1, -1, -1));
                	matches.add(nextMatch);
				}
            }
        }
        setMatchList(matches);

        //conference
        List<Conference> conferences= new ArrayList<Conference>();
        for(int i=0;i<teamList.size();i++){
            for(int j=0;j<4;j++){
                Conference conference = new Conference();
                conference.setConference(j);
                conference.setTeam(teamList.get(i));
                conferences.add(conference);
            }
        }
        setConferenceList(conferences);
    }

    public void setCourtScheduleInfo(CourtScheduleInfo courtScheduleInfo) {
        this.courtScheduleInfo = courtScheduleInfo;
    }

    public CourtScheduleInfo getCourtScheduleInfo() {
        return courtScheduleInfo;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    @ValueRangeProvider(id = "teamRange")
    public List<Team> getTeamList() {
        return teamList;
    }

    public void setMatchTimeList(List<MatchTime> matchTimeList) {
        this.matchTimeList = matchTimeList;
    }

    public List<MatchTime> getMatchTimeList() {
        return matchTimeList;
    }

    public void setMatchDateList(List<MatchDate> matchDateList) {
        this.matchDateList = matchDateList;
    }

    public List<MatchDate> getMatchDateList() {
        return matchDateList;
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

    public void setConferenceList(List<Conference> conferenceList) {
        this.conferenceList = conferenceList;
    }

    public List<Conference> getConferenceList() {
        return conferenceList;
    }

    public int getNumberOfConferenceDays(){
        return Days.daysBetween(conferenceStartDate, conferenceStartDate).getDays();
    }
    public List<Integer> getDayList(){
        return this.dayList;
    }
    public List<Integer> getTimeList(){
        return this.timeList;
    }
    public List<Integer> getCourtList(){
        return this.courtList;
    }
    public void setDayList(List<Integer> dayList){
        this.dayList=dayList;
    }
    public void setTimeList(List<Integer> timeList){
        this.timeList=timeList;
    }
    public void setCourtList(List<Integer> courtList){
        this.courtList=courtList;
    }

    public void setFirstDay(Calendar firstDay){
        this.firstDay=firstDay;
    }
    public Calendar getFirstDay(){
        return this.firstDay;
    }


    /*public void setGenderList(List<Gender> genderList) {
        this.genderList = genderList;
    }

    public List<Gender> getGenderList() {
        return genderList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setLevelList(List<Level> levelList) {
        this.levelList = levelList;
    }

    public List<Level> getLevelList() {
        return levelList;
    }*/

    public void setMatchAssignmentList(List<Match> matchList) {
        this.matchAssignmentList = matchAssignmentList;
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

    public void writeXlsx(String filepath) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Conference 1");
        //Create a new row in current sheet
        int rowNumber = 0;
        int cellNumber = 0;
        Row header = sheet.createRow(rowNumber);
        //cell.setCellValue("CourtScheduler");
        header.createCell(0).setCellValue("Team 1");
        header.createCell(1).setCellValue("Team 2");
        header.createCell(2).setCellValue("Court");
        header.createCell(3).setCellValue("Time");
        header.createCell(4).setCellValue("Date");


        for(Match match : matchList) {
            cellNumber = 0;
			rowNumber++;
            Row dataRow = sheet.createRow(rowNumber);
            String teamName1 = match.getT1().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName1);
            String teamName2 = match.getT2().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName2);
            Integer courtId = match.getMatchSlot().getCourt();
            dataRow.createCell(cellNumber++).setCellValue(courtId);
            Integer matchTime = match.getMatchSlot().getTime();
            dataRow.createCell(cellNumber++).setCellValue(matchTime);
            Integer matchDate = match.getMatchSlot().getDay();
            dataRow.createCell(cellNumber++).setCellValue(matchDate);
        }

        try {
            FileOutputStream out =
                    new FileOutputStream(new File(filepath));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully to " + filepath + ".");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void generatePlaceholderMatches(Team[] teams) {
        // TODO: optimize
		for (Team t1 : teams) {
			for (Team t2 : teams) {
				if (t1.getTeamId() < t2.getTeamId()) {
					Match next = new Match(t1, t2);


                    // TODO: verify conference is ok, that they can play each other.
					matchAssignmentList.add(next);
				}
			}
		}
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
        facts.add(courtScheduleInfo);
        facts.addAll(teamList);
        facts.addAll(matchList);
        facts.addAll(matchDateList);
        facts.addAll(matchTimeList);
        facts.addAll(conferenceList);
        //facts.addAll(genderList);
        //facts.addAll(gradeList);
        //facts.addAll(levelList);
        // Do not add the planning entity's (matchAssignmentList) because that will be done automatically
        return facts;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @ValueRangeProvider(id = "matchSlot")
    public List<MatchSlot> getMatchSlots() {
        return new ArrayList<MatchSlot>();
    }
}
