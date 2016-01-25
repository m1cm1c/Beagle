package de.uka.ipd.sdq.beagle.core.evaluableexpressions;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 *
 * <p>COVERAGE:OFF
 */

/**
 * Might be thrown if a {@link EvaluableExpression} contains itself.
 *
 * @author Joshua Gleitze
 * @see EvaluableExpression
 */
public class ExpressionRecursionException extends RuntimeException {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -3704244273606345556L;

	/**
	 * The expression which was the cause of this exception.
	 */
	private final EvaluableExpression selfContainedExpression;

	/**
	 * Creates an Exception for the fact that {@code selfContainedExpression}, contains
	 * itself.
	 *
	 * @param selfContainedExpression an expression containing itself.
	 */
	public ExpressionRecursionException(final EvaluableExpression selfContainedExpression) {
		super(String.format("Can not evaluate: expression %s containes itself!", selfContainedExpression));
		this.selfContainedExpression = selfContainedExpression;
	}

	/**
	 * The expression which contains itself and raised this exception.
	 *
	 * @return the causing, self-contained expression.
	 */
	public EvaluableExpression getCausingExpression() {
		return this.selfContainedExpression;
	}
}
