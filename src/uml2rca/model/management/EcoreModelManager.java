package uml2rca.model.management;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.XMLMap;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import core.model.management.AbstractStatefulModelManager;
import core.model.management.NotAValidModelStateException;

public class EcoreModelManager extends AbstractStatefulModelManager<Model, String> {
	
	/* STATIC ATTRIBUTES */
	public static Package UML_PRIMITIVE_TYPES_LIBRARY; // the UML library of primitive types
	
	static {
		UML_PRIMITIVE_TYPES_LIBRARY = loadPackage(
				URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
	}

	/* CONSTRUCTORS */
	public EcoreModelManager(String path) 
			throws InstantiationException, IllegalAccessException, 
			NotAValidModelStateException {
		
		super(path);
		importAndLoadState(path, "Initial state", EcoreModelState.class);
	}
	
	/* METHODS */	
	private void registerUMLResourceFactoryForUMLExtension() {
		Resource.Factory.Registry.INSTANCE
		.getExtensionToFactoryMap()
		.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
	}
	
	private ResourceSet createModelResourceSet() {
		// Create a resource set
		ResourceSet resourceSet = new ResourceSetImpl();
		
		// Register the UMLPackage in this resource set's package registry
		resourceSet.getPackageRegistry()
		.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		
		/*
		 * Register the UML Resource Factory for the UML Resource extension
		 * in this resource set
		 */
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		
		return resourceSet;
	}
	
	@Override
	public Model importModel(String path) {
		
		// Create the URI for the model element to load
		URI uri = URI.createURI(path);
		
		// Register the UML Resource Factory for the UML resource extension
		registerUMLResourceFactoryForUMLExtension();
		
		// Obtain a resource set
		ResourceSet resourceSet = createModelResourceSet();
		
		// Create a resource for the designated URI
		Resource resource = resourceSet.createResource(uri);
		
		XMLResource.XMLMap xmlMap = new XMLMapImpl();
		xmlMap.setNoNamespacePackage(UMLPackage.eINSTANCE);
		Map<String, XMLMap> options = new HashMap<>();
		options.put(XMLResource.OPTION_XML_MAP, xmlMap);
	   
		try {
			resource.load(options);
	   } catch(Exception e) {
		   
		   System.err.println("Error: cannot load model from " + path + ": " + e);
		   e.printStackTrace();
		   return null;
	   }
		
		return (Model) resource.getContents().get(0);
	}
	
	@Override
	public boolean exportModel(Model model, String path) {
		
		// Create the URI for the model element to save
		URI uri = URI.createURI(path);
		
		// Register the UML Resource Factory for the UML resource extension
		registerUMLResourceFactoryForUMLExtension();
		
		// Obtain a resource set
		ResourceSet resourceSet = createModelResourceSet();
		
		// Create a resource for the designated URI
		Resource resource = resourceSet.createResource(uri);
		
		// Add the model to the resource
		resource.getContents().add(model);
		
		// Persist the contents of the resource
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			
			System.err.println("Error: cannot save model at " + path + ": " + e);
			e.printStackTrace();
			return false;
	   }
		
		return true;
	}

	/* STATIC METHODS */
	public static Package loadPackage(URI uri) {
		
		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);
		
		Package package_ = UMLFactory.eINSTANCE.createPackage();
		Resource resource = (resourceSet).getResource(uri, true);
		
		package_ = (Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		
		return package_;
	}
}