package core.model.management;

public abstract class AbstractModelManager<E> implements IModelManager<E> {

	/* ATTRIBUTES */
	protected E model;
	protected String path;
	
	/* CONSTRUCTORS */
	public AbstractModelManager() {}
	public AbstractModelManager(String path) {
		this.path = path;
	}
	
	/* METHODS */
	@Override
	public E getModel() {
		return model;
	}
	
	@Override
	public String getPath() {
		return path;
	}
}