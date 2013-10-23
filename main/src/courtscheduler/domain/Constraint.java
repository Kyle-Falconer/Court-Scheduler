package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Constraint {

    private Integer weight;
    private boolean hard;

    public boolean isHard() {
        return hard;
    }

    public void setHard(boolean hard) {
        this.hard = hard;
    }

    public void toggleHard(){
        this.hard=!this.hard;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
