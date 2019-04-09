package castor.algorithms.bottomclause;

import java.util.ArrayList;
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

public class BottomClauseGeneratorUsingJoinTreeOlkenSamplingRandom extends BottomClauseGeneratorUsingJoinTreeOlkenSampling {

	public BottomClauseGeneratorUsingJoinTreeOlkenSamplingRandom(int seed, JoinNode joinTree) {
		super(seed, joinTree);
	}

	@Override
	public void createBodyLiterals(GenericDAO genericDAO, Schema schema, 
			JoinNode joinTree, Tuple exampleTuple, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int sampleSize, int queryLimit) {
		
		// Approach 1: Get one sample from all relations, multiple times
		//TODO: implement
		
		// Approach 2: Get multiple samples from each relation, instead of calling it multiple times
		List<Tuple> exampleTupleList = new ArrayList<Tuple>();
		exampleTupleList.add(exampleTuple);
		for (JoinEdge joinEdge : joinTree.getEdges()) {
			generateBottomClauseAux(genericDAO, schema, exampleTupleList, joinEdge, groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, 1, sampleSize, queryLimit);
		}
	}
}
