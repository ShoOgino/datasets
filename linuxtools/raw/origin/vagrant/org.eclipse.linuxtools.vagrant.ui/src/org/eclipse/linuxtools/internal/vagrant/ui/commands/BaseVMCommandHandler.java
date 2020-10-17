/*******************************************************************************
 * Copyright (c) 2014, 2018 Red Hat.
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
package org.eclipse.linuxtools.internal.vagrant.ui.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.linuxtools.vagrant.core.IVagrantVM;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class BaseVMCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		final IStructuredSelection selection = HandlerUtil
				.getCurrentStructuredSelection(event);
		final List<IVagrantVM> selectedContainers = CommandUtils
				.getSelectedContainers(selection);
		final Job job = new Job(getJobName(selectedContainers)) {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				if (confirmed(selectedContainers)) {
					monitor.beginTask(getJobName(selectedContainers),
							selectedContainers.size());
					for (final IVagrantVM container : selectedContainers) {
						monitor.setTaskName(getTaskName(container));
						executeInJob(container, monitor);
						monitor.worked(1);
					}
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		return null;
	}

	void openError(final String errorMessage, final Exception e) {
		Display.getDefault()
				.syncExec(() -> MessageDialog.openError(
						Display.getCurrent().getActiveShell(), errorMessage,
						e.getMessage()));
	}

	// allow commands to add confirmation dialog
	boolean confirmed(
			@SuppressWarnings("unused") List<IVagrantVM> selectedContainers) {
		return true;
	}

	abstract String getJobName(final List<IVagrantVM> selectedContainers);

	abstract String getTaskName(final IVagrantVM container);

	abstract void executeInJob(final IVagrantVM container, IProgressMonitor monitor);
}
