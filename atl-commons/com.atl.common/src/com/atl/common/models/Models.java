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

import static com.atl.common.trans.Transformations.transform;
import static com.atl.common.utils.Preconditions.checkNotNull;
import static org.eclipse.emf.ecore.util.EcoreUtil.getObjectsByType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;

import com.atl.common.trans.Transformation;

/**
 * Utility Class for Model manipulation.
 * 
 * <p>{@link Models} provides methods for creation of ATL specific models 
 * ({@link EMFModel}) from {@link Resource} objects.</p>
 * 
 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
 * @since 0.1
 */
public class Models {

	public static final String ECORE_URI = EcorePackage.eNS_URI;

	public static final String ATL_URI = ATLLoader.ATL_URI;

	public static final String PROBLEM_URI = ProblemLoader.PROBLEM_URI;

	private Models() {}

	static {
		ModelsFactory.register( EcorePackage.eINSTANCE.eResource() );
		ModelsFactory.register( ATLLoader.getAtlResource() );
		ModelsFactory.register( ProblemLoader.getProblemResource() );
	}

	public static EMFReferenceModel ecore() {
		return Models.get( ECORE_URI );
	}

	public static EMFReferenceModel atl() {
		return Models.get( ATL_URI );
	}

	public static EMFReferenceModel problem() {
		return Models.get( PROBLEM_URI );
	}

	/**
	 * Returns an empty {@link EMFModel} conforms to the metamodel given
	 * as parameter.
	 */
	public static EMFModel emptyModel(final Resource metamodel) {
		checkNotNull(metamodel);

		return ModelsFactory.newEMFModel( transform(metamodel, ModelsFactory.convert) );
	}

	/**
	 * Returns an empty {@link EMFModel} conforms to the metamodel given 
	 * as parameter.
	 */
	public static EMFModel emptyModel(final EMFReferenceModel metamodel) {
		checkNotNull(metamodel);

		return ModelsFactory.newEMFModel( metamodel );
	}

	/**
	 * Returns an empty {@link EMFModel} conforms to the metamodel given as 
	 * parameter. The {@link EMFModel} {@link Resource} is created using the given
	 * {@link Resource.Factory} implementation.
	 */
	public static EMFModel emptyModel(final EMFReferenceModel metamodel, Resource.Factory factory) {
		checkNotNull(metamodel);

		final Resource resource = factory.createResource(URI.createURI("tmp-model"));
		ModelsFactory.resourceSet.getResources().add( resource );
		final EMFModel model = emptyModel(metamodel);
		ModelsFactory.injector.inject(model, resource);

		return model;
	}

	/**
	 * Returns an empty {@link EMFModel} conforms to the Ecore metamodel.
	 */
	public static EMFModel emptyEcoreModel() {
		return emptyModel( ecore() );
	}

	/**
	 * Returns an empty {@link EMFModel} conforms to the ATL metamodel.
	 */
	public static EMFModel emptyAtlModel() {
		return emptyModel( ATLLoader.getAtlResource() );
	}

	/**
	 * Register the {@link Resource} given as parameter as a metamodel. The 
	 * {@link Resource} must have its URI set.
	 */
	public static void register(final Resource resource) {
		checkNotNull(resource, "Cannot register a null resource.");

		ModelsFactory.register( resource );
	}

	/**
	 * Register the {@link EPackage} in the current {@link ResourceSet}.
	 * @param ePackage
	 */
	public static void register(final EPackage ePackage) {
		checkNotNull(ePackage, "Cannot register a null resource.");

		ModelsFactory.register(ePackage.eResource());
	}

	/**
	 * Register the {@link EPackage} given as parameter as a metamodel.
	 * @param uri - use to register the EPackage
	 * @param ePackage - to be registered
	 */
	public static void register(final String uri, final EPackage ePackage) {
		if (ePackage.eResource() == null) {
			final Resource resource = Models.resource(uri);
			resource.getContents().add(ePackage);
		} else {
			ePackage.eResource().setURI(URI.createURI(uri));	
		}
		register( ePackage.eResource() );

		EPackage.Registry.INSTANCE.put(uri, ePackage);
	}

	/**
	 * Register a {@link Factory} according to a file extension.
	 * @param extension
	 * @param factory
	 */
	public static void factory(String extension, Factory factory) {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(extension, factory);
	}

	/**
	 * Returns a {@link EMFModel} from a {@link Resource} given as 
	 * parameter and set is {@link EMFReferenceModel} according to 
	 * the metamodelUri given as second parameter. The metamodelUri 
	 * is used to load a previously registered {@link Resource} using 
	 * the Models.register(Resource) method. 
	 */
	public static EMFModel inject(Resource model, String metamodelUri) {
		checkNotNull(model, "Cannot inject a null resource.");
		checkNotNull(metamodelUri);
		checkResourceInResourceSet( model );

		return inject( model, ModelsFactory.get(URI.createURI(metamodelUri)) );
	}

