/*******************************************************************************
 * Copyright (c) 2015, 2018 Red Hat.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - Initial Contribution
 *******************************************************************************/

package org.eclipse.linuxtools.internal.docker.core;

import org.eclipse.core.runtime.Platform;

/**
 * Utility class for System/OS info
 */
public class SystemUtils {

	/**
	 * @return <code>true</code> if if the current OS is Windows,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isWindows() {
		return Platform.getOS().equals(Platform.OS_WIN32);
	}

	/**
	 * @return <code>true</code> if if the current OS is Mac, <code>false</code>
	 *         otherwise.
	 */
	public static boolean isMac() {
		return Platform.getOS().equals(Platform.OS_MACOSX);
	}

	/**
	 * @return <code>true</code> if if the current OS is Linux,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isLinux() {
		return Platform.getOS().equals(Platform.OS_LINUX);
	}

}
