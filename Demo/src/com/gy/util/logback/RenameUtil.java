/**
 * 
 */
package com.gy.util.logback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RolloverFailure;

/**
 * @author ric.g21
 * 
 */
public class RenameUtil extends ch.qos.logback.core.rolling.helper.RenameUtil {
	static String RENAMING_ERROR_URL = CoreConstants.CODES_URL+"#renamingError";

	/**
	 * A robust file renaming method which in case of failure falls back to
	 * renaming by copying. In case, the file to be renamed is open by another
	 * process, renaming by copying will succeed whereas regular renaming will
	 * fail. However, renaming by copying is much slower.
	 * 
	 * @param from
	 * @param to
	 * @throws RolloverFailure
	 */
	public void rename(String from, String to) throws RolloverFailure {
		if (from.equals(to)) {
			addWarn("From and to file are the same [" + from + "]. Skipping.");
			return;
		}
		File fromFile = new File(from);

		if (fromFile.exists()) {
			File toFile = new File(to);
			createMissingTargetDirsIfNecessary(toFile);
			
			addInfo("Renaming file [" + fromFile + "] to [" + toFile + "]");

			boolean result = fromFile.renameTo(toFile);

			if (!result) {
				addWarn("Failed to rename file [" + fromFile + "] to ["
						+ toFile + "].");
				addWarn("Please consider leaving the [file] option of "
						+ RollingFileAppender.class.getSimpleName() + " empty.");
				addWarn("See also " + RENAMING_ERROR_URL);
			}else{
				fromFile.setReadable(true, false);
			}
		} else {
			throw new RolloverFailure("File [" + from + "] does not exist.");
		}
	}

	void createMissingTargetDirsIfNecessary(File toFile) throws RolloverFailure {
		if (FileUtil.isParentDirectoryCreationRequired(toFile)) {
			boolean result = FileUtil.createMissingParentDirectories(toFile);
			if (!result) {
				throw new RolloverFailure(
						"Failed to create parent directories for ["
								+ toFile.getAbsolutePath() + "]");
			}
		}
	}

	@Override
	public String toString() {
		return RenameUtil.class.getName();
	}
}
