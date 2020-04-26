package core.conflict;

public interface IConflictCandidate<T, E> {
	
	/* METHODS */
	T getCandidate();
	void setCandidate(T candidate);
	boolean satisfiesConflictCondition();
}