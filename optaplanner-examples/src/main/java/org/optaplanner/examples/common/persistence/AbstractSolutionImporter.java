/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.common.persistence;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.common.app.LoggingMain;
import org.optaplanner.examples.common.business.ProblemFileComparator;
import org.optaplanner.examples.common.business.SolutionFileFilter;

public abstract class AbstractSolutionImporter extends LoggingMain {

    protected static final String DEFAULT_OUTPUT_FILE_SUFFIX = "xml";

    protected SolutionDao solutionDao;

    public AbstractSolutionImporter(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    protected File getInputDir() {
        return new File(solutionDao.getDataDir(), "import");
    }

    public abstract String getInputFileSuffix();

    protected File getOutputDir() {
        return new File(solutionDao.getDataDir(), "unsolved");
    }

    protected String getOutputFileSuffix() {
        return DEFAULT_OUTPUT_FILE_SUFFIX;
    }

    public void convertAll() {
        File inputDir = getInputDir();
        if (!inputDir.exists()) {
            throw new IllegalStateException("The directory inputDir (" + inputDir.getAbsolutePath()
                    + ") does not exist.");
        }
        File outputDir = getOutputDir();
        File[] inputFiles = inputDir.listFiles();
        Arrays.sort(inputFiles, new ProblemFileComparator());
        for (File inputFile : inputFiles) {
            if (acceptInputFile(inputFile) && acceptInputFileDuringBulkConvert(inputFile)) {
                Solution solution = readSolution(inputFile);
                String inputFileName = inputFile.getName();
                String outputFileName = inputFileName.substring(0,
                        inputFileName.length() - getInputFileSuffix().length())
                        + getOutputFileSuffix();
                File outputFile = new File(outputDir, outputFileName);
                solutionDao.writeSolution(solution, outputFile);
            }
        }
    }

    public boolean acceptInputFile(File inputFile) {
        return inputFile.getName().endsWith("." + getInputFileSuffix());
    }

    /**
     * Some files are to big to be serialized to XML or take too long.
     * @param inputFile never null
     * @return true if accepted
     */
    public boolean acceptInputFileDuringBulkConvert(File inputFile) {
        return true;
    }

    public abstract Solution readSolution(File inputFile);

}
