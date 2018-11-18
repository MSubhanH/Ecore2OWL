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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;

import com.atl.common.models.Models;

public class Parameters {
	Parameter[] params;
	int arraySize = 10;
	int size = 0;
	
	public Parameters() {
		params = new Parameter[arraySize];
	}

	public void add(String model, String metamodel, EMFReferenceModel eModel) 
	{
		params[size] = new Parameter(model, metamodel, eModel);
		size += 1;
	}

	public void add(String model, String metamodel, EMFReferenceModel eModel, Factory factory) {
		params[size] = new Parameter(model, metamodel, eModel, factory);
		size += 1;
	}
	
	public void add(String model, String metamodel, EMFReferenceModel eModel, Resource aResource) {
		params[size] = new Parameter(model, metamodel, eModel, aResource);
		size += 1;
	}
	
	public void initInput(EMFModel one, final ILauncher launcher)
	{
		launcher.addInModel(one, params[0].modelName, params[0].metamodelName);
	}

	public void initInput(Set<EMFModel> set, final ILauncher launcher)
	{
		final EMFModel[] models = set.toArray(new EMFModel[set.size()]);
		for (int i=0; i < size; i++) 
		{
			launcher.addInModel(models[i], params[i].modelName, params[i].metamodelName);
		}
	}

	public Set<EMFModel> initOutput(final ILauncher launcher)
	{
		final HashSet<EMFModel> out = new HashSet<EMFModel>();
		for (int i=0; i < size; i++)
		{
			final Parameter parameter = params[i];
			final EMFModel model;
			if (parameter.factory != null) {
				model = Models.emptyModel(parameter.metamodel, parameter.factory);
			} 
			else if (parameter.resource != null) {
				model = Models.inject(parameter.resource, parameter.metamodel);
			}
			else {
				model = Models.emptyModel(parameter.metamodel);
			}
			launcher.addOutModel(model, parameter.modelName, parameter.metamodelName);
			out.add(model);
		}
		return out;
	}

	public EMFModel initOneOutput(final ILauncher launcher)
	{
		final Parameter parameter = params[0];
		final EMFModel model;
		if (parameter.factory != null) {
			model = Models.emptyModel(parameter.metamodel, parameter.factory);
		}
		else if (parameter.resource != null) {
			model = Models.inject(parameter.resource, parameter.metamodel);
		}
		else {
			model = Models.emptyModel(parameter.metamodel);
		}
		launcher.addOutModel(model, parameter.modelName, parameter.metamodelName);
		

		return model;
	}

	private class Parameter {
		private String modelName;
		private String metamodelName;
		private EMFReferenceModel metamodel;
		private Resource.Factory factory;
		private Resource resource;

		Parameter(String modelName, String metamodelName, EMFReferenceModel metamodel) 
		{
			this.modelName = modelName;
			this.metamodelName = metamodelName;
			this.metamodel = metamodel;
		}

		Parameter(String modelName, String metamodelName, EMFReferenceModel metamodel, Factory factory) {
			this.modelName = modelName;
			this.metamodelName = metamodelName;
			this.metamodel = metamodel;
			this.factory = factory;
		}

		public Parameter(String modelName, String metamodelName, EMFReferenceModel metamodel, Resource aResource) {
			this.modelName = modelName;
			this.metamodelName = metamodelName;
			this.metamodel = metamodel;
			this.resource = aResource;
		}
	}

	public int getSize() {
		return size;
	}
	
}
