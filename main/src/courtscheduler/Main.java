package courtscheduler;

import courtscheduler.persistence.Reader;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 9/21/13
 * Time: 9:35 PM
 */
public class Main {

    protected static int LOG = 2;

    public static void main(String[] args){

        new Reader();

        if (LOG >= 1) {
            System.out.println("Court Scheduler");
            System.out.println("===============");
        }

        // http://docs.jboss.org/drools/release/5.5.0.Final/drools-planner-docs/html_single/index.html#d0e1961
        /*
        XmlSolverFactory solverFactory = new XmlSolverFactory();
        solverFactory.configure("/courtscheduler/solver/SolverConfig.xml");
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        solverConfig.getTerminationConfig().setMaximumMinutesSpend(1000L);  // FIXME: get this from the config file
        Solver solver = solverConfig.buildSolver();

        if (LOG >= 2 ){
            System.out.println("\n\nconfiguration loaded...");
        }
        */

    }

}
