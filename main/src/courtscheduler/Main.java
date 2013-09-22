package courtscheduler;

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
    public static void main(String[] args){

        System.out.println("Court Scheduler");
        System.out.println("===============");

        // http://docs.jboss.org/drools/release/5.5.0.Final/drools-planner-docs/html_single/index.html#d0e1961
        XmlSolverFactory solverFactory = new XmlSolverFactory();
        solverFactory.configure("SolverConfig.xml");
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        solverConfig.getTerminationConfig().setMaximumMinutesSpend(1000L);  // FIXME: get this from the config file
        Solver solver = solverConfig.buildSolver();

        System.out.println("\n\nconfiguration loaded...");
    }

}
