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
package com.atl.common.trans;

import static com.atl.common.utils.Preconditions.checkArgument;
import static com.atl.common.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.ASM;
import org.eclipse.m2m.atl.engine.emfvm.ASMXMLReader;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import com.atl.common.utils.FileUtils;
import com.atl.common.utils.Parameters;

/**
 * {@link Transformations} contains static factory methods for creating {@link Transformation} 
 * instnaces.
 * <p>{@link Transformation} are created using a {@link Builder} pattern. The {@link Builder} class 
 * provides methods for the creation of four kinds of Transformation.
 * </p>
 * <p>{@link Transformations} provides the static method transform for launching a transformation. The 
 * argument and return type of this method depends on the type of the transformation taken as input. 
 * </p>
 * <p>{@link Transformations} provides the static method compose for realizing a composition of 
 * transformation. Similar to composition of Functions, the compose method makes 
 * possible the chaining of Transformation according to their input and output parameters.
 * </p>
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.1
 */
public class Transformations {

	private Transformations() {}

	public static <F, T> T transform(F arg, Transformation<F, T> transformation) {
		return transformation.apply( arg );
		
	}

	/**
	 * Returns the composition of 2 transformations. Can be used 
	 * to chain transformations.
	 * <p>
	 * T3: A -> C <- T1: A -> B + T2: B -> C
	 * </p> 
	 */
	public static <A, B, C> Transformation<A, C> compose(Transformation<B, C> g, Transformation<A, ? extends B> f) {
		return new TransformationComposition<A, B, C>(g, f);
	}

	private static class TransformationComposition<A, B, C> implements Transformation<A, C>, Serializable 
	{
		private final Transformation<B, C> g;
		private final Transformation<A, ? extends B> f;

		public TransformationComposition(Transformation<B, C> g, Transformation<A, ? extends B> f) {
			this.g = checkNotNull(g);
			this.f = checkNotNull(f);
		}

		public C apply(A a) {
			return g.apply(f.apply(a));
		}

		@Override public boolean equals(Object obj) {
			if (obj instanceof TransformationComposition<?, ?, ?>) {
				TransformationComposition<?, ?, ?> that = (TransformationComposition<?, ?, ?>) obj;
				return f.equals(that.f) && g.equals(that.g);
			}
			return false;
		}

		@Override public int hashCode() {
			return f.hashCode() ^ g.hashCode();
		}

		@Override public String toString() {
			return g.toString() + "(" + f.toString() + ")";
		}

		private static final long serialVersionUID = 0;
	}

	private static OneInOneOutTransformation createOneInOneOut(final Builder builder) {
		final Map<String, Object> options = builder.options == null ? 
				Collections.<String, Object> emptyMap() 
				: builder.options;
				final Parameters ins = builder.ins;
				final Parameters outs = builder.outs;
			
				return new OneInOneOutTransformation() {

					public EMFModel apply(EMFModel arg) {
						final ILauncher launcher = builder.getLauncher();
						launcher.initialize(Collections.<String, Object> emptyMap());
						ins.initInput(arg, launcher);
						EMFModel model = outs.initOneOutput(launcher);

						for (String lib: builder.libs.keySet()) 
						{
							launcher.addLibrary(lib, builder.libs.get(lib));
						}

						launcher.launch(
								ILauncher.RUN_MODE, 
								new NullProgressMonitor(), 
								options,
								(Object[]) builder.asms );

						return model;
					}

				};
	}

	private static OneInMultiOutTransformation createOneInMultiOut(final Builder builder) {
		final Map<String, Object> options = builder.options == null ? 
				Collections.<String, Object> emptyMap() 
				: builder.options;
				final Parameters ins = builder.ins;
				final Parameters outs = builder.outs;

				return new OneInMultiOutTransformation() {

					public Set<EMFModel> apply(EMFModel arg) {
						final ILauncher launcher = builder.getLauncher();
						launcher.initialize(Collections.<String, Object> emptyMap());
						ins.initInput(arg, launcher);
						Set<EMFModel> models = outs.initOutput(launcher);

						for (String lib: builder.libs.keySet()) 
						{
							launcher.addLibrary(lib, builder.libs.get(lib));
						}

						launcher.launch(
								ILauncher.RUN_MODE, 
								new NullProgressMonitor(), 
								options,
								(Object[]) builder.asms );

						return models;
					}
				};
	}

