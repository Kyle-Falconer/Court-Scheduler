<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <benchmarkDirectory>local/data/cloudbalancing/template</benchmarkDirectory>
  <parallelBenchmarkCount>AUTO</parallelBenchmarkCount>
  <warmUpSecondsSpend>30</warmUpSecondsSpend>

  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <xstreamAnnotatedClass>org.optaplanner.examples.cloudbalancing.domain.CloudBalance</xstreamAnnotatedClass>
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/2computers-6processes.xml</inputSolutionFile>-->
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/3computers-9processes.xml</inputSolutionFile>-->
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/4computers-12processes.xml</inputSolutionFile>-->
      <inputSolutionFile>data/cloudbalancing/unsolved/100computers-300processes.xml</inputSolutionFile>
      <inputSolutionFile>data/cloudbalancing/unsolved/200computers-600processes.xml</inputSolutionFile>
      <inputSolutionFile>data/cloudbalancing/unsolved/400computers-1200processes.xml</inputSolutionFile>
      <inputSolutionFile>data/cloudbalancing/unsolved/800computers-2400processes.xml</inputSolutionFile>
      <problemStatisticType>BEST_SCORE</problemStatisticType>
    </problemBenchmarks>

    <solver>
      <solutionClass>org.optaplanner.examples.cloudbalancing.domain.CloudBalance</solutionClass>
      <planningEntityClass>org.optaplanner.examples.cloudbalancing.domain.CloudProcess</planningEntityClass>
      <scoreDirectorFactory>
        <scoreDefinitionType>HARD_SOFT</scoreDefinitionType>
        <scoreDrl>/org/optaplanner/examples/cloudbalancing/solver/cloudBalancingScoreRules.drl</scoreDrl>
      </scoreDirectorFactory>
      <termination>
        <maximumMinutesSpend>5</maximumMinutesSpend>
      </termination>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT_DECREASING</constructionHeuristicType>
        <forager>
          <pickEarlyType>FIRST_NON_DETERIORATING_SCORE</pickEarlyType>
        </forager>
      </constructionHeuristic>
    </solver>
  </inheritedSolverBenchmark>

<#list [5, 7, 11, 13] as entityTabuSize>
<#list [500, 1000, 2000] as acceptedCountLimit>
  <solverBenchmark>
    <name>entityTabuSize ${entityTabuSize} acceptedCountLimit ${acceptedCountLimit}</name>
    <solver>
      <localSearch>
        <unionMoveSelector>
          <changeMoveSelector/>
          <swapMoveSelector/>
        </unionMoveSelector>
        <acceptor>
          <entityTabuSize>${entityTabuSize}</entityTabuSize>
        </acceptor>
        <forager>
          <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>
</#list>
</#list>
</plannerBenchmark>
