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
package com.atl.common.models;

import static com.atl.common.models.Models.get;
import static com.atl.common.models.Models.register;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.emfvm.ASM;
import org.eclipse.m2m.atl.engine.emfvm.ASMXMLReader;

/**
 * 
 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
 * @since 0.6
 */
public class Properties<K, V> {
	private static final String PROPERTIES_NS_URI = "http://com.atl.commons/2010/Properties";
	private final Map<K, V> propertyMap = new HashMap<K, V>();
	private final EObject aProperty;
	private final static EPackage aPackage;
		
	protected Properties() {
		aProperty = aPackage.getEFactoryInstance().create((EClass) aPackage.getEClassifier("PropertyMap"));
	}
	
	static {
		final Resource aResource = new XMIResourceImpl(URI.createURI(PROPERTIES_NS_URI));
		try {
			aResource.load(Properties.class.getResourceAsStream("Properties.ecore"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		register(aResource);
		aPackage = (EPackage) EcoreUtil.getObjectByType(
				get(PROPERTIES_NS_URI).getResource().getContents(), 
				EcorePackage.eINSTANCE.getEPackage());
	}
	
	public static Properties<String, String> createProperties() {
		return new Properties<String, String>();
	}
	
	public static <K, V> Properties<K, V> createProperties(Class<K> key, Class<V> value) {
		return new Properties<K, V>();
	}
	
	public static <K, V> Properties<K, V> createProperties(Map<K, V> options) {
		System.out.println("Aaa");
		final Properties<K, V> properties = new Properties<K, V>();
		properties.propertyMap.putAll(options);
		return properties;
	}
	
	public Properties<K, V> add(K key, V value) {
		if (propertyMap.containsKey(key)) {
			propertyMap.remove(key);
		}
		propertyMap.put(key, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public Resource serialize() {
	
		final Resource aResource = new XMIResourceImpl();
		final EClass ePropertyMap = (EClass) aPackage.getEClassifier("PropertyMap");
		final EReference eEntries = (EReference) ePropertyMap.getEStructuralFeature("entries");
		
		final EClass eEntry = (EClass) aPackage.getEClassifier("Entry");
		final EAttribute eKey = (EAttribute) eEntry.getEStructuralFeature("key");
		final EAttribute eValue = (EAttribute) eEntry.getEStructuralFeature("value");

		for (K key: propertyMap.keySet()) {
			EObject aEntry = aPackage.getEFactoryInstance().create(eEntry);
			
			aEntry.eUnset(eKey);
			aEntry.eSet(eKey, key);
			aEntry.eSet(eValue, propertyMap.get(key));

			((EList<Object>)aProperty.eGet(eEntries)).add(aEntry);
		}
		
		aResource.getContents().add(aProperty);
		return aResource;
	}
	
	public static ASM getHelpers() {
		return new ASMXMLReader().read(Properties.class.getResourceAsStream("PropertiesHelpers.asm"));
	}

	public static EMFReferenceModel getReferenceModel() {
		return get(PROPERTIES_NS_URI);
	}
}
