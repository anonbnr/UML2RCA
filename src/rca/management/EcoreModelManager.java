package rca.management;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.XMLMap;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.UMLPackage;

import core.management.AbstractModelManager;

public class EcoreModelManager extends AbstractModelManager<EObject> {

	@Override
	public boolean save(EObject modelElement, String stringURI) {
		URI uri = URI.createURI(stringURI);
		
		Resource.Factory.Registry.INSTANCE
		.getExtensionToFactoryMap()
		.put("uml", new XMIResourceFactoryImpl());
		
		Resource resource = (new ResourceSetImpl()).createResource(uri);
		resource.getContents().add(modelElement);
		
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
		URI uri = URI.createURI(stringURI);
		
		Resource.Factory.Registry.INSTANCE
		.getExtensionToFactoryMap()
		.put("uml", new XMIResourceFactoryImpl());
		
		Resource resource = (new ResourceSetImpl()).createResource(uri);
		
		EPackage pack = UMLPackage.eINSTANCE;
		XMLResource.XMLMap xmlMap = new XMLMapImpl();
		xmlMap.setNoNamespacePackage(pack);
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
}