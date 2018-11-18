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

import org.eclipse.emf.ecore.resource.Resource;

public class Preconditions {
	  private Preconditions() {}

	  /**
	   * Ensures the truth of an expression involving one or more parameters to the
	   * calling method.
	   *
	   * @param expression a boolean expression
	   * @throws IllegalArgumentException if {@code expression} is false
	   */
	  public static void checkArgument(boolean expression) {
	    if (!expression) {
	      throw new IllegalArgumentException();
	    }
	  }

	  /**
	   * Ensures the truth of an expression involving one or more parameters to the
	   * calling method.
	   *
	   * @param expression a boolean expression
	   * @param errorMessage the exception message to use if the check fails; will
	   *     be converted to a string using {@link String#valueOf(Object)}
	   * @throws IllegalArgumentException if {@code expression} is false
	   */
	  public static void checkArgument(boolean expression, Object errorMessage) {
	    if (!expression) {
	      throw new IllegalArgumentException(String.valueOf(errorMessage));
	    }
	  }

	  /**
	   * Ensures that an object reference passed as a parameter to the calling
	   * method is not null.
	   *
	   * @param reference an object reference
	   * @return the non-null reference that was validated
	   * @throws NullPointerException if {@code reference} is null
	   */
	  public static <T> T checkNotNull(T reference) {
	    if (reference == null) {
	      throw new NullPointerException();
	    }
	    return reference;
	  }

	  /**
	   * Ensures that an object reference passed as a parameter to the calling
	   * method is not null.
	   *
	   * @param reference an object reference
	   * @param errorMessage the exception message to use if the check fails; will
	   *     be converted to a string using {@link String#valueOf(Object)}
	   * @return the non-null reference that was validated
	   * @throws NullPointerException if {@code reference} is null
	   */
	  public static <T> T checkNotNull(T reference, Object errorMessage) {
	    if (reference == null) {
	      throw new NullPointerException(String.valueOf(errorMessage));
	    }
	    return reference;
	  }
	  
	  public static void checkIsLoaded(Resource resource) {
		  if (!resource.isLoaded()) {
			  try {
				resource.load(resource.getResourceSet().getLoadOptions());
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	  }
}
