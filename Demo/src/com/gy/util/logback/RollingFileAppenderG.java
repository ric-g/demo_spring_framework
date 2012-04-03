/**
 * 
 */
package com.gy.util.logback;

import static ch.qos.logback.core.CoreConstants.CODES_URL;

import java.io.File;
import java.io.IOException;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.rolling.helper.CompressionMode;

/**
 * @author ric.g21
 * 
 */
public class RollingFileAppenderG<E> extends FileAppender<E> {

	File currentlyActiveFile;
	TriggeringPolicy<E> triggeringPolicy;
	RollingPolicy rollingPolicy;
	
	public RollingFileAppenderG(){
		
	}

	public void start() {
		if (triggeringPolicy == null) {
			addWarn("No TriggeringPolicy was set for the RollingFileAppender named "
					+ getName());
			addWarn("For more information, please visit " + CODES_URL
					+ "#rfa_no_tp");
			return;
		}

		// we don't want to void existing log files
		if (!append) {
			addWarn("Append mode is mandatory for RollingFileAppender");
			append = true;
		}

		if (rollingPolicy == null) {
			addError("No RollingPolicy was set for the RollingFileAppender named "
					+ getName());
			addError("For more information, please visit " + CODES_URL
					+ "rfa_no_rp");
			return;
		}

		if (isPrudent()) {
			if (rawFileProperty() != null) {
				addWarn("Setting \"File\" property to null on account of prudent mode");
				setFile(null);
			}
			if (rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
				addError("Compression is not supported in prudent mode. Aborting");
				return;
			}
		}

		currentlyActiveFile = new File(getFile());
		addInfo("Active log file name: " + getFile());
		super.start();
		// set readable
		currentlyActiveFile.setReadable(true, false);
	}

	@Override
	public void stop() {
		if (rollingPolicy != null)
			rollingPolicy.stop();
		if (triggeringPolicy != null)
			triggeringPolicy.stop();
		super.stop();
	}

	@Override
	public void setFile(String file) {
		// http://jira.qos.ch/browse/LBCORE-94
		// allow setting the file name to null if mandated by prudent mode
		if (file != null
				&& ((triggeringPolicy != null) || (rollingPolicy != null))) {
			addError("File property must be set before any triggeringPolicy or rollingPolicy properties");
			addError("Visit " + CODES_URL
					+ "#rfa_file_after for more information");
		}
		super.setFile(file);
	}

	@Override
	public String getFile() {
		return rollingPolicy.getActiveFileName();
	}

	/**
	 * <p>
	 * Sets and <i>opens</i> the file where the log output will go. The
	 * specified file must be writable.
	 * 
	 * <p>
	 * If there was already an opened file, then the previous file is closed
	 * first.
	 * 
	 * <p>
	 * <b>Do not use this method directly. To configure a FileAppender or one of
	 * its subclasses, set its properties one by one and then call start().</b>
	 * 
	 * @param filename
	 *            The path to the log file.
	 * @param append
	 *            If true will append to fileName. Otherwise will truncate
	 *            fileName.
	 * @param bufferedIO
	 * @param bufferSize
	 * 
	 * @throws IOException
	 * 
	 */
	public void openFile(String file_name) throws IOException {
		synchronized (lock) {
			File file = new File(file_name);
			if (FileUtil.isParentDirectoryCreationRequired(file)) {
				boolean result = FileUtil.createMissingParentDirectories(file);
				if (!result) {
					addError("Failed to create parent directories for ["
							+ file.getAbsolutePath() + "]");
				}
			}
			file.setReadable(true, false);
			ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(
					file, append);
			resilientFos.setContext(context);
			setOutputStream(resilientFos);
		}
	}

	/**
	 * Implemented by delegating most of the rollover work to a rolling policy.
	 */
	public void rollover() {
		synchronized (lock) {
			// Note: This method needs to be synchronized because it needs
			// exclusive
			// access while it closes and then re-opens the target file.
			//
			// make sure to close the hereto active log file! Renaming under
			// windows
			// does not work for open files.
			this.closeOutputStream();

			try {
				rollingPolicy.rollover();
			} catch (RolloverFailure rf) {
				addWarn("RolloverFailure occurred. Deferring roll-over.");
				// we failed to roll-over, let us not truncate and risk data
				// loss
				this.append = true;
			}

			try {
				// update the currentlyActiveFile
				// http://jira.qos.ch/browse/LBCORE-90
				currentlyActiveFile = new File(rollingPolicy
						.getActiveFileName());
				// This will also close the file. This is OK since multiple
				// close operations are safe.
				this.openFile(rollingPolicy.getActiveFileName());
				// set readable
				currentlyActiveFile.setReadable(true, false);
			} catch (IOException e) {
				addError("setFile(" + fileName + ", false) call failed.", e);
			}
		}
	}

	@Override
	protected void subAppend(E event) {
		// The roll-over check must precede actual writing. This is the
		// only correct behavior for time driven triggers.

		// We need to synchronize on triggeringPolicy so that only one rollover
		// occurs at a time
		synchronized (triggeringPolicy) {
			if (triggeringPolicy.isTriggeringEvent(currentlyActiveFile, event)) {
				rollover();
			}
		}

		super.subAppend(event);
	}

	

	public RollingPolicy getRollingPolicy() {
		return rollingPolicy;
	}

	public TriggeringPolicy<E> getTriggeringPolicy() {
		return triggeringPolicy;
	}

	/**
	 * Sets the rolling policy. In case the 'policy' argument also implements
	 * {@link TriggeringPolicy}, then the triggering policy for this appender is
	 * automatically set to be the policy argument.
	 * 
	 * @param policy
	 */
	@SuppressWarnings("unchecked")
	public void setRollingPolicy(RollingPolicy policy) {
		rollingPolicy = policy;
		if (rollingPolicy instanceof TriggeringPolicy) {
			triggeringPolicy = (TriggeringPolicy<E>) policy;
		}

	}

	public void setTriggeringPolicy(TriggeringPolicy<E> policy) {
		triggeringPolicy = policy;
		if (policy instanceof RollingPolicy) {
			rollingPolicy = (RollingPolicy) policy;
		}
	}

}
