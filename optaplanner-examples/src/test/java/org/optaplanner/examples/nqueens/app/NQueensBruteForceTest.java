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

package org.optaplanner.examples.nqueens.app;

import java.io.File;
import java.util.Collections;

import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.bruteforce.BruteForceSolverPhaseConfig;
import org.optaplanner.core.config.phase.SolverPhaseConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.examples.common.app.SolverPerformanceTest;
import org.optaplanner.examples.common.persistence.SolutionDao;
import org.optaplanner.examples.nqueens.persistence.NQueensDao;

public class NQueensBruteForceTest extends SolverPerformanceTest {

    @Override
    protected String createSolverConfigResource() {
        return "/org/optaplanner/examples/nqueens/solver/nqueensSolverConfig.xml";
    }

    @Override
    protected SolutionDao createSolutionDao() {
        return new NQueensDao();
    }

    @Override
    protected SolverFactory buildSolverFactory(String scoreAttainedString, EnvironmentMode environmentMode) {
        SolverFactory solverFactory = super.buildSolverFactory(scoreAttainedString, environmentMode);
        solverFactory.getSolverConfig().setSolverPhaseConfigList(
                Collections.<SolverPhaseConfig>singletonList(new BruteForceSolverPhaseConfig())
        );
        return solverFactory;
    }

    // ************************************************************************
    // Tests
    // ************************************************************************

    @Test(timeout = 600000)
    public void solveModel_4queens() {
        runSpeedTest(new File("data/nqueens/unsolved/4queens.xml"), "0", EnvironmentMode.REPRODUCIBLE);
    }

}
