package core.management;

public interface IModelManager<E> {
	boolean exportModel(E modelElement, String stringURI);
	E importModel(String stringURI);
}
