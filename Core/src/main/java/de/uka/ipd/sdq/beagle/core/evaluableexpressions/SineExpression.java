package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that executes a sine function on its contained expression.
 *
 * @author Annika Berger
 * @author Christoph Michelbach
 *
 */
public class SineExpression implements EvaluableExpression {

	/**
	 * States how long it takes to evaluate the expression for a computer. The bigger the
	 * number, the harder it is. The norm is addition which means that the
	 * {@code COMPUTATINOAL_COMPLEXITY} of addition is {@code 1}. Scaling is linear.
	 */
	public static final double COMPUTATINOAL_COMPLEXITY = 20d;

	/**
	 * States how hard it is for educated humans to understand the expression. The bigger
	 * the number, the harder it is. The norm is addition which means that the
	 * {@code HUMAN_UNDERSTANDABILITY_COMPXELITY} of addition is {@code 1}. Scaling is
	 * linear.
	 */
	public static final double HUMAN_UNDERSTANDABILITY_COMPXELITY = 15d;

	/**
	 * The argument of the sine expression.
	 */
	private final EvaluableExpression argument;

	/**
	 * Builds an expression which returns the sine of the argument.
	 *
	 * @param argument The argument to be used. Must not be {@code null}.
	 */
	public SineExpression(final EvaluableExpression argument) {
		Validate.notNull(argument);
		this.argument = argument;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the argument of the sine function.
	 *
	 * @return The argument of the function.
	 */
	public EvaluableExpression getArgument() {
		return this.argument;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(final EvaluableExpressionVisitor visitor) {
		Validate.notNull(visitor);
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#evaluate(de.uka.sdq.
	 * beagle. core.expressions.EvaluableVariableAssignment)
	 */
	@Override
	public double evaluate(final EvaluableVariableAssignment variableAssignments) {
		Validate.notNull(variableAssignments);
		return Math.sin(this.argument.evaluate(variableAssignments));
	}

	@Override
	public String toString() {
		return String.format("(sin%s)", this.argument);
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final SineExpression other = (SineExpression) object;
		return new EqualsBuilder().append(this.argument, other.argument).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(231, 233).append(this.argument).toHashCode();
	}

}
