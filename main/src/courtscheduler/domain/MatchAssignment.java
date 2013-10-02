package courtscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/21/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@PlanningEntity
public class MatchAssignment extends AbstractPersistable {

    @PlanningVariable(valueRangeProviderRefs = {"planningVariable" })
    public Object getPlanningVariable(){
        // FIXME
        return null;
    }

    public void setPlanningVariable(Object variable){
        // FIXME
    }

}
