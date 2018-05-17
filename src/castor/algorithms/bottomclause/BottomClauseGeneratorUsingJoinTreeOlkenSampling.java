package castor.algorithms.bottomclause;

import java.util.List;
import java.util.Map;
import java.util.Random;

import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.JoinEdge;
import castor.sampling.JoinNode;
import castor.utils.Triple;

public abstract class BottomClauseGeneratorUsingJoinTreeOlkenSampling extends BottomClauseGeneratorUsingJoinTree {

	public BottomClauseGeneratorUsingJoinTreeOlkenSampling(int seed, JoinNode joinTree) {
		super(seed, joinTree);
	}
	
	protected void generateBottomClauseAux(GenericDAO genericDAO, Schema schema, 
			List<Tuple> tuples, JoinEdge joinEdge, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int depth, int sampleSize) {
		//TODO implement
	}
	
}
