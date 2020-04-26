package uml2rca.model.management;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
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

import core.model.management.AbstractModelManager;
import uml2rca.exceptions.NoModelStateFoundException;

public class EcoreModelManager extends AbstractModelManager<EObject> {
	
	/* ATTRIBUTES */
	private String path;
	private Model model;
	private MutablePair<Model, String> currentState;
	private LinkedList<MutablePair<Model, String>> states;
	
	/* STATIC ATTRIBUTES */
	public static Package UML_PRIMITIVE_TYPES_LIBRARY; // the UML library of primitive types
	
	static {
		UML_PRIMITIVE_TYPES_LIBRARY = loadPackage(
				URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
	}

	/* CONSTRUCTORS */
	public EcoreModelManager() {}
	
	public EcoreModelManager(String path) {
		this.states = new LinkedList<MutablePair<Model, String>>();
		loadState(path, "Initial state");
	}
	
	/* METHODS */
	public String getPath() {
		return path;
	}

	public Model getModel() {
		return model;
	}

	public MutablePair<Model, String> getCurrentState() {
		return currentState;
	}
	
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
	public EObject importModel(String stringURI) {
		
		// Create the URI for the model element to load
		URI uri = URI.createURI(stringURI);
		
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
		   
		   System.err.println("Error: cannot load model from " + stringURI + ": " + e);
		   e.printStackTrace();
		   return null;
	   }
		
		return resource.getContents().get(0);
	}
	
	@Override
	public boolean exportModel(EObject modelElement, String stringURI) {
		
		// Create the URI for the model element to save
		URI uri = URI.createURI(stringURI);
		
		// Register the UML Resource Factory for the UML resource extension
		registerUMLResourceFactoryForUMLExtension();
		
		// Obtain a resource set
		ResourceSet resourceSet = createModelResourceSet();
		
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
	
	private boolean addState(MutablePair<Model, String> state) {
		return states.add(state);
	}
	
	public boolean hasState(String stateDescription) {
		return states.stream()
				.map(pair -> pair.getRight())
				.anyMatch(description -> description.equals(stateDescription));
	}
	
	public void saveState(Model model, String stateDescription) {
		this.model = model;
		this.currentState = MutablePair.of(model, stateDescription); 
		addState(this.currentState);
	}
	
	public boolean saveStateAndExport(EObject modelElement, String stateDescription, String path) {
		boolean result = exportModel(modelElement, path);
		
		if (modelElement instanceof Model) {
			this.path = path;
			saveState((Model) modelElement, stateDescription);
		}
		
		return result;
	}
	
	public Model loadState(String stateDescription) throws NoModelStateFoundException {
		
		if (hasState(stateDescription)) {
			currentState = states.stream()
			.filter(pair -> pair.getRight().equals(stateDescription))
			.collect(Collectors.toList())
			.get(0);
			
			model = currentState.getLeft();
		}
		
		else
			throw new NoModelStateFoundException(stateDescription);
		
		return model;
	}
	
	public Model loadInitialState() {
		return states.getFirst().getLeft();
	}
	
	public EObject loadState(String path, String stateDescription) {
		EObject result = importModel(path);
		
		if (result instanceof Model) {
			this.path = path;
			this.model = (Model) result;
			this.currentState = MutablePair.of(model, stateDescription);
			
			if (hasState(stateDescription)) {
				states.stream()
						.filter(pair -> pair.getRight().equals(stateDescription))
						.collect(Collectors.toList())
						.get(0)
						.setLeft(model);
			}
			
			else
				addState(this.currentState);
		}
		
		return result;
	}
	
	public void displayStates() {
		states.stream()
		.map(pair -> pair.getRight())
		.forEach(System.out::println);
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