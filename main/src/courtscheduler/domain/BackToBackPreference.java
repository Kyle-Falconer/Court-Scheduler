package courtscheduler.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 10/14/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackToBackPreference extends Constraint{

    boolean likesBackToBack = false;

	public BackToBackPreference(boolean likesBackToBack) {
		this.likesBackToBack = likesBackToBack;
	}
    public void toggleBackToBack(){
        this.likesBackToBack=!this.likesBackToBack;
    }
    public boolean isLikesBackToBack() {
        return likesBackToBack;
    }


}
