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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {

	public static InputStream addResource(String path, Class<?> c)
	{
		final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream rsrc = null;
		if ( contextClassLoader!=null ) {
			rsrc = contextClassLoader.getResourceAsStream( path );
		}
		if ( rsrc == null ) {
			rsrc = c.getClassLoader().getResourceAsStream( path );
		}
		if ( rsrc == null ) {
			URL url = null;
			try {
				url = new URL(path);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			if (url != null) {
				try {
					rsrc = url.openStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if ( rsrc == null ) {
			throw new NullPointerException( "Resource: " + path + " not found" );
		}
		return rsrc;
	}
	
}
