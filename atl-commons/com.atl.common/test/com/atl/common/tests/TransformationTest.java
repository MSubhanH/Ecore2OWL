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
package com.atl.common.tests;

import static com.atl.common.trans.Transformations.transform;

import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.engine.emfvm.ASM;
import org.junit.Test;

import com.atl.common.models.Models;
import com.atl.common.trans.ATLModel2ASM;
import com.atl.common.trans.Transformation;
import com.atl.common.trans.Transformations;

public class TransformationTest {

	@Test 
	public void HOTTest() {
		Resource model = Models.resource("test/l3i/sido/emf4sw/tests/models/calendar2rdf.atl.xmi");
		EMFModel m = Models.inject( model, Models.ATL_URI );

		ASM asm = transform(m, new ATLModel2ASM());

		assert asm != null;
	}

	@Test 
	public void testBuilder() {
		long start = System.currentTimeMillis();
		Transformation<Set<EMFModel>, EMFModel> t = new Transformations.Builder()
		.asm(TransformationTest.class.getResource("models/ecore2ecore.asm"))
		.in(Models.ecore(), "IN1", "ecore")
		.in(Models.ecore(), "IN2", "ecore")
		.out(Models.ecore(), "OUT", "ecore")
		.buildMultiInOneOut();

		Resource in1 = Models.resource("test/l3i/sido/emf4sw/tests/models/Calendar.ecore");
		Resource in2 = Models.resource("test/l3i/sido/emf4sw/tests/models/Calendar.ecore");
		Set<EMFModel> set = Models.setOf(Models.inject(in1, Models.ECORE_URI), Models.inject(in2, Models.ECORE_URI));


		EMFModel mo = transform(set, t);
		Models.extract(mo, System.out);
		System.out.println((System.currentTimeMillis() - start) / 1000.);
		mo = null;
		mo = transform(set, t);
		Models.extract(mo, System.out);
	}
}
