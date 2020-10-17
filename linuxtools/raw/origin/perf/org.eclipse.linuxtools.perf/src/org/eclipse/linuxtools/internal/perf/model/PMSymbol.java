/*******************************************************************************
 * (C) Copyright 2010, 0218 IBM Corp. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Thavidu Ranatunga (IBM) - Initial implementation.
 *******************************************************************************/
package org.eclipse.linuxtools.internal.perf.model;


public class PMSymbol extends TreeParent {
    private boolean pathConflictFound = false;

    public String getFunctionName() {
        String tmpName = getName();
        if (tmpName.startsWith("[")) { // filer out the "[.] " //$NON-NLS-1$
            tmpName = tmpName.substring(4);
        }
        int argloc = tmpName.indexOf('('); // and the (....
        if (argloc != -1) {
            tmpName = tmpName.substring(0,argloc);
        }
        return tmpName;
    }

    public PMSymbol(String symbolName, float pc, double samples) {
        super(symbolName, pc, samples);
    }

    public void addPercent(Integer lineNum, Float percent) {  //Adds percent to a lineref within this symbol.
        PMLineRef current = (PMLineRef)getChild(lineNum.toString());
        if (current == null) {
            current = new PMLineRef(lineNum, percent);
            addChild(current);
        } else {
            current.addPercent(percent);
        }
    }

    @Override
    public String toString() {
        return getPercent()
                + "% (" + getFormattedSamples() + " samples) in " + getName() + (pathConflictFound ? "(Warning multiple paths found for this symbol!)" : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public void markConflict() {
        pathConflictFound = true;
    }

    public boolean conflicted() {
        return pathConflictFound;
    }
}
