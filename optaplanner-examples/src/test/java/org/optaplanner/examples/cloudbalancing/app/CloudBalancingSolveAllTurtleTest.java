/*
 * Copyright 2013 JBoss Inc
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

package org.optaplanner.examples.cloudbalancing.app;

import java.io.File;
import java.util.Collection;

import org.junit.runners.Parameterized;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.examples.cloudbalancing.persistence.CloudBalancingDao;
import org.optaplanner.examples.cloudbalancing.solver.score.CloudBalancingMapBasedSimpleScoreCalculator;
import org.optaplanner.examples.common.app.SolveAllTurtleTest;
import org.optaplanner.examples.common.persistence.SolutionDao;

public class CloudBalancingSolveAllTurtleTest extends SolveAllTurtleTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> getSolutionFilesAsParameters() {
        return getUnsolvedDataFilesAsParameters(new CloudBalancingDao());
    }

    public CloudBalancingSolveAllTurtleTest(File unsolvedDataFile) {
        super(unsolvedDataFile);
    }

    @Override
    protected String createSolverConfigResource() {
        return "/org/optaplanner/examples/cloudbalancing/solver/cloudBalancingSolverConfig.xml";
    }

    @Override
    protected ScoreDirectorFactoryConfig createOverwritingAssertionScoreDirectorFactory() {
        ScoreDirectorFactoryConfig assertionScoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        assertionScoreDirectorFactoryConfig.setSimpleScoreCalculatorClass(
                CloudBalancingMapBasedSimpleScoreCalculator.class);
        return assertionScoreDirectorFactoryConfig;
    }

    @Override
    protected SolutionDao createSolutionDao() {
        return new CloudBalancingDao();
    }

}
