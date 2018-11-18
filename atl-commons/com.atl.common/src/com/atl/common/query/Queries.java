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

import static com.atl.common.utils.Preconditions.checkArgument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.ASM;
import org.eclipse.m2m.atl.engine.emfvm.ASMXMLReader;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import com.atl.common.utils.ATLCompiler;

/**
 * Utility Class for ATL Queries.
 * 
 * <p>{@link Queries} provides methods for creation of ATL specific queries.
 * 
 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
 * @since 0.1
 */
public class Queries {

	private static <T extends Object> Query<T> create(final Builder builder) {
		return new Query<T>() {
			public T run() {
				return this.apply(builder.model);
			}

			@SuppressWarnings("unchecked")
			public T apply(EMFModel arg0) {
				final EMFVMLauncher launcher = new EMFVMLauncher();
				launcher.initialize(Collections.<String, Object> emptyMap());

				launcher.addInModel(arg0, "IN", builder.metamodelName);
				return (T) launcher.launch(
						ILauncher.RUN_MODE, 
						new NullProgressMonitor(), 
						Collections.<String, Object> emptyMap(),
						new Object[]{builder.asm}
				);
			}
		};
	}

	private static Query<List<EObject>> createList(final Builder builder) {
		return new Query<List<EObject>>() {
			public List<EObject> run() {
				return this.apply(builder.model);
			}

			@SuppressWarnings("unchecked")
			public List<EObject> apply(EMFModel arg0) {
				final EMFVMLauncher launcher = new EMFVMLauncher();
				launcher.initialize(Collections.<String, Object> emptyMap());

				launcher.addInModel(arg0, "IN", builder.metamodelName);
				Object ret = launcher.launch(
						ILauncher.RUN_MODE, 
						new NullProgressMonitor(), 
						Collections.<String, Object> emptyMap(),
						new Object[]{builder.asm}
				);
				if (ret instanceof List<?>) {
					return (List<EObject>) ret;
				} else {
					throw new ClassCastException("Query result cannot be cast to List<EObject>.");
				}
			}
		};
	}

	private static Query<Set<EObject>> createSet(final Builder builder) {
		return new Query<Set<EObject>>() {
			public Set<EObject> run() {
				return this.apply(builder.model);
			}

			@SuppressWarnings("unchecked")
			public Set<EObject> apply(EMFModel arg0) {
				final EMFVMLauncher launcher = new EMFVMLauncher();
				launcher.initialize(Collections.<String, Object> emptyMap());

				launcher.addInModel(arg0, "IN", builder.metamodelName);
				Object ret = launcher.launch(
						ILauncher.RUN_MODE, 
						new NullProgressMonitor(), 
						Collections.<String, Object> emptyMap(),
						new Object[]{builder.asm}
				);
				if (ret instanceof Set<?>) {
					return (Set<EObject>) ret;
				} else {
					throw new ClassCastException("Query result cannot be cast to Set<EObject>.");
				}
			}
		};
	}

	private static Query<EObject> createOne(final Builder builder) {
		return new Query<EObject>() {
			public EObject run() {
				return this.apply(builder.model);
			}

			public EObject apply(EMFModel arg0) {
				final EMFVMLauncher launcher = new EMFVMLauncher();
				launcher.initialize(Collections.<String, Object> emptyMap());

				launcher.addInModel(arg0, "IN", builder.metamodelName);
				Object ret = launcher.launch(
						ILauncher.RUN_MODE, 
						new NullProgressMonitor(), 
						Collections.<String, Object> emptyMap(),
						new Object[]{builder.asm}
				);
				if (ret instanceof EObject) {
					return (EObject) ret;
				} else {
					throw new ClassCastException("Query result cannot be cast to EObject.");
				}
			}
		};
	}

	public static class Builder {
		private EMFModel model;
		private String metamodelName;
		private String query = "query name = ";
		private ASM asm;

		public Builder() {}

		public Builder in(EMFModel model, String metamodelName) {
			this.model = model;
			this.metamodelName = metamodelName;
			return this;
		}

		public Builder asm(URL url) {
			asm = readASMFromURL(url);
			return this;
		}

		public Builder query(String query) {
			this.query += query + ";" ;
			return this;
		}

		public <T extends Object> Query<T> create() {
			init();

			return Queries.create(this);
		}

		public Query<List<EObject>> list() {
			init();

			return Queries.createList(this);
		}

		public Query<Set<EObject>> set() {
			init();

			return Queries.createSet(this);
		}

		public Query<EObject> one() {
			init();

			return Queries.createOne(this);
		}

		private ASM readASMFromURL(URL url) 
		{
			try {
				return new ASMXMLReader().read( url.openStream() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private void init() {
			//			checkArgument(this.query.endsWith(";"), "Cannot create Query, malformed expression.");
			checkArgument(this.model != null, "Cannot create Query, input model not set.");
			checkArgument(this.metamodelName != null, "Cannot create Query, metamodel name not set.");

			if (asm == null && query != null) {
				EObject[] pbs = null;
				final OutputStream result = new ByteArrayOutputStream();
				try {
					pbs = ATLCompiler.compile(new ByteArrayInputStream(this.query.getBytes()), result);
					if (pbs.length == 0) {
						asm = new ASMXMLReader().read(new ByteArrayInputStream(result.toString().getBytes()));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
