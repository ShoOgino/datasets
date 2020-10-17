/*******************************************************************************
 * Copyright (c) 2012, 2018 Red Hat Inc. and others.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.gprof.launch;

import org.eclipse.osgi.util.NLS;

public class GprofLaunchMessages extends NLS {

    // Messages related to when the user is asked if he would like the '-pg' flag to be added to the active configuration.
    public static String
    GprofMissingFlag_Title,
    GprofMissingFlag_Body_shared,
    GprofMissingFlag_Body_Autotools,
    GprofMissingFlag_Body_Managed,
    GprofMissingFlag_BodyPost_autoAddFlagQuestion;

    //Messages for when gmon.out is not found.
    public static String
    GprofNoGmonOut_title,
    GprofNoGmonOut_body,
    GprofNoGmonOut_BrowseWorkSpace,
    GprofNoGmonOut_BrowseFileSystem,
    GprofNoGmonOut_CancleLaunch;

    //for when gmon.out is too old.
    public static String GprofGmonStale_msg;
    public static String GprofGmonStaleExplanation_msg;

    //Browsing workspace/filesystem
    public static String GprofNoGmonDialog_OpenGmon;

    static {
        NLS.initializeMessages(GprofLaunchMessages.class.getName(), GprofLaunchMessages.class);
    }

}
