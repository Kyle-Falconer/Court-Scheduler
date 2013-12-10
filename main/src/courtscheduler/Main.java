/*
 * Copyright 2013 Michael Adams, CJ Done, Charles Eswine, Kyle Falconer,
 *  Will Gorman, Stephen Kaysen, Pat McCroskey and Matthew Swinney
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package courtscheduler;

import courtscheduler.domain.CourtSchedule;
import courtscheduler.persistence.CourtScheduleIO;
import courtscheduler.persistence.CourtScheduleInfo;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;

import java.awt.*;
import java.io.*;
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

    private static FileOutputStream fileOut = null;
    private static InputStream procErr = null;
    private static InputStream procOut = null;

    public static void main(String[] args) throws Exception {

        if (LOG_LEVEL >= 1) {
            System.out.println("Court Scheduler");
            System.out.println("============================================================");
        }

        String configurationUtilityFilename = getOptArg(args, 1, "configuration"+File.separator+"configSetup.exe");
        if (blockRunProgram(configurationUtilityFilename) != 0){
            System.out.println("[Error] Could not run the configuration utility.");
            //return;
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
        String solverConfigFilename = "/SolverConfig.xml";

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

        String in_filename= info.getInputFileLocation();
        if (in_filename == null){
            in_filename = forceGetArg(args, 0, "Please enter the path of the input file: ");
        }
        System.out.println("Input file location is at: " + in_filename);
        String output_folder = info.getOutputFolderLocation() == null ? parentDirectory(in_filename) : info.getOutputFolderLocation();
        output_folder = parseFolder(output_folder);
        System.out.println("Output folder location is at: " + output_folder);
        String output_filename = output_folder+"output.xlsx";
        System.out.println("Main output file location is at: " + output_filename);

        CourtScheduleIO utils = new CourtScheduleIO(info);
        CourtSchedule testSchedule;
        try{
            testSchedule= new CourtSchedule(utils.readXlsx(in_filename, info), info);
			if (LOG_LEVEL > 1)
				System.out.println(new java.util.Date() + " [INFO] Matches constructed. Sending data to solver engine...");
            // solve the problem (gee, it sounds so easy when you put it like that)
            solver.setPlanningProblem(testSchedule);
            solver.solve();
			CourtSchedule bestSolution = (CourtSchedule)solver.getBestSolution();

            output_filename = utils.writeXlsx(bestSolution.getMatchList(), info, output_filename);
            openFile(output_filename);
        } catch(Exception e){
            e.printStackTrace(); //FIXME
        }


    }

    public static void pipeStream(InputStream input, OutputStream output) throws IOException
    {
        byte buffer[] = new byte[1024];
        int numRead = 0;

        do
        {
            numRead = input.read(buffer);
            if (numRead > 0){
                output.write(buffer, 0, numRead);
            }
        } while (input.available() > 0);

        output.flush();
    }

    private static int blockRunProgram(String filename) {
        if (filename != null) {
            try {
                fileOut = new FileOutputStream("configuration_log.txt");

                String[] cmd = {"cmd", "/c", filename};
                Process p = Runtime.getRuntime().exec(cmd);

                procOut = p.getInputStream();
                procErr = p.getErrorStream();
                pipeStream(procOut, fileOut);
                pipeStream(procErr, fileOut);

                int exitVal = p.waitFor();
                return exitVal;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }


    private static void openFile(String filename){
		if (filename != null) {
        	try {
        	    if (System.getProperty("os.name").contains("Windows")){
        	        Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename);
        	    } else {
        	        Desktop dt = Desktop.getDesktop();
        	        dt.open(new File(filename));
        	    }
        	} catch (Exception e) {
        	    e.printStackTrace();
        	}
		}
    }

    private static XmlSolverFactory loadConfig(String defaultConfigXmlFilename) {
        XmlSolverFactory configured = null;
        String solverConfigFilename = defaultConfigXmlFilename;
        while (configured == null) {
            // http://docs.jboss.org/drools/release/5.5.0.Final/drools-planner-docs/html_single/index.html#d0e1961
            try {
                configured = new XmlSolverFactory(solverConfigFilename);
            } catch (IllegalArgumentException iae) {
                System.out.println("Could not find the solver configuration file: " + solverConfigFilename);
                configured = null;
                solverConfigFilename = promptGetString("Please enter the class path of the solver configuration file: ");
            }
        }
        return configured;
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

    /**
     *
     * @param filename The absolute or relative path of a filename
     * @return The string path representing the parent directory of {@code filename}.
     */
    public static String parentDirectory(String filename){
        File file = new File(filename);
        File parentDir = file.getParentFile();
        String parentDirString = file.getParent();
        if (parentDirString == null || parentDirString.equals(".")){
            return "."+File.separator;
        }
        parentDirString = parentDirString.replace("\\", File.separator);
        parentDirString = parentDirString.replace("/", File.separator);
        return parentDirString;
    }

    public static String parseFolder(String folder) {

        String result = folder.replace("\\", File.separator);
        result = folder.replace("/", File.separator);
        String lastCharacter = result.substring(folder.length()-1);
        if (lastCharacter.equals(File.separator)){
            return result;
        }
        return result+File.separator;

    }

}
