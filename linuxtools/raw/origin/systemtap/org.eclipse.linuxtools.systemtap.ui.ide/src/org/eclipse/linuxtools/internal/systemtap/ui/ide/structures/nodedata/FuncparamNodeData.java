/*******************************************************************************
 * Copyright (c) 2014, 2018 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.systemtap.ui.ide.structures.nodedata;

import org.eclipse.linuxtools.internal.systemtap.ui.ide.structures.tparsers.FunctionParser;


/**
 * A structure for containing extra information of SystemTap function parameters.
 * @since 3.0
 */
public class FuncparamNodeData implements ISingleTypedNode {
    static final String ID = "FuncparamNodeData"; //$NON-NLS-1$
    private final String type;

    @Override
    public String toString() {
        return getType();
    }

    @Override
    public String getType() {
        return type;
    }

    /**
     * Create a new instance of function parameter information.
     * @param type The <code>String</code> representation of the parameter's type.
     * Pass <code>null</code> if the type is unknown.
     */
    public FuncparamNodeData(String type) {
        this.type = type == null ? FunctionParser.UNKNOWN_TYPE : type; // Parameters can't be void.
    }
}
