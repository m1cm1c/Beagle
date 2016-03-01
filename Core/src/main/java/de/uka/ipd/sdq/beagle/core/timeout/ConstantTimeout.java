package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

/**
 * Provides a constant timeout which cannot be changed.
 *
 * @author Christoph Michelbach
 */
public class ConstantTimeout extends Timeout {

	/**
	 * The timeout in milliseconds.
	 */
	private long timeout;

	/**
	 * Constructs a new constant timeout.
	 *
	 * @param timeout The timeout in milliseconds. Must not be negative.
	 */
	public ConstantTimeout(final int timeout) {
		Validate.isTrue(timeout >= 0);

		this.timeout = timeout;

		new Thread(this::notifyOnReachedTimeout).start();
	}

	@Override
	public boolean isReached() {
		Validate.isTrue(this.initialised);

		return System.currentTimeMillis() - this.startingTime > this.timeout;
	}

	/**
	 * Returns the timeout in milliseconds.
	 *
	 * @return The timeout in milliseconds.
	 */
	public long getTimeout() {
		Validate.isTrue(this.initialised);

		return this.timeout;
	}

	@Override
	public void reportOneStepProgress() {
		Validate.isTrue(this.initialised);
	}

	/**
	 * Will be executed once the timeout is reached.
	 */
	private void notifyOnReachedTimeout() {
		// Wait until the timeout is up.
		while (!ConstantTimeout.this.isReached()) {
			final long timeToSleep =
				ConstantTimeout.this.startingTime + ConstantTimeout.this.timeout - System.currentTimeMillis();

			/**
			 * This can happen if the necessary time passed between the execution of the
			 * loop condition and the calculation if {@code timeToWait}.
			 */
			if (timeToSleep <= 0) {
				assert ConstantTimeout.this.isReached();
			} else {
				while (true) {
					try {
						Thread.sleep(timeToSleep);

						// Don't retry if the try block has been executed successfully.
						break;
					} // CHECKSTYLE:OFF
					catch (final InterruptedException exception) {
						// Retry on interrupt.
					}
					// CHECKSTYLE:ON
				}
			}

			for (final Runnable callback : ConstantTimeout.this.callbacks) {
				new Thread(callback).start();
			}

		}
	}

}
