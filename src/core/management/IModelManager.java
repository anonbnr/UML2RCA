package core.management;

public interface IModelManager<E> {
	boolean save(E modelElement, String stringURI);
	E load(String stringURI);
}
