package courtscheduler.domain;


import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
// import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
//...

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningSolution
public class CourtSchedule extends AbstractPersistable implements Solution<HardSoftScore> {

    private HardSoftScore score;
    private CourtScheduleInfo courtScheduleInfo;

    private List<Team> teamList;  // Value Range Providers
    private List<MatchTime> matchTimeList;
    private List<MatchDate> matchDateList;
    private List<Match> matchList;
    private List<Conference> conferenceList;
    //private List<Gender> genderList;
    //private List<Grade> gradeList;
    //private List<Level> levelList;
    //private List<Requests> requestsList;

    public List<MatchAssignment> matchAssignmentList;


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

    public void setConferenceList(List<Conference> conferenceList) {
        this.conferenceList = conferenceList;
    }

    public List<Conference> getConferenceList() {
        return conferenceList;
    }
    /*       Will and Kyle : These shouldnt be their own classes and should be pulled from teamList
    public void setGenderList(List<Gender> genderList) {
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
    }
      */

    public void setMatchAssignmentList(List<MatchAssignment> matchAssignmentList) {
        this.matchAssignmentList = matchAssignmentList;
    }

    @PlanningEntityCollectionProperty
    public List<MatchAssignment> getMatchAssignments(){
        return matchAssignmentList;
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

    public void writeXlsx() {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Conference 1");
        //Create a new row in current sheet
        int rowNumber = 0;
        int cellNumber = 0;
        Row header = sheet.createRow(rowNumber);
        cell.setCellValue("CourtScheduler");
        header.createCell(0).setCellValue("Team 1");
        header.createCell(1).setCellValue("Team 2");
        header.createCell(2).setCellValue("Court");
        header.createCell(3).setCellValue("Time");
        header.createCell(4).setCellValue("Date");


        for(MatchAssignment matchAssignment : matchAssignmentList) {
            cellNumber = 0;
            Row dataRow = sheet.createRow(rowNumber++);
            String teamName1 = matchAssignment.getTeamId().getTeamName();
            dataRow.createCell(cellNumber).setCellValue(teamName1);
            String teamName2 = matchAssignment.getTeamId().getTeamName();
            dataRow.createCell(cellNumber++).setCellValue(teamName2);
            String courtId = matchAssignment.getCourtId().toString();
            dataRow.createCell(cellNumber++).setCellValue(courtId);
            String matchTime = matchAssignment.getMatch().getMatchTime().getStartTime();
            dataRow.createCell(cellNumber++).setCellValue(matchTime);
            String matchDate = matchAssignment.getMatch().getMatchDate().getDate();
            dataRow.createCell(cellNumber++).setCellValue(matchDate);
        }

        try {
            FileOutputStream out =
                    new FileOutputStream(new File("C:\\CourtScheduler.xls"));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
      //  facts.addAll(genderList);
      //  facts.addAll(gradeList);
      //  facts.addAll(levelList);
        // Do not add the planning entity's (matchAssignmentList) because that will be done automatically
        return facts;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
