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


package courtscheduler.domain;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Charles
 * Date: 10/3/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchSlotStrengthComparator implements Comparator<MatchSlot> {
    public int compare(MatchSlot a, MatchSlot b) {
        /* This method will compare two teams based on their constraints.
        * Teams with fewer constraints should sort "higher" than teams with more constraints,
        * since teams with fewer constraints are more likely to fit into a given match.
        * See 4.3.5.3 in the OptaPlanner 6.0 documentation for more. --MS */

        int i = new CompareToBuilder()
                .append(a.getDay(), b.getDay())
                .append(a.getTime(), b.getTime())
                .toComparison();
        //System.out.println(">> " + a + " " + b + " " + i);
        return i;
        //0 + (int)(Math.random() * ((10 - 0) + 1)); //FIXME
    }

}