	/**
	 * Returns a {@link EMFModel} from a {@link Resource} given as 
	 * parameter and set is {@link EMFReferenceModel} according to 
	 * the second parameter.
	 */
	public static EMFModel inject(Resource model, EMFReferenceModel metamodel) {
		checkNotNull(model, "Cannot inject a null resource.");
		checkNotNull(metamodel);
		//		checkResourceInResourceSet( metamodel.getResource() );

		final EMFModel out = ModelsFactory.newEMFModel( metamodel );
		ModelsFactory.inject( out, model );

		return out;
	}

	/**
	 * Returns a {@link EMFModel} from a {@link Resource} given as 
	 * parameter and set is {@link EMFReferenceModel} according to 
	 * the {@link Resource} given as second parameter.
	 */
	public static EMFModel inject(Resource model, Resource metamodel) {
		checkNotNull(model);
		checkNotNull(metamodel);
		checkResourceInResourceSet( model );
		checkResourceInResourceSet( metamodel );

		if (!metamodel.isLoaded()) {
			try {
				metamodel.load(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		final EMFModel out = emptyModel( metamodel );
		ModelsFactory.inject(out, model);

		return out;
	}

	public static EMFModel inject(String modelPath, EMFReferenceModel metamodel) {
		checkNotNull(metamodel);

		final EMFModel model = emptyModel( metamodel );
		ModelsFactory.inject(model, modelPath);

		return model;
	}

	public static EMFModel injectAtlModel(Resource model) {
		checkNotNull(model, "Cannot inject a null resource.");

		return inject(model, ATLLoader.getAtlResource());
	}

	public static void extract(EMFModel model, String target) {
		checkNotNull(model);

		ModelsFactory.extract(model, target);
	}

	public static void extract(EMFModel model, OutputStream target) {
		checkNotNull(model);

		ModelsFactory.extract(model, target);
	}

	/**
	 * Returns the {@link ResourceSet} currently used.
	 */
	public static ResourceSet resourceSet() {
		return ModelsFactory.resourceSet;
	}

	/**
	 * Returns a {@link Resource} with a given uri.
	 */
	public static Resource resource(final String uri) {
		return resource(URI.createURI(uri));
	}

	public static Resource resource(final String uri, boolean load) {		
		return resource(URI.createURI(uri), load);
	}

	public static Resource resource(final URI uri) {
		return ModelsFactory.createResource( uri );
	}

	public static Resource resource(final URI uri, boolean load) {
		final Resource aResource = resource(uri);

		if (load && aResource != null) {
			try {
				aResource.load(aResource.getResourceSet().getLoadOptions());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return aResource;
	}

	public static void unload(EMFModel model) {
		ModelsFactory.factory.unload(model);
	}

	public static void unload(Resource resource) {
		if ( ModelsFactory.has(resource) ) {
			resource.getContents().clear();
			ModelsFactory.resourceSet.getResources().remove(resource);
		}
	}

	public static Resource getMetamodelByUri(String uri) {
		return get(uri) == null ? null : get(uri).getResource();
	}

	public static EMFReferenceModel get(String uri) {
		if ( EPackage.Registry.INSTANCE.getEPackage(uri) != null) {
			return transform( EPackage.Registry.INSTANCE.getEPackage(uri).eResource(), ModelsFactory.convert );
		} else {
			return null;
		}
	}

	public static Set<EMFModel> setOf(EMFModel... models) {
		final Set<EMFModel> set = new LinkedHashSet<EMFModel>();
		for (EMFModel model: models) {
			set.add(model);
		}
		return set;
	}

	private static void checkResourceInResourceSet(Resource model) {
		if (model.getResourceSet() == null || !ModelsFactory.has( model )) 
		{
			ModelsFactory.addToResourceSet( model );
		}
	}

	public static boolean conformsTo(final EMFModel model, final EMFReferenceModel metamodel) {
		return metamodel.equals( model.getReferenceModel() );
	}

	public static EMFModelFactory getModelFactory() {
		return ModelsFactory.factory;
	}

	public static EPackage ePackage(String packageNsURI) {
		final Resource res = get(packageNsURI).getResource();
		if (res == null) {
			throw new IllegalArgumentException("No EPackage with nsURI " + packageNsURI + " were registered");
		}
		if (res.getContents().isEmpty()) {
			return null;
		}
		final EObject eObject = res.getContents().get(0);

		return eObject instanceof EPackage ? (EPackage) eObject : null;
	}

	/**
	 * ModelsFactory manages a single instance of {@link EMFModelFactory} and  
	 * its associated {@link ResourceSet}. ModelsFactory provides methods for 
	 * creation, injection and extraction of {@link EMFModel}.
	 * 
	 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
	 */
	private static final class ModelsFactory {

		private static final EMFModelFactory factory;

		private static final ResourceSet resourceSet;

		private static final Map<Resource, EMFReferenceModel> metamodels;

		private static final EMFInjector injector;

		private static final EMFExtractor extractor;

		static {
			factory = new EMFModelFactory();
			resourceSet = factory.getResourceSet();
			injector = new EMFInjector();
			extractor = new EMFExtractor();
			metamodels = new HashMap<Resource, EMFReferenceModel>();

			resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
		}

		static EMFReferenceModel newReferenceModel() {
			return (EMFReferenceModel) factory.newReferenceModel();
		}

		public static void inject(EMFModel model, String modelPath) {
			try {
				injector.inject(model, modelPath);
			} catch (ATLCoreException e) {
				e.printStackTrace();
			}
		}

		public static Resource createResource(final URI uri) {
			return resourceSet.createResource(uri);
		}

		static void register(Resource resource) {
			registerPackages( getObjectsByType(resource.getContents(), 
					EcorePackage.eINSTANCE.getEPackage()) );
			
			ModelsFactory.addToResourceSet( resource );
		}


		static void registerPackages(Collection<Object> packages) {
			for (Object obj: packages) {
				if (obj instanceof EPackage) {
					if (!EPackage.Registry.INSTANCE.containsKey(((EPackage) obj).getNsURI())) {
						EPackage.Registry.INSTANCE.put(((EPackage) obj).getNsURI(), ((EPackage) obj));
					}
				}
			}
		}

		static void inject(EMFModel out, Resource model) {
			if (!model.isLoaded()) 
			{
				try {
					model.load(Collections.EMPTY_MAP);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			injector.inject(out, model);
		}

		static void extract(EMFModel model, String target) {
			try {
				extractor.extract(model, target);
			} catch (ATLCoreException e) {
				e.printStackTrace();
			}
		}

		static void extract(EMFModel model, OutputStream target) {
			try {
				extractor.extract(model, target, Collections.<String, Object> emptyMap());
			} catch (ATLCoreException e) {
				e.printStackTrace();
			}
		}

		static EMFModel newEMFModel(EMFReferenceModel metamodel) {
			return (EMFModel) factory.newModel( metamodel );
		}

		static boolean has(Resource resource) {
			return resourceSet.getResources().contains( resource );
		}

		static boolean addToResourceSet(Resource resource) {
			return resourceSet.getResources().add( resource );
		}

		static Resource get(URI uri) {
			return resourceSet.getResource(uri, true);
		}

		private static Transformation<Resource, EMFReferenceModel> convert =
			new Transformation<Resource, EMFReferenceModel>() {

			public EMFReferenceModel apply(Resource arg) 
			{
				if (metamodels.containsKey(arg)) {
					return metamodels.get( arg );
				} else {
					final EMFReferenceModel model = newReferenceModel();
					injector.inject(model, arg);
					metamodels.put(arg, model);
					return model;
				}
			};
		};

	}

	/**
	 * Utility class in charge of loading the ATL metamodel {@link Resource}. 
	 * 
	 * @author <a href=g.hillairet@gmail.com>guillaume hillairet</a>
	 */
	private static final class ATLLoader {

		private static final String ATL_URI = "http://www.eclipse.org/gmt/2005/ATL";

		private static Resource ATL_RESOURCE;

		private static EMFReferenceModel ATL_METAMODEL;

		static {
			try {
				ATL_METAMODEL = (EMFReferenceModel) ModelsFactory.factory.getBuiltInResource("ATL.ecore");
				ATL_RESOURCE = ATL_METAMODEL.getResource();
				ATL_RESOURCE.setURI( URI.createURI(ATL_URI) );
			} catch (ATLCoreException e) {
				e.printStackTrace();
			}
		}

		static Resource getAtlResource() {return ATL_RESOURCE; }

	}

	private static final class ProblemLoader {

		private static final String PROBLEM_URI = "http://www.eclipse.org/gmt/tcs/2007/PROBLEM";

		private static Resource PROBLEM_RESOURCE;

		private static EMFReferenceModel PROBLEM_METAMODEL;

		static {
			try {
				PROBLEM_METAMODEL = (EMFReferenceModel) ModelsFactory.factory.getBuiltInResource("Problem.ecore");
				PROBLEM_RESOURCE = PROBLEM_METAMODEL.getResource();
				PROBLEM_RESOURCE.setURI( URI.createURI(PROBLEM_URI) );
			} catch (ATLCoreException e) {
				e.printStackTrace();
			}
		}

		static Resource getProblemResource() {return PROBLEM_RESOURCE; }

	}

}
