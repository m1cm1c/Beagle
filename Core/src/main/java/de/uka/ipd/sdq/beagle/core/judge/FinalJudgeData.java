package de.uka.ipd.sdq.beagle.core.judge;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import java.io.Serializable;
import java.util.stream.DoubleStream;

/**
 * Stores all data of {@link FinalJudge}.
 *
 * @author Christoph Michelbach
 */
class FinalJudgeData implements Serializable {

	/**
	 * serialVersionUID to make sure serialisation works fine.
	 */
	private static final long serialVersionUID = 9038094699907524618L;

	/**
	 * The millisecond timestamp of the point in time when evolution of evaluable
	 * expressions started or {@code -1} to indicate that this field isn't initialised.
	 */
	private long startTime = -1;

	/**
	 * The number of generations with significant improvement (see
	 * {@code FinalJugde#SIGNIFICANT_IMPROVEMENT} passed.
	 */
	private int numberOfGenerationsWithoutSignificantImprovementPassed;

	/**
	 * Contains the fittest value of each seff element (no mapping needed).
	 */
	private MultiSet<Double> fittestValues;

	/**
	 * The latest fitness value which came with good enough improvement to set
	 * {@code numberOfGenerationsWithoutSignificantImprovementPassed} to {@code 0}.
	 */
	private double fitnessBaselineValue = Double.POSITIVE_INFINITY;

	/**
	 * Returns {@code startTime}.
	 *
	 * @return The {@code startTime}.
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * Sets {@code startTime}.
	 *
	 * @param startTime The {@code startTime} to set.
	 */
	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns {@code numberOfGenerationsWithoutSignificantImprovementPassed}.
	 *
	 * @return The {@code numberOfGenerationsWithoutSignificantImprovementPassed}.
	 */
	public int getNumberOfGenerationsWithoutSignificantImprovementPassed() {
		return this.numberOfGenerationsWithoutSignificantImprovementPassed;
	}

	/**
	 * Sets {@code numberOfGenerationsWithoutSignificantImprovementPassed}.
	 *
	 * @param numberOfGenerationsWithoutSignificantImprovementPassed The
	 *            {@code numberOfGenerationsWithoutSignificantImprovementPassed} to set.
	 */
	public void setNumberOfGenerationsWithoutSignificantImprovementPassed(
		final int numberOfGenerationsWithoutSignificantImprovementPassed) {
		this.numberOfGenerationsWithoutSignificantImprovementPassed =
			numberOfGenerationsWithoutSignificantImprovementPassed;
	}

	/**
	 * Returns {@code fitnessBaselineValue}.
	 *
	 * @return The {@code fitnessBaselineValue}.
	 */
	public double getFitnessBaselineValue() {
		return this.fitnessBaselineValue;
	}

	/**
	 * Announces that a new generation is being judged. Increments the number of passed
	 * generations and sets the fittest values to an empty set.
	 */
	public void newGeneration() {
		this.fittestValues = new HashMultiSet<>();
	}

	/**
	 * Sets {@code fitnessBaselineValue}.
	 *
	 * @param fitnessBaselineValue The {@code fitnessBaselineValue} to set.
	 */
	public void setFitnessBaselineValue(final double fitnessBaselineValue) {
		this.fitnessBaselineValue = fitnessBaselineValue;
	}

	/**
	 * Adds {@code value} to the collection of the fittest value.
	 *
	 * @param value The fittest value of a seff element.
	 */
	public void addFittestValue(final double value) {
		this.fittestValues.add(value);
	}

	/**
	 * Returns the fittest values of this generation.
	 *
	 * @return The fittness values of the fittest proposed expressions of the momentary
	 *         generation. The stream will contain as many {@code double}s as there are
	 *         seff elements on the blackboard.
	 */
	public DoubleStream getFittestValues() {
		return this.fittestValues.stream().mapToDouble(value -> value);
	}
}
