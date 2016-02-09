package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunctionBlackboardView;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.eclipse.net4j.util.collection.Pair;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Tests for {@link PcmRepositoryBlackboardFactory}.
 * 
 * @author Christoph Michelbach
 */
public class PcmRepositoryBlackboardFactoryTest {

	/**
	 * A factory which creates instances of {@link PcmRepositoryBlackboardFactory}.
	 */
	private static PcmRepositoryBlackboardFactoryFactory pcmRepositoryBlackboardFactoryFactory =
		new PcmRepositoryBlackboardFactoryFactory();

	/**
	 * Pseudo fitness function.
	 */
	private EvaluableExpressionFitnessFunction fitnessFunction = new EvaluableExpressionFitnessFunction() {

		/**
		 * Store between (pairs of measurable seff elements and evaluable expressions) and
		 * doubles.
		 */
		private HashMap<Pair<MeasurableSeffElement, EvaluableExpression>, Double> store = new HashMap<>();

		@Override
		public double gradeFor(final ExternalCallParameter parameter, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(parameter, expression);
		}

		@Override
		public double gradeFor(final SeffLoop loop, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(loop, expression);
		}

		@Override
		public double gradeFor(final SeffBranch branch, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(branch, expression);
		}

		@Override
		public double gradeFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(rdia, expression);
		}

		/**
		 * Determines the fitness of a combination of a {@code MeasurableSeffElement} and
		 * an {@code EvaluableExpression}.
		 *
		 * @param element A {@code MeasurableSeffElement}.
		 * @param expression An {@code EvaluableExpression}.
		 * @return The fitness value.
		 */
		private double determineValue(final MeasurableSeffElement element, final EvaluableExpression expression) {
			final Pair<MeasurableSeffElement, EvaluableExpression> pair = new Pair<>(parameter, expression);
			if (this.store.containsKey(pair)) {
				return this.store.get(pair);
			} else {
				final double fitness = Math.pow(Math.random() * 10E5, 3);
				this.store.put(pair, fitness);

				return fitness;
			}
		}
	};

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(java.util.Set)
	 * and PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(String)}. Asserts
	 * that creation is possible and {@code null} or an empty string or otherwise
	 * impossible path cannot be passed.
	 */
	@Test
	public void pcmRepositoryBlackboardFactory() {
		assertThat(() -> new PcmRepositoryBlackboardFactory((String) null, this.fitnessFunction),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory((File) null, this.fitnessFunction),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory(".", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("..", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("/", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("/tmp", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("\0", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));

		final File[] impossibleRepositoryFiles = {
			new File(""), new File("."), new File(".."), new File("/"), new File("/tmp"), new File("\0")
		};

		for (File impossibleRepositoryFile : impossibleRepositoryFiles) {
			assertThat(() -> new PcmRepositoryBlackboardFactory(impossibleRepositoryFile, this.fitnessFunction),
				throwsException(IllegalArgumentException.class));
		}
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForAllElements()} .
	 * 
	 * @throws FileNotFoundException if a source code file can not be resolved
	 */
	@Test
	public void getBlackboardForAllElements() throws FileNotFoundException {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();
		final Blackboard result = pcmRepositoryBlackboardFactory.getBlackboardForAllElements();
		assertThat(result, is(notNullValue()));

		assertThat(result.getAllRdias().size(), is(not(0)));
		assertThat(result.getAllSeffBranches().size(), is(not(0)));
		assertThat(result.getAllSeffLoops().size(), is(not(0)));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.util.Collection)} .
	 */
	@Test
	public void getBlackboardForIdsCollectionOfString() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		final HashSet<String> collection = new HashSet<String>();
		collection.add("");

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds(collection),
			is(new BlackboardFactory().getEmpty()));

		assertThat(() -> pcmRepositoryBlackboardFactory.getBlackboardForIds((String) null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.lang.String[])} .
	 * 
	 * @throws FileNotFoundException if a source code file can not be resolved
	 */
	@Test
	public void getBlackboardForIdsStringArray() throws FileNotFoundException {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds(""), is(new BlackboardFactory().getEmpty()));

		assertThat(() -> pcmRepositoryBlackboardFactory.getBlackboardForIds((String[]) null),
			throwsException(NullPointerException.class));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"), is(not(nullValue())));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			is(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g")));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			is(not(pcmRepositoryBlackboardFactory.getBlackboardForIds("_FaSO4LnqEeWVlphM5rov7g"))));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			is(not(pcmRepositoryBlackboardFactory.getBlackboardForAllElements())));

		final Blackboard emptyBlackboard =
			new Blackboard(new HashSet<ResourceDemandingInternalAction>(), new HashSet<SeffBranch>(),
				new HashSet<SeffLoop>(), new HashSet<ExternalCallParameter>(), this.fitnessFunction);

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_SomeIdWhichDosntExistA"), is(emptyBlackboard));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_TooShortId"), is(emptyBlackboard));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("IllegalId"), is(emptyBlackboard));

		final Blackboard blackboardForIds =
			pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g", "_FaSO4LnqEeWVlphM5rov7g");

		assertThat(blackboardForIds.getAllSeffLoops().size(), is(not(0)));

		for (SeffLoop seffLoop : blackboardForIds.getAllSeffLoops()) {
			// How do i figure out whether this is correct?
			seffLoop.getLoopBody().getStartFile();
		}
	}

}
