package rca.management;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.XMLMap;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

import core.management.AbstractModelManager;

public class EcoreModelManager extends AbstractModelManager<EObject> {

	@Override
	public boolean save(EObject modelElement, String stringURI) {
		
		// Create the URI for the model element to save
		URI uri = URI.createURI(stringURI);
		
		// Register the UML Resource Factory for the UML resource extension
		registerUMLResourceFactoryForUMLExtension();
		
		// Obtain a resource set
		ResourceSet resourceSet = createResourceSet();
		
		// Create a resource for the designated URI
		Resource resource = resourceSet.createResource(uri);
		
		// Add the model to the resource
		resource.getContents().add(modelElement);
		
		// Persist the contents of the resource
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			
			System.err.println("Error: cannot save model at " + stringURI + ": " + e);
			e.printStackTrace();
			return false;
	   }
		
		return true;
	}

	@Override
	public EObject load(String stringURI) {
		
		// Create the URI for the model element to load
		URI uri = URI.createURI(stringURI);
		
		// Register the UML Resource Factory for the UML resource extension
		registerUMLResourceFactoryForUMLExtension();
		
		// Obtain a resource set
		ResourceSet resourceSet = createResourceSet();
		
		// Create a resource for the designated URI
		Resource resource = resourceSet.createResource(uri);
		
		XMLResource.XMLMap xmlMap = new XMLMapImpl();
		xmlMap.setNoNamespacePackage(UMLPackage.eINSTANCE);
		Map<String, XMLMap> options = new HashMap<>();
		options.put(XMLResource.OPTION_XML_MAP, xmlMap);
	   
		try {
			resource.load(options);
	   } catch(Exception e) {
		   
		   System.err.println("Error: cannot load model from " + stringURI + ": " + e);
		   e.printStackTrace();
		   return null;
	   }
		
		return resource.getContents().get(0);
	}
	
	private void registerUMLResourceFactoryForUMLExtension() {
		Resource.Factory.Registry.INSTANCE
		.getExtensionToFactoryMap()
		.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
	}
	
	private ResourceSet createResourceSet() {
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
}