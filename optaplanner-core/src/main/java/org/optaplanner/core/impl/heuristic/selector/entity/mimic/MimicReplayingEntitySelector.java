package org.optaplanner.core.impl.heuristic.selector.entity.mimic;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.heuristic.selector.common.iterator.SelectionIterator;
import org.optaplanner.core.impl.heuristic.selector.entity.AbstractEntitySelector;
import org.optaplanner.core.impl.phase.AbstractSolverPhaseScope;

public class MimicReplayingEntitySelector extends AbstractEntitySelector {

    protected final MimicRecordingEntitySelector recordingEntitySelector;

    protected boolean hasRecordingCreated;
    protected boolean hasRecording;
    protected boolean recordingCreated;
    protected Object recording;
    protected boolean recordingAlreadyReturned;

    public MimicReplayingEntitySelector(MimicRecordingEntitySelector recordingEntitySelector) {
        this.recordingEntitySelector = recordingEntitySelector;
        // No solverPhaseLifecycleSupport because the MimicRecordingEntitySelector is hooked up elsewhere too
        recordingEntitySelector.addMimicReplayingEntitySelector(this);
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void phaseStarted(AbstractSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        // Doing this in phaseStarted instead of stepStarted due to QueuedEntityPlacer compatibility
        hasRecordingCreated = false;
        recordingCreated = false;
    }

    @Override
    public void phaseEnded(AbstractSolverPhaseScope phaseScope) {
        super.phaseEnded(phaseScope);
        // Doing this in phaseEnded instead of stepEnded due to QueuedEntityPlacer compatibility
        hasRecordingCreated = false;
        hasRecording = false;
        recordingCreated = false;
        recording = null;
    }

    public PlanningEntityDescriptor getEntityDescriptor() {
        return recordingEntitySelector.getEntityDescriptor();
    }

    public boolean isContinuous() {
        return recordingEntitySelector.isContinuous();
    }

    public boolean isNeverEnding() {
        return recordingEntitySelector.isNeverEnding();
    }

    public long getSize() {
        return recordingEntitySelector.getSize();
    }

    public Iterator<Object> iterator() {
        return new ReplayingEntityIterator();
    }

    public void recordedHasNext(boolean hasNext) {
        hasRecordingCreated = true;
        hasRecording = hasNext;
        recordingCreated = false;
        recording = null;
        recordingAlreadyReturned = false;
    }

    public void recordedNext(Object next) {
        hasRecordingCreated = true;
        hasRecording = true;
        recordingCreated = true;
        recording = next;
        recordingAlreadyReturned = false;
    }

    private class ReplayingEntityIterator extends SelectionIterator<Object> {

        private ReplayingEntityIterator() {
            // Reset so the last recording plays again even if it has already played
            recordingAlreadyReturned = false;
        }

        public boolean hasNext() {
            if (!hasRecordingCreated) {
                throw new IllegalStateException("Replay must occur after record."
                        + " The recordingEntitySelector (" + recordingEntitySelector
                        + ")'s hasNext() has not been called yet. ");
            }
            return hasRecording && !recordingAlreadyReturned;
        }

        public Object next() {
            if (!recordingCreated) {
                throw new IllegalStateException("Replay must occur after record."
                        + " The recordingEntitySelector (" + recordingEntitySelector
                        + ")'s next() has not been called yet. ");
            }
            if (recordingAlreadyReturned) {
                throw new NoSuchElementException("The recordingAlreadyReturned (" + recordingAlreadyReturned
                        + ") is impossible. Check if hasNext() returns true before this call.");
            }
            // Until the recorder records something, this iterator has no next.
            recordingAlreadyReturned = true;
            return recording;
        }

    }

    public Iterator<Object> endingIterator() {
        // No replaying, because the endingIterator() is used for determining size
        return recordingEntitySelector.endingIterator();
    }

    public ListIterator<Object> listIterator() {
        // TODO Not yet implemented
        throw new UnsupportedOperationException();
    }

    public ListIterator<Object> listIterator(int index) {
        // TODO Not yet implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "Replaying(" + recordingEntitySelector + ")";
    }

}
