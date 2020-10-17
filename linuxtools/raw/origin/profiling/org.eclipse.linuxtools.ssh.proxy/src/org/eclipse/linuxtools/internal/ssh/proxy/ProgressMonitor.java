/*******************************************************************************
 * Copyright (c) 2012, 2018 IBM Corporation and others.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.ssh.proxy;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.jcraft.jsch.SftpProgressMonitor;

class ProgressMonitor implements SftpProgressMonitor {
    private IProgressMonitor monitor;
    private long max;
    private String message;

    public ProgressMonitor(IProgressMonitor monitor, String message) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        this.monitor = monitor;
        this.message = message;
    }

    @Override
      public void init(int op, String src, String dest, long max) {
         monitor.beginTask(message, 100);
         this.max = max;
      }

    @Override
    public boolean count(long count) {
        if (max != 0)
            monitor.worked((int)(count/max));
        return !monitor.isCanceled();
    }

    @Override
    public void end() {
         monitor.done();
    }
}