	private static MultiInOneOutTransformation createMultiInOneOut(final Builder builder) {
		final Map<String, Object> options = builder.options == null ?
				Collections.<String, Object> emptyMap() 
				: builder.options;
				final Parameters ins = builder.ins;
				final Parameters outs = builder.outs;

				return new MultiInOneOutTransformation() {

					public EMFModel apply(Set<EMFModel> arg) {
						final ILauncher launcher = builder.getLauncher();
						launcher.initialize(Collections.<String, Object> emptyMap());
						ins.initInput(arg, launcher);
						EMFModel model = outs.initOneOutput(launcher);

						for (String lib: builder.libs.keySet()) 
						{
							launcher.addLibrary(lib, builder.libs.get(lib));			
						}

						launcher.launch(
								ILauncher.RUN_MODE, 
								new NullProgressMonitor(), 
								options,
								(Object[]) builder.asms );

						return model;
					}

				};
	}

	// M1:MMa, M2:MMa ? 
	// arg = Set(M1, M2) ; ins = Map({M1, MMa}, {M2:MMa})
	private static MultiInMultiOutTransformation createMutiInMultiOut(final Builder builder) {
		final Map<String, Object> options = builder.options == null ? 
				Collections.<String, Object> emptyMap() 
				:  builder.options;
				final Parameters ins = builder.ins;
				final Parameters outs = builder.outs;

				return new MultiInMultiOutTransformation() {

					public Set<EMFModel> apply(Set<EMFModel> arg) {
						final ILauncher launcher = builder.getLauncher();
						launcher.initialize(Collections.<String, Object> emptyMap());
						ins.initInput(arg, launcher);
						Set<EMFModel> models = outs.initOutput(launcher);

						for (String lib: builder.libs.keySet()) 
						{
							launcher.addLibrary(lib, builder.libs.get(lib));
						}

						launcher.launch(
								ILauncher.RUN_MODE, 
								new NullProgressMonitor(), 
								options,
								(Object[])builder.asms );

						return models;
					}

				};

	}

	/**
	 * {@link Builder} for {@link Transformation}.
	 * 
	 * @author ghilla01
	 */
	public static class Builder {		
		private ASM[] asms;
		private Map<String, ASM> libs;
		private Parameters ins;
		private Parameters outs;

		private Map<String, Object> options;
		private ILauncher launcher;

		public Builder() {
			ins = new Parameters();
			outs = new Parameters();
			libs = new HashMap<String, ASM>();
		}

		public ILauncher getLauncher() {
			if (launcher == null) {
				launcher = new EMFVMLauncher();
			}
			return launcher;
		}

		/**
		 * Returns a {@link OneInOneOutTransformation}. This kind of 
		 * transformation takes one {@link EMFModel} as input and returns 
		 * one {@link EMFModel}.
		 */
		public OneInOneOutTransformation buildOneInOneOut() {
			checkNotNull( this.asms, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.asms.length > 0, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.ins.getSize() == 1, "Cannot Build Transformation, input metamodel are missing." );
			checkArgument( this.outs.getSize() == 1, "Cannot Build Transformation, output metamodel are missing." );

			return Transformations.createOneInOneOut( this );
		}

		/**
		 * Returns a {@link OneInMultiOutTransformation}. This kind of 
		 * transformation takes one {@link EMFModel} as input and returns 
		 * a {@link Set} of {@link EMFModel}.
		 */
		public OneInMultiOutTransformation buildOneInMultiOut() {
			checkNotNull( this.asms, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.asms.length > 0, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.ins.getSize() == 1, "Cannot Build Transformation, input metamodel are missing." );
			checkArgument( this.outs.getSize() >= 1, "Cannot Build Transformation, output metamodels are missing." );

			return Transformations.createOneInMultiOut( this );
		}

		/**
		 * Returns a {@link MultiInOneOutTransformation}. This kind of 
		 * transformation takes a {@link Set} of {@link EMFModel} as input 
		 * and returns one {@link EMFModel}.
		 */
		public MultiInOneOutTransformation buildMultiInOneOut() {
			checkNotNull( this.asms, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.asms.length > 0, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.ins.getSize() >= 1, "Cannot Build Transformation, input metamodels are missing." );
			checkArgument( this.outs.getSize() == 1, "Cannot Build Transformation, output metamodel are missing." );

			return Transformations.createMultiInOneOut( this );	    
		}

		/**
		 * Returns a {@link MultiInMultiOutTransformation}. This kind of 
		 * transformation takes a {@link Set} of {@link EMFModel} as input 
		 * and returns a {@link Set} of {@link EMFModel}.
		 */
		public MultiInMultiOutTransformation buildMultiInMultiOut() {
			checkNotNull( this.asms, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.asms.length > 0, "Cannot Build Transformation, asm file(s) are missing." );
			checkArgument( this.ins.getSize() >= 1, "Cannot Build Transformation, input metamodels are missing." );
			checkArgument( this.outs.getSize() >= 1, "Cannot Build Transformation, output metamodels are missing." );

			return Transformations.createMutiInMultiOut( this );
		}

