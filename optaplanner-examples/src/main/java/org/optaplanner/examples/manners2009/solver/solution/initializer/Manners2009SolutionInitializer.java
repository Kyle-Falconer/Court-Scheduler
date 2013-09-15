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

package org.optaplanner.examples.manners2009.solver.solution.initializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.phase.custom.CustomSolverPhaseCommand;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.common.domain.PersistableIdComparator;
import org.optaplanner.examples.manners2009.domain.Guest;
import org.optaplanner.examples.manners2009.domain.Manners2009;
import org.optaplanner.examples.manners2009.domain.Seat;
import org.optaplanner.examples.manners2009.domain.SeatDesignation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Manners2009SolutionInitializer implements CustomSolverPhaseCommand {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    public void changeWorkingSolution(ScoreDirector scoreDirector) {
        Manners2009 manners2009 = (Manners2009) scoreDirector.getWorkingSolution();
        initializeSeatDesignationList(scoreDirector, manners2009);
    }

    private void initializeSeatDesignationList(ScoreDirector scoreDirector, Manners2009 manners2009) {
        // TODO the planning entity list from the solution should be used and might already contain initialized entities
        List<SeatDesignation> seatDesignationList = createSeatDesignationList(manners2009);
        // Assign one guest at a time
        List<Seat> undesignatedSeatList = new ArrayList<Seat>(manners2009.getSeatList());
        for (SeatDesignation seatDesignation : seatDesignationList) {
            Score bestScore = SimpleScore.valueOf(Integer.MIN_VALUE);
            Seat bestSeat = null;

            boolean added = false;
            // Try every seat for that guest
            // TODO by reordening the seats so index 0 has a different table then index 1 and so on,
            // this will probably be faster because perfectMatch will be true sooner
            for (Seat seat : undesignatedSeatList) {
                if (seatDesignation.getGuest().getGender() == seat.getRequiredGender()) {
                    if (!added) {
                        scoreDirector.beforeEntityAdded(seatDesignation);
                        seatDesignation.setSeat(seat);
                        scoreDirector.afterEntityAdded(seatDesignation);
                        added = true;
                    } else {
                        scoreDirector.beforeVariableChanged(seatDesignation, "seat");
                        seatDesignation.setSeat(seat);
                        scoreDirector.afterVariableChanged(seatDesignation, "seat");
                    }
                    Score score = scoreDirector.calculateScore();
                    if (score.compareTo(bestScore) > 0) {
                        bestScore = score;
                        bestSeat = seat;
                    }
                }
            }
            if (bestSeat == null) {
                throw new IllegalStateException("The bestSeat (" + bestSeat + ") cannot be null.");
            }
            scoreDirector.beforeVariableChanged(seatDesignation, "seat");
            seatDesignation.setSeat(bestSeat);
            scoreDirector.afterVariableChanged(seatDesignation, "seat");
            // There will always be enough allowed seats: ok to do this for this problem, but not ok for most problems
            undesignatedSeatList.remove(bestSeat);
        }
        // For the GUI's combobox list mainly, not really needed
        Collections.sort(seatDesignationList, new PersistableIdComparator());
        manners2009.setSeatDesignationList(seatDesignationList);
    }

    private List<SeatDesignation> createSeatDesignationList(Manners2009 manners2009) {
        List<SeatDesignation> seatDesignationList = new ArrayList<SeatDesignation>(manners2009.getGuestList().size());
        for (Guest guest : manners2009.getGuestList()) {
            SeatDesignation seatDesignation = new SeatDesignation();
            seatDesignation.setId(guest.getId());
            seatDesignation.setGuest(guest);
            seatDesignationList.add(seatDesignation);
        }
        return seatDesignationList;
    }

}
