package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: CJ
 * Date: 10/28/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchSlot extends AbstractPersistable {
    private Integer day;
    private Integer time;
    private Integer court;

    public MatchSlot(int day, int time, int court) {
        this.day = day;
        this.time = time;
        this.court = court;
    }

    public Integer getDay(){
        return this.day;
    }
    public Integer getTime(){
        return this.time;
    }
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

    public String toString() {
        return "(" + day + ":" + time + ":" + court + ")";
    }
}
