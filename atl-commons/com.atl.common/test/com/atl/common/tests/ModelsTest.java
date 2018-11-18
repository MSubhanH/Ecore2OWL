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

import static com.atl.common.models.Models.factory;
import static com.atl.common.models.Models.get;
import static com.atl.common.models.Models.inject;
import static com.atl.common.models.Models.register;
import static com.atl.common.models.Models.resource;
import static com.atl.common.trans.Transformations.transform;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.junit.Test;

import com.atl.common.models.Models;
import com.atl.common.trans.OneInOneOutTransformation;
import com.atl.common.trans.Transformations;

public class ModelsTest {

	@Test 
	public void testInjectFromEcoreResource() {
		URI uri = URI.createURI("test/com/atl/common/tests/models/Calendar.ecore");

		EMFModel Calendar = Models.inject(new XMIResourceImpl(uri), EcorePackage.eINSTANCE.eResource());

		assertTrue( Calendar != null );
		assertTrue( Calendar.getResource() != null );
		assertTrue( Calendar.getResource().getContents().get(0) != null );
		assertTrue( Calendar.getResource().getContents().get(0) instanceof EPackage );
	}

	@Test 
	public void testInjectFromEcoreUri() {
		URI uri = URI.createURI("test/com/atl/common/tests/models/Calendar.ecore");

		EMFModel Calendar = Models.inject(new XMIResourceImpl(uri), Models.ECORE_URI);

		assertTrue( Calendar != null );
		assertTrue( Calendar.getResource() != null );
		assertTrue( Calendar.getResource().getContents().get(0) != null );
		assertTrue( Calendar.getResource().getContents().get(0) instanceof EPackage );
	}
	
	@Test
	public void testRegisterAndLoad() {
		register(resource("test/com/atl/common/tests/models/Calendar.ecore", true));
		
		EMFModel model = get("http://Calendar/0.1/");
		
		assertTrue(model != null);
	}
	
	@Test 
	public void testBook() throws IOException {
		factory("xml", new XMLResourceFactoryImpl());
		
		register(resource("test/com/atl/common/tests/models/Book.ecore", true));
		register(resource("test/com/atl/common/tests/models/Publication.ecore", true));

		Resource model = resource("test/com/atl/common/tests/models/inputModelXML.xml", true);
		
		assertTrue(model != null);
		
		OneInOneOutTransformation trans = new Transformations.Builder()
		.asm("com/atl/common/tests/models/Book2Publication.asm")
		.in(get("http://ex.org/Book"), "IN", "Book")
		.out(get("http://ex.org/Publication"), "OUT", "Publication")
		.buildOneInOneOut();
		
		EMFModel out = transform(inject(model, get("http://ex.org/Book")), trans);
		
		Resource resource = resource("test/com/atl/common/tests/models/out.xml");
		resource.getContents().addAll(out.getResource().getContents());
		resource.save(null);
	}

}
