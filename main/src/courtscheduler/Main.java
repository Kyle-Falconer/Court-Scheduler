package courtscheduler;

//import courtscheduler.persistence.XlsxReader;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import org.optaplanner.core.api.solver.Solver;


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

        //new Reader("main/src/courtscheduler/persistence/teamlist.csv");
        // new Reader(args[0]);

  //      new XlsxReader(args[0]);



        // http://docs.jboss.org/drools/release/5.5.0.Final/drools-planner-docs/html_single/index.html#d0e1961
        XmlSolverFactory solverFactory = new XmlSolverFactory("/courtscheduler/solver/SolverConfig.xml");
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        Solver solver = solverConfig.buildSolver();

        if (LOG >= 2 ){
            System.out.println("\n\nconfiguration loaded...");
        }


    }

}
