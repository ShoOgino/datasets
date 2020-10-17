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
package org.eclipse.linuxtools.vagrant.core;

import java.io.File;
import java.util.List;

public interface IVagrantConnection {

	String getName();

	void addVMListener(IVagrantVMListener listener);

	void removeVMListener(IVagrantVMListener listener);

	List<IVagrantVM> getVMs();

	List<IVagrantVM> getVMs(boolean force);

	boolean isVMsLoaded();

	void addToTrackedKeys(String key);

	void addBoxListener(IVagrantBoxListener listener);

	void removeBoxListener(IVagrantBoxListener listener);

	boolean isBoxesLoaded();

	List<IVagrantBox> getBoxes();

	List<IVagrantBox> getBoxes(boolean force);

	void init(File vagrantDir);

	void up(File vagrantDir, String provider);

	void addBox(String name, String location, boolean progress) throws VagrantException, InterruptedException;

	void destroyVM(IVagrantVM vm) throws VagrantException, InterruptedException;

	void haltVM(IVagrantVM vm) throws VagrantException, InterruptedException;

	void startVM(IVagrantVM vm) throws VagrantException, InterruptedException;

	void removeBox(String name) throws VagrantException, InterruptedException;

	void packageVM(IVagrantVM vm, String name) throws VagrantException, InterruptedException;
}
