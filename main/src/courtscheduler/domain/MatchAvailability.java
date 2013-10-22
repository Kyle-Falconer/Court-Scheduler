package courtscheduler.domain;

/**
 * "I can play on these timeslots and these days"
 * User: Kyle
 * Date: 10/21/13
 * Time: 9:21 PM
 */
public class MatchAvailability {

    Constraint[] constraints;

    public MatchAvailability(){

    }

    public MatchAvailability(MatchAvailability m1, MatchAvailability m2){
        // TODO: set the intersection of these two, m1 and m2 to be this MatchAvailability
    }

}
