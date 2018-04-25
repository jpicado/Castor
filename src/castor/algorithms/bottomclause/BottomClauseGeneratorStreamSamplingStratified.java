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
import castor.sampling.SamplingUtils;
import castor.utils.Triple;

public class BottomClauseGeneratorStreamSamplingStratified extends BottomClauseGeneratorStreamSampling {
	
	private List<JoinNode> joinPaths;
	
	public BottomClauseGeneratorStreamSamplingStratified(int seed, JoinNode joinTree) {
		super(seed, joinTree);
		this.joinPaths = SamplingUtils.getAllJoinPathsFromTree(joinTree);
	}

	@Override
	public void createBodyLiterals(GenericDAO genericDAO, Schema schema, 
			JoinNode joinTree, Tuple exampleTuple, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int sampleSize) {
		
		List<Tuple> exampleTupleList = new ArrayList<Tuple>();
		exampleTupleList.add(exampleTuple);
		
		// For each join path, generate samples and add to clause
		for (JoinNode node : joinPaths) {
			for (JoinEdge joinEdge : node.getEdges()) {
				generateBottomClauseAux(genericDAO, schema, exampleTupleList, joinEdge, groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, 1, 1);
			}
		}
	}
}
