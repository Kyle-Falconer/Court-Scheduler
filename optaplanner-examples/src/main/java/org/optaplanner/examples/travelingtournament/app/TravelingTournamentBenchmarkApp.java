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

package org.optaplanner.examples.travelingtournament.app;

import org.optaplanner.examples.common.app.CommonBenchmarkApp;

public class TravelingTournamentBenchmarkApp extends CommonBenchmarkApp {

    public static final String BENCHMARK_CONFIG_PREFIX
            = "/org/optaplanner/examples/travelingtournament/benchmark/";
    public static final String BENCHMARK_CONFIG
            = BENCHMARK_CONFIG_PREFIX + "travelingTournamentBenchmarkConfig.xml";
    public static final String STEP_LIMIT_BENCHMARK_CONFIG
            = BENCHMARK_CONFIG_PREFIX + "travelingTournamentStepLimitBenchmarkConfig.xml";

    public static void main(String[] args) {
        String benchmarkConfig;
        if (args.length > 0) {
            // default is a workaround for http://jira.codehaus.org/browse/MEXEC-35
            if (args[0].equals("default")) {
                benchmarkConfig = BENCHMARK_CONFIG;
            } else if (args[0].equals("stepLimit")) {
                benchmarkConfig = STEP_LIMIT_BENCHMARK_CONFIG;
            } else {
                benchmarkConfig = BENCHMARK_CONFIG_PREFIX + args[0] + "BenchmarkConfig.xml";
            }
        } else {
            benchmarkConfig = BENCHMARK_CONFIG;
        }
        new TravelingTournamentBenchmarkApp().buildAndBenchmark(benchmarkConfig);
    }

}