		/**
		 * Method used to set up an input parameter of one {@link Transformation}.
		 * If transformation has for input IN:Ecore then method should be called 
		 * as {@code in(Models.ecore(), "IN", "Ecore")}.
		 * 
		 */
		public Builder in(EMFReferenceModel metamodel, String modelName, String metamodelName) { 	
			ins.add(modelName, metamodelName, metamodel);
			return this; 
		}

		public Builder in(EMFReferenceModel metamodel, String modelName, String metamodelName, Resource inResource) {
			ins.add(modelName, metamodelName, metamodel, inResource);
			return this;
		}
		
		/**
		 * Method used to set up an output parameter of one {@link Transformation}.
		 * If transformation has for input OUT:Ecore then method should be called 
		 * as {@code out(Models.ecore(), "OUT", "Ecore")}. 
		 *
		 */
		public Builder out(EMFReferenceModel metamodel, String modelName, String metamodelName) {
			outs.add(modelName, metamodelName, metamodel);
			return this;
		}
		
		/**
		 * Method used to set up an output parameter of one {@link Transformation}.
		 * If transformation has for input OUT:Ecore then method should be called 
		 * as {@code out(Models.ecore(), "OUT", "Ecore")}. 
		 *
		 */
		public Builder out(EMFReferenceModel metamodel, String modelName, String metamodelName, Resource.Factory factory) {
			outs.add(modelName, metamodelName, metamodel, factory);
			return this;
		}

		public Builder out(EMFReferenceModel metamodel, String modelName, String metamodelName, Resource aResource) {
			outs.add(modelName, metamodelName, metamodel, aResource);
			return this;
		}

		public Builder options(Map<String, Object> options) 
		{ this.options = options;
		return this; }

		public Builder launcher(ILauncher launcher) { 
			this.launcher = launcher;
			return this; 
		}
		
		/**
		 * Method used to set up the location of the asm file(s) containing the ATL 
		 * transformation.
		 */
		public Builder asm(URI... url)
		{ this.asms = readASMFromURI( url ); return this; }
		
		public Builder asm(URL... url) 
		{ this.asms = readASMFromURL( url ); return this; }

		public Builder asm(ASM... asm) 
		{ this.asms = asm; return this; }

		public Builder asm(String... path)
		{ this.asms = readASMFromPath(path); return this; }

		public Builder lib(String name, URL url) 
		{ this.libs.put(name, readASMFromURL( url )); return this; }

		public Builder lib(String name, String path) 
		{ this.libs.put(name, readASMFromPath( path )); return this; }

		public Builder lib(String name, ASM asm) 
		{ this.libs.put(name, asm); return this; }
		
		private ASM[] readASMFromURL(URL[] urls) {
			ASM[] asms = new ASM[urls.length];
			for (int i = 0; i < urls.length; i++)
			{
				try {
					final InputStream in = urls[i].openStream();
					try {
						asms[i] = new ASMXMLReader().read( in );
					} finally {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return asms;
		}

		private ASM readASMFromURL(URL url) {
			ASM asm = null;
			try {
				final InputStream stream = url.openStream();
				try {
					asm = new ASMXMLReader().read( stream );
				} finally {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return asm;
		}

		private ASM[] readASMFromPath(String[] paths) {
			final ASM[] asms = new ASM[paths.length];
			for (int i = 0; i < paths.length; i++)
			{
				try {
					final InputStream in = FileUtils.addResource(paths[i], this.getClass());
					try {
						if (in != null) {
							asms[i] = new ASMXMLReader().read( in );
						}
					} finally {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return asms;
		}

		private ASM readASMFromPath(String path) {
			ASM asm = null;
			try {
				final InputStream in = FileUtils.addResource(path, this.getClass());
				try {
					if (in != null) {
						asm = new ASMXMLReader().read( in );
					}
				} finally {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return asm;
		}

		private ASM[] readASMFromURI(URI[] uris) {
			ASM[] asms = new ASM[uris.length];
			for (int i = 0; i < uris.length; i++)
			{
				try {
					final URL url = new URL(uris[i].toFileString());
					
					final InputStream in = url.openStream();
					try {
						asms[i] = new ASMXMLReader().read( in );
					} finally {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return asms;
		}

	}

}

