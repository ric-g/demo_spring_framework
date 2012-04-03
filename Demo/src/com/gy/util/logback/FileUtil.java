/**
 * 
 */
package com.gy.util.logback;

import java.io.File;

/**
 * @author ric.g21
 * 
 */
public class FileUtil extends ch.qos.logback.core.util.FileUtil {
	static public boolean isParentDirectoryCreationRequired(File file) {
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
