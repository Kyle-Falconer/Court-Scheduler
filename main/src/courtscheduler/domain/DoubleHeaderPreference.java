package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleHeaderPreference extends Constraint {

    boolean likesDoubleHeaders = false;

	public DoubleHeaderPreference(boolean likesDoubleHeaders) {
		this.likesDoubleHeaders = likesDoubleHeaders;
	}
    public void toggleDoubleHeader(){
        this.likesDoubleHeaders=!this.likesDoubleHeaders;
    }

    public boolean likesDoubleHeaders() {
        return likesDoubleHeaders;
    }
}
