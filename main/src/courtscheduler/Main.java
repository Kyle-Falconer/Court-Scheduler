package courtscheduler;

//import courtscheduler.persistence.XlsxReader;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.persistence.XlsxReader;
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

    public static void main(String[] args) throws Exception{

        if (LOG >= 1) {
            System.out.println("Court Scheduler");
            System.out.println("============================================================");
        }

		// initialize solver
        // http://docs.jboss.org/drools/release/5.5.0.Final/drools-planner-docs/html_single/index.html#d0e1961
        XmlSolverFactory solverFactory = new XmlSolverFactory("/courtscheduler/solver/SolverConfig.xml");
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        Solver solver = solverConfig.buildSolver();

        if (LOG >= 2 ){
            System.out.println("\n\nconfiguration loaded...");
        }
        CourtSchedule testSchedule = XlsxReader.readExcelFile("Book1.xlsx");

		// solve the problem (gee, it sounds so easy when you put it like that)
		//solver.setPlanningProblem(testSchedule);
		// solver.solve();
		// Solution bestSolution = solver.getBestSolution();
		testSchedule.generatePlaceholderMatches();
		testSchedule.writeXlsx("output.xlsx");

		// output best solution
		// TODO
    }
}
