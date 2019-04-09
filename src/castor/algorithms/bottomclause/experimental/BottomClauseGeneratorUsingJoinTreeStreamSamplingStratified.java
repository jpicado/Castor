package castor.algorithms.bottomclause.experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import castor.algorithms.bottomclause.BottomClauseGeneratorUsingJoinTreeStreamSampling;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.JoinEdge;
import castor.sampling.JoinNode;
import castor.sampling.SamplingUtils;
import castor.utils.Triple;

/*
 * This version computes all join paths, and uses stream sampling for each join path.
 * IT HAS THE FOLLOWING PROBLEMS:
 * 1) SAMPLES FROM SAME RELATION MULTIPLE TIMES.
 * 2) IT SAMPLES DIFFERENT TUPLES FOR EVERY JOIN PATH. THEREFORE, IT IS UNLIKELY THAT IT CAPTURES TREE-LIKE PATTERNS.
 */
//TODO: DELETE THIS CLASS, AS IT IS NOT USEFUL?
public class BottomClauseGeneratorUsingJoinTreeStreamSamplingStratified extends BottomClauseGeneratorUsingJoinTreeStreamSampling {
	
	private List<JoinNode> joinPaths;
	
	public BottomClauseGeneratorUsingJoinTreeStreamSamplingStratified(int seed, JoinNode joinTree) {
		super(seed, joinTree);
		this.joinPaths = SamplingUtils.getAllJoinPathsFromTree(joinTree);
	}

	@Override
	public void createBodyLiterals(GenericDAO genericDAO, Schema schema, 
			JoinNode joinTree, Tuple exampleTuple, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int sampleSize, int queryLimit) {
		
		List<Tuple> exampleTupleList = new ArrayList<Tuple>();
		exampleTupleList.add(exampleTuple);
		
		// For each join path, generate samples and add to clause
		for (JoinNode node : joinPaths) {
			for (JoinEdge joinEdge : node.getEdges()) {
				// Join path sizes change for each join path
				joinPathSizes.clear();
				generateBottomClauseAux(genericDAO, schema, exampleTupleList, joinEdge, groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, 1, sampleSize, queryLimit);
			}
		}
	}
}
