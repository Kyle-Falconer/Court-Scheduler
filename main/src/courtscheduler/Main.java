package courtscheduler;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.domain.CourtScheduleInfo;
import courtscheduler.persistence.CourtScheduleIO;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;


/**
 * AKA: Project "Matchsticks"
 * Date: 9/21/13
 * Time: 9:35 PM
 */
public class Main {

    public static int LOG_LEVEL = 1;

    public static void main(String[] args) throws Exception {


        if (LOG_LEVEL >= 1) {
            System.out.println("Court Scheduler");
            System.out.println("============================================================");
        }

        if (LOG_LEVEL >= 3) {
            System.out.println("Current working directory = " + System.getProperty("user.dir"));
            ClassLoader cl = ClassLoader.getSystemClassLoader();

            System.out.println("Current classpaths: ");
                    URL[] urls = ((URLClassLoader) cl).getURLs();
            for (URL url : urls) {
                System.out.println(url.getFile());
            }
        }

        // This filename needs to be relative to the application's classpath
        String solverConfigFilename = getOptArg(args, 2, "/courtscheduler/solver/SolverConfig.xml");

		// initialize CourtSchedule configuration
		CourtScheduleInfo info = new CourtScheduleInfo("config.ini");
        if(info.configure() == -1){
           return;
        }
        if (LOG_LEVEL >= 2){
            System.out.println(info.toString());
        }


        // initialize solver
        XmlSolverFactory solverFactory = loadConfig(solverConfigFilename);

        SolverConfig solverConfig = solverFactory.getSolverConfig();
        Solver solver = solverConfig.buildSolver();

        if (LOG_LEVEL >= 2) {
            System.out.println("\n\nconfiguration loaded...");
        }

        String in_filename = forceGetArg(args, 0, "Please enter the path of the input file: ");
        String out_filename = getOptArg(args, 1, "output.xlsx");


        CourtScheduleIO utils = new CourtScheduleIO();
        CourtSchedule testSchedule;
        try{
            testSchedule= new CourtSchedule(utils.readXlsx(in_filename), info);

            // solve the problem (gee, it sounds so easy when you put it like that)
            solver.setPlanningProblem(testSchedule);
            solver.solve();
			CourtSchedule bestSolution = (CourtSchedule)solver.getBestSolution();

            utils.writeXlsx(bestSolution.getMatchList(), info, out_filename);
        } catch(Exception e){
            e.printStackTrace(); //FIXME
        }

    }

    private static XmlSolverFactory loadConfig(String defaultConfigXmlFilename) {
        XmlSolverFactory configed = null;
        String solverConfigFilename = defaultConfigXmlFilename;
        while (configed == null) {
            // http://docs.jboss.org/drools/release/5.5.0.Final/drools-planner-docs/html_single/index.html#d0e1961
            try {
                configed = new XmlSolverFactory(solverConfigFilename);
            } catch (IllegalArgumentException iae) {
                System.out.println("Could not find the solver configuration file: " + solverConfigFilename);
                configed = null;
                solverConfigFilename = promptGetString("Please enter the class path of the solver configuration file: ");
            }
        }
        return configed;
    }

    private static String promptGetString(String prompt) {
        String result = null;
        while (result == null) {
            System.out.print(prompt);
            Scanner s = new Scanner(System.in);
            result = s.next();
            if (result.equals("")) {
                result = null;
            }
        }
        return result;
    }

    private static String getOptArg(String[] args, int argIndex, String defaultValue) {
        String result = defaultValue;
        if (args.length >= argIndex + 1 && args[argIndex].length() > 1) {
            result = args[argIndex];
            // TODO: check for file extension and add the .xlsx extension if needed
        }
        return result;
    }

    private static String forceGetArg(String[] args, int argIndex, String prompt) {
        String result;
        if (args.length < argIndex + 1) {
            result = promptGetString(prompt);
        } else {
            result = args[argIndex];
        }
        return result;
    }

}
