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
package com.atl.common.models.resource;

import static com.atl.common.models.Models.atl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

/**
 * 
 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
 * @since 0.6
 */
public class ATLResource extends ResourceImpl implements Resource {

	private static final AtlParser parser = AtlParser.getDefault();
	
	public ATLResource(URI uri) {
		super(uri);
	}
	
	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		EMFModel model = null;
		try {
			model = (EMFModel) parser.getModelFactory().newModel(atl());
			parser.inject(model, inputStream, options);
		} catch (ATLCoreException e) {
			e.printStackTrace();
		}
		
		if (model != null && model.getResource() != null) {
			this.getContents().addAll(model.getResource().getContents());
		}
	}
	
	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		EMFModel model = null;
		try {
			model = (EMFModel) parser.getModelFactory().newModel(atl());
		} catch (ATLCoreException e1) {
			e1.printStackTrace();
		}
		
		if (model != null) {
			new EMFInjector().inject(model, this);
			try {
				parser.extract(model, outputStream, options);
			} catch (ATLCoreException e) {
				e.printStackTrace();
			}
		}		
	}
}
