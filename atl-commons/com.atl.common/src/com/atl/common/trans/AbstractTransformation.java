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

import static com.atl.common.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.ASM;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.1
 */
public abstract class AbstractTransformation<F, T> implements Transformation<F, T>{

	protected final EMFVMLauncher launcher;

	protected final Object[] asm;

	protected final String name;

	public AbstractTransformation(String name, ASM... asm) {
		checkNotNull( asm );

		this.name = name;
		this.asm = asm;
		launcher = new EMFVMLauncher();
	}

	public AbstractTransformation(String name, URL... asm) {
		checkNotNull( asm );

		this.name = name;
		this.asm = openStreams( asm );
		launcher = new EMFVMLauncher();
	}

	public abstract T apply(F arg0);

	protected EMFVMLauncher launch(Map<String, Object> options) {
		launcher.launch(
				ILauncher.RUN_MODE, 
				new NullProgressMonitor(), 
				options,
				asm );

		return launcher;
	}

	private Object[] openStreams(URL[] urls) {
		final Object[] streams = new InputStream[urls.length];
		for (int i = 0; i < urls.length; i++) {
			try {
				streams[i] = urls[i].openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return streams;
	}

}
