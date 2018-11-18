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
package com.atl.common.query;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import com.atl.common.trans.Transformation;

/**
 * Query interface.
 * 
 * <p>{@link Query} is a special kind of {@link Transformation} that returns 
 * a single Object or a Collection of Object.  
 * 
 * 
 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
 * @since 0.1
 */
public interface Query<T extends Object> extends Transformation<EMFModel, T> {
	
	public T run();
	
}
