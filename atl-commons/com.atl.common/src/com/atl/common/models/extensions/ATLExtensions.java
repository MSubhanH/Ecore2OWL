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
package com.atl.common.models.extensions;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.m2m.atl.engine.emfvm.lib.AbstractStackFrame;
import org.eclipse.m2m.atl.engine.emfvm.lib.ExecEnv;
import org.eclipse.m2m.atl.engine.emfvm.lib.LibExtension;
import org.eclipse.m2m.atl.engine.emfvm.lib.Operation;

import com.atl.common.models.Models;

/**
 * 
 *	@author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * 	@since 0.7.0
 */
public class ATLExtensions implements LibExtension {

	@Override
	public void apply(ExecEnv execEnv, Map<String, Object> options) {
		final Operation resolveOperation = new Operation(1, "resolve") {  
			@Override
			public Object exec(AbstractStackFrame frame) {
				Object[] localVars = frame.getLocalVars();
				if (localVars[0] instanceof EObject) {
					EObject obj  = (EObject) localVars[0];
					return EcoreUtil.resolve(obj, Models.resourceSet());
				}
				return null;
			}
		};
		execEnv.registerOperation(Object.class, resolveOperation);
		
		Operation inverse = new Operation(1, "inverse") {  
			@SuppressWarnings("unchecked")
			@Override
			public Collection<Object> exec(AbstractStackFrame frame) {
				Object[] localVars = frame.getLocalVars();
				if (localVars[0] instanceof Collection) {
					if ( ((Collection<Object>) localVars[0]).size() > 1) {
						try {
							Collection<Object> col = (Collection<Object>) localVars[0].getClass().newInstance();
							Object[] array = ((Collection<?>)localVars[0]).toArray();
							
							int i=0;
							while (i < array.length) {
								col.add(array[array.length - 1 - i]);
								i++;
							}
							return col;
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}

					return (Collection<Object>) localVars[0];	
				} else return null;
			}
		};
		execEnv.registerOperation(Collection.class, inverse);
	}

}
