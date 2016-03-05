package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds information about the project under analysis. Objects of this class are
 * immutable.
 *
 * @author Roman Langrehr
 * @author Christoph Michelbach
 */
public class ProjectInformation implements Serializable {

	/**
	 * See {@link Serializable}.
	 */
	private static final long serialVersionUID = 2451089669559562551L;

	/**
	 * The timeout to be used.
	 */
	private final Timeout timeout;

	/**
	 * The current state of the analysis.
	 */
	private AnalysisState analysisState;

	/**
	 * The provider of the source files to be analysed.
	 */
	private final SourceCodeFileProvider fileProvider;

	/**
	 * The class path containing everything needed to compile the project.
	 */
	private final String buildPath;

	/**
	 * The charset used for all files in the project.
	 */
	private final Charset charset;

	/**
	 * The configurations defining how to launch the measured software.
	 */
	private final Set<LaunchConfiguration> launchConfigurations;

	/**
	 * Creates a new Project Information.
	 *
	 * @param timeout The timeout to be used.
	 * @param fileProvider The provider of the source files to be analysed.
	 * @param buildPath The class path containing everything needed to compile the
	 *            project.
	 * @param charset The charset used for all files in the project. May be {@code null},
	 *            in this case {@link Charset#defaultCharset()} will be used.
	 * @param launchConfigurations The configurations defining how to launch the measured
	 *            software. Must not be {@code null}, must not contain {@code null}, must
	 *            not be empty.
	 */
	public ProjectInformation(final Timeout timeout, final SourceCodeFileProvider fileProvider, final String buildPath,
		final Charset charset, final Set<LaunchConfiguration> launchConfigurations) {
		Validate.notNull(fileProvider);
		Validate.notNull(buildPath);
		Validate.noNullElements(launchConfigurations);
		Validate.isTrue(!launchConfigurations.isEmpty(), "The launch configurations may not be empty");

		this.timeout = timeout;
		this.fileProvider = fileProvider;
		this.charset = charset == null ? Charset.defaultCharset() : charset;
		this.buildPath = buildPath;
		this.launchConfigurations = new HashSet<>(launchConfigurations);
		this.analysisState = AnalysisState.RUNNING;
	}

	/**
	 * Queries the project’s build path.
	 *
	 * @return The class path containing everything needed to compile the project. Will
	 *         never be {@code null}.
	 */
	public String getBuildPath() {
		return this.buildPath;
	}

	/**
	 * Queries the project’s charset.
	 *
	 * @return The charset used for all files in the project. Will never be {@code null}.
	 */
	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * Returns the timeout to be used. The timeout describes the minimum time Beagle shall
	 * keep trying to find results while no perfect results were found.
	 *
	 * @return The timeout that will be used by Beagle.
	 */
	public Timeout getTimeout() {
		return this.timeout;
	}

	/**
	 * Queries the provider responsible to get the source files Beagle shall analyse.
	 *
	 *
	 * @return The provider of source files.
	 */
	public SourceCodeFileProvider getFileProvider() {
		return this.fileProvider;
	}

	/**
	 * Queries the configurations defining how to launch the measured software.
	 *
	 * @return The project’s launch configurations. Will neither be {@code null}, nor
	 *         contain {@code null}, nor be empty.
	 */
	public Set<LaunchConfiguration> getLaunchConfigurations() {
		return new HashSet<>(this.launchConfigurations);
	}

	/**
	 * Returns the current state of the analysis.
	 *
	 * @return The current state of the analysis.
	 */
	public AnalysisState getAnalysisState() {
		return this.analysisState;
	}

	/**
	 * Sets the current state of the analysis to {@code analysisState}.
	 *
	 * @param analysisState The state the analysis will be in after this method has been
	 *            called.
	 */
	public void setAnalysisState(final AnalysisState analysisState) {

		/*
		 * Ignore this method call if the new state is equal to the old state.
		 */
		if (this.analysisState.equals(analysisState)) {
			return;
		}

		switch (analysisState) {
			case RUNNING:
				Validate.validState(this.analysisState == AnalysisState.ENDING,
					"Can't switch from %s to AnalysisState.RUNNING.", this.analysisState);

				this.analysisState = AnalysisState.RUNNING;
				AnalysisState.ENDING.notifyAll();
				break;
			case ABORTING:
				Validate.validState(this.analysisState == AnalysisState.RUNNING,
					"Can't switch from %s to AnalysisState.ABORTING.", this.analysisState);

				this.analysisState = AnalysisState.ABORTING;

				break;
			case ENDING:
				Validate.validState(this.analysisState == AnalysisState.RUNNING,
					"Can't switch from %s to AnalysisState.ENDING.", this.analysisState);

				this.analysisState = AnalysisState.ENDING;

				break;
			case TERMINATED:
				this.analysisState = AnalysisState.TERMINATED;

				break;
			default:
				Validate.isTrue(false);
				break;
		}
	}

	/**
	 * The current state of the analysis.
	 *
	 * @author Christoph Michelbach
	 */
	public enum AnalysisState {
		/**
		 * The analysis is currently running.
		 */
		RUNNING,

		/**
		 * Stopping the analysis without trying to preserve data accumulated in the
		 * current phase.
		 */
		ABORTING,

		/**
		 * Stopping the analysis but trying to preserve data accumulated in the current
		 * phase.
		 */
		ENDING,

		/**
		 * The analysis isn't running.
		 */
		TERMINATED
	}
}
