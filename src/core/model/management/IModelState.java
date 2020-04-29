package core.model.management;

public interface IModelState<E, S> {
	E getModel();
	void setModel(E model);
	S getDescription();
	void setDescription(S description);	
}
