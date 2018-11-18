/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package com.atl.common.trans;

import static com.atl.common.models.Models.atl;
import static com.atl.common.utils.Preconditions.checkArgument;
import static com.atl.common.utils.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.engine.emfvm.ASM;
import org.eclipse.m2m.atl.engine.emfvm.ASMXMLReader;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import com.atl.common.utils.ATLCompiler;

/**
 * Uses {@link AtlParser} to create {@code ASM} from ATL {@code EMFModel}.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.1
 */
public class ATLModel2ASM implements Transformation<EMFModel, ASM> {
	private final static AtlParser atlParser;

	public ATLModel2ASM() {
		super();
	}

	static {
		atlParser = AtlParser.getDefault();
	}

	public ASM apply(EMFModel arg) {
		checkNotNull(arg);
		checkArgument(atl().equals(arg.getReferenceModel()), "Not a valid argument, should be conform to ATL metamodel");
		
		ASM asm = null;
		EObject[] pbs = null;
		final OutputStream source = new ByteArrayOutputStream();
		final OutputStream result = new ByteArrayOutputStream();

		try {
			atlParser.extract(arg, source, Collections.EMPTY_MAP);
		} catch (ATLCoreException e) {
			e.printStackTrace();
		}

		try {
			pbs = ATLCompiler.compile(new ByteArrayInputStream(source.toString().getBytes()), result);

			if (pbs.length == 0) {
				asm = new ASMXMLReader().read(new ByteArrayInputStream(result.toString().getBytes()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			source.close();
			result.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return asm;
	}

}
