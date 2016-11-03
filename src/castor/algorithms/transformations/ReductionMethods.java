package castor.algorithms.transformations;

public interface ReductionMethods {

	// NO NEGATIVE REDUCTION
	public String NEGATIVE_REDUCTION_NONE 			= "none";
	// REDUCE USING CONSISTENCY (AS IN GOLEM)
	public String NEGATIVE_REDUCTION_CONSISTENCY	= "consistency";
	// REDUCE USING PRECISION (AS IN PROGOLEM)
	public String NEGATIVE_REDUCTION_PRECISION 		= "precision";
}
