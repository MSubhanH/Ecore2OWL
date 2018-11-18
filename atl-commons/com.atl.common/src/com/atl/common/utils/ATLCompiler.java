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
package com.atl.common.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.m2m.atl.engine.compiler.atl2006.Atl2006Compiler;

public class ATLCompiler {
	private static final int MAX_LINE_LENGTH = 1000;

	public static EObject[] compile(InputStream in, OutputStream outputStream) throws IOException {
		EObject[] ret = null;
		InputStream newIn = in;
		// The BufferedInputStream is required to reset the stream before actually compiling
		newIn = new BufferedInputStream(newIn, MAX_LINE_LENGTH);
		newIn.mark(MAX_LINE_LENGTH);
		byte[] buffer = new byte[MAX_LINE_LENGTH];
		newIn.read(buffer);
		//		atlcompiler = AtlSourceManager.getCompilerName(AtlSourceManager.getTaggedInformations(buffer,
		//				AtlSourceManager.COMPILER_TAG));
		newIn.reset();

		ret = new Atl2006Compiler().compileWithProblemModel(newIn, outputStream);
		return ret;
	}
}
