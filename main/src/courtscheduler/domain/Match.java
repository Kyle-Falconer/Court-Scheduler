package courtscheduler.domain;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.nurserostering.domain.DayOfWeek;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 9/30/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningEntity
public class Match {

    private Team t1;
    private Team t2;
    MatchAvailability avail;   // calculated based on the intersection of the teams' availabilities
    private Integer day;
    private Integer time;
    private Integer court;


    public Match(Team t1, Team t2){
        this.t1 = t1;
        this.t2 = t2;
        avail = new MatchAvailability(t1.getAvailability(), t2.getAvailability());
    }
    @PlanningVariable(valueRangeProviderRefs = {"dayRange"})
    public Integer getDay(){
        return this.day;
    }
    @PlanningVariable(valueRangeProviderRefs = {"timeRange"})
    public Integer getTime(){
        return this.time;
    }
    @PlanningVariable(valueRangeProviderRefs = {"courtRange"})
    public Integer getCourt(){
        return this.court;
    }
    public void setDay(Integer day){
        this.day=day;
    }
    public void setTime(Integer time){
        this.time=time;
    }
    public void setCourt(Integer court){
        this.court=court;
    }
    public Team getT1(){
        return this.t1;
    }
    public Team getT2(){
        return this.t2;
    }
    public MatchDate getMatchDate(Calendar dateScale){
        MatchDate date = new MatchDate();
        dateScale.add(Calendar.DATE,day);
        date.setCal(dateScale);
        date.setDayOfWeek(DayOfWeek.valueOfCalendar(dateScale.get(Calendar.DAY_OF_WEEK)));
        return date;
    }
}
