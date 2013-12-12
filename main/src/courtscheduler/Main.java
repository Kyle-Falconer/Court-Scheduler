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
import courtscheduler.domain.Team;
import courtscheduler.persistence.CourtScheduleIO;
import courtscheduler.persistence.CourtScheduleInfo;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


/**
 * AKA: Project "Matchsticks"
 * Date: 9/21/13
 * Time: 9:35 PM
 */
public class Main {

    public static int LOG_LEVEL = 1;

    private static String main_log_filename = "cs_log.txt";
    private static StringBuilder log_strings;

    private static FileOutputStream configuration_log_out = null;
    private static InputStream procErr = null;
    private static InputStream procOut = null;

    public static void main(String[] args) throws Exception {

        System.out.println("Court Scheduler");
        System.out.println("================================================================================");
        log_strings = new StringBuilder();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                writeToFile(main_log_filename, log_strings.toString());
            }
        });

        runConfigurationUtility(getOptArg(args, 1, "configuration"+File.separator+"configSetup.exe"));


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
        while (in_filename == null){
            in_filename = forceGetArg(args, 0, "Please enter the path of the input file: ");
            File file = new File(in_filename);
            if (!file.exists()){
                in_filename = null;
            }
        }

        System.out.println("Input file location is at: \"" + in_filename+"\"");
        String output_folder = info.getOutputFolderLocation() == null ? parentDirectory(in_filename) : info.getOutputFolderLocation();
        output_folder = parseFolder(output_folder);
        System.out.println("Output folder location is at: \"" + output_folder +"\"");
        String output_filename = output_folder+"output.xlsx";
        System.out.println("Main output file location is at: \"" + output_filename+"\"");

        CourtScheduleIO utils = new CourtScheduleIO(info);
        CourtSchedule testSchedule;
        try{
            java.util.List<Team> input = utils.readXlsx(in_filename, info);
            if (input == null){
                error("Expected to use the file with the following path as input, but it could not be found:\n\t" + in_filename);
            }

            testSchedule= new CourtSchedule(input, info);
			if (LOG_LEVEL > 1)
				System.out.println(new java.util.Date() + " [INFO] Matches constructed. Sending data to solver engine...");
            // solve the problem (gee, it sounds so easy when you put it like that)
            solver.setPlanningProblem(testSchedule);
            solver.solve();
			CourtSchedule bestSolution = (CourtSchedule)solver.getBestSolution();
            log_strings.append("Best score: "+solver.getBestSolution().getScore());

            output_filename = utils.writeXlsx(bestSolution.getMatchList(), info, output_filename);
            openFile(output_filename);
        } catch(Exception e){
            error(true, "Fatal error", e.toString());
        }

    }

    public static void writeToFile(String filename, String contents){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.println(contents);
        } catch (FileNotFoundException e) {
            error(false, "When trying to write the log to a file, the file \""+filename+"\" could not be found", e.toString());
        } catch (UnsupportedEncodingException e) {
            error(false, "Encoding exception when trying to write the log to a file.", e.toString());
        }finally {
            writer.close();
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

    private static void runConfigurationUtility(String filename){
        File configFile = new File(filename);
        if (!configFile.exists()){
            error("Expected the configuration utility to be found at the following location:\n" + configFile.getAbsolutePath());
        }
        if (blockRunProgram(filename) != 0){
            error("Could not run the configuration utility.");
        }
    }

    private static int blockRunProgram(String filename) {
        if (filename != null) {
            try {
                configuration_log_out = new FileOutputStream("configuration_log.txt");

                String[] cmd = {"cmd", "/c", filename};
                Process p = Runtime.getRuntime().exec(cmd);

                procOut = p.getInputStream();
                procErr = p.getErrorStream();
                pipeStream(procOut, configuration_log_out);
                pipeStream(procErr, configuration_log_out);

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

    public static void error(String message){
        error(false, message, null);
    }

    public static void error(String message, String stacktrace){
        error(false, message, stacktrace);
    }

    public static void error(boolean fatal, String message, String stacktrace){
        StringBuilder full_message = new StringBuilder();
        full_message.append("\n================================================================================\n");
        full_message.append("ERROR:\n");
        if (stacktrace != null && Main.LOG_LEVEL >= 2){
            full_message.append("MESSAGE:\n"+message+"\n\nSTACKTRACE:\n"+stacktrace+"\n");
        } else {
            full_message.append(message + "\n");
        }
        full_message.append("\n================================================================================\n");
        if (fatal){
            full_message.append("\nThe program will now quit.\n");
            log_strings.append(full_message.toString());
            System.out.print(full_message.toString());
            Scanner s = new Scanner(System.in);
            s.nextLine();
            writeToFile(main_log_filename, log_strings.toString());
            System.exit(1);
        }
        log_strings.append(full_message.toString());
        System.out.print(full_message.toString());
    }

    public static void warning(String message){
        warning(message, null);
    }

    public static void warning(String message, String stacktrace){
        StringBuilder full_message = new StringBuilder();
        full_message.append("WARNING: " + message);

        if (stacktrace != null && Main.LOG_LEVEL >= 2){
            full_message.append("\nSTACKTRACE:\n"+stacktrace);
        }
        full_message.append("\n");
        log_strings.append(full_message.toString());
        System.out.print(full_message.toString());
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
