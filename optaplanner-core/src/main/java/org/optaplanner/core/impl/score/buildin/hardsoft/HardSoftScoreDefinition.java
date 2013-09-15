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

package org.optaplanner.core.impl.score.buildin.hardsoft;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.holder.ScoreHolder;
import org.optaplanner.core.impl.score.definition.AbstractScoreDefinition;

public class HardSoftScoreDefinition extends AbstractScoreDefinition<HardSoftScore> {

    private double hardScoreTimeGradientWeight = 0.75; // TODO this is a guess

    private HardSoftScore perfectMaximumScore = HardSoftScore.valueOf(0, 0);
    private HardSoftScore perfectMinimumScore = HardSoftScore.valueOf(
            Integer.MIN_VALUE, Integer.MIN_VALUE);

    public double getHardScoreTimeGradientWeight() {
        return hardScoreTimeGradientWeight;
    }

    /**
     * It's recommended to use a number which can be exactly represented as a double,
     * such as 0.5, 0.25, 0.75, 0.125, ... but not 0.1, 0.2, ...
     * @param hardScoreTimeGradientWeight 0.0 <= hardScoreTimeGradientWeight <= 1.0
     */
    public void setHardScoreTimeGradientWeight(double hardScoreTimeGradientWeight) {
        this.hardScoreTimeGradientWeight = hardScoreTimeGradientWeight;
        if (hardScoreTimeGradientWeight < 0.0 || hardScoreTimeGradientWeight > 1.0) {
            throw new IllegalArgumentException("Property hardScoreTimeGradientWeight (" + hardScoreTimeGradientWeight
                    + ") must be greater or equal to 0.0 and smaller or equal to 1.0.");
        }
    }

    @Override
    public HardSoftScore getPerfectMaximumScore() {
        return perfectMaximumScore;
    }

    public void setPerfectMaximumScore(HardSoftScore perfectMaximumScore) {
        this.perfectMaximumScore = perfectMaximumScore;
    }

    @Override
    public HardSoftScore getPerfectMinimumScore() {
        return perfectMinimumScore;
    }

    public void setPerfectMinimumScore(HardSoftScore perfectMinimumScore) {
        this.perfectMinimumScore = perfectMinimumScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public Class<HardSoftScore> getScoreClass() {
        return HardSoftScore.class;
    }

    public Score parseScore(String scoreString) {
        return HardSoftScore.parseScore(scoreString);
    }

    public double calculateTimeGradient(HardSoftScore startScore, HardSoftScore endScore,
            HardSoftScore score) {
        if (score.compareTo(endScore) > 0) {
            return 1.0;
        } else if (score.compareTo(startScore) < 0) {
            return 0.0;
        }
        double timeGradient = 0.0;
        double softScoreTimeGradientWeight = 1.0 - hardScoreTimeGradientWeight;
        if (startScore.getHardScore() == endScore.getHardScore()) {
            timeGradient += hardScoreTimeGradientWeight;
        } else {
            int hardScoreTotal = endScore.getHardScore() - startScore.getHardScore();
            int hardScoreDelta = score.getHardScore() - startScore.getHardScore();
            double hardTimeGradient = (double) hardScoreDelta / (double) hardScoreTotal;
            timeGradient += hardTimeGradient * hardScoreTimeGradientWeight;
        }
        if (score.getSoftScore() >= endScore.getSoftScore()) {
            timeGradient += softScoreTimeGradientWeight;
        } else if (score.getSoftScore() <= startScore.getSoftScore()) {
            // No change: timeGradient += 0.0
        } else {
            int softScoreTotal = endScore.getSoftScore() - startScore.getSoftScore();
            int softScoreDelta = score.getSoftScore() - startScore.getSoftScore();
            double softTimeGradient = (double) softScoreDelta / (double) softScoreTotal;
            timeGradient += softTimeGradient * softScoreTimeGradientWeight;
        }
        return timeGradient;
    }

    public ScoreHolder buildScoreHolder(boolean constraintMatchEnabled) {
        return new HardSoftScoreHolder(constraintMatchEnabled);
    }

}
