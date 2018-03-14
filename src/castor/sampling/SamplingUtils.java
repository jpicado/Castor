package castor.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import castor.language.Argument;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.settings.DataModel;
import castor.settings.Parameters;

public class SamplingUtils {

	/*
	 * Finds the join tree at schema level (using modes)
	 */
	public static JoinNode findJoinTree(DataModel dataModel, Parameters parameters) {
		JoinNode headNode = new JoinNode(dataModel.getModeH().getPredicateName());
		
		// Group modes by type
		Map<String,List<Mode>> modesGroupedByType = new HashMap<String,List<Mode>>();
		Set<String> usedModes = new HashSet<String>();
		for (Mode mode : dataModel.getModesB()) {
			// Skip modes if already seen same modes but with different access modes (+,-,#)
			if (!usedModes.contains(mode.toGroundModeString())) {
				usedModes.add(mode.toGroundModeString());
				
				for (Argument argument : mode.getArguments()) {
					if (!modesGroupedByType.containsKey(argument.getType())) {
						modesGroupedByType.put(argument.getType(), new ArrayList<Mode>());
					}
					modesGroupedByType.get(argument.getType()).add(mode);
				}
			}
		}
		
		// Call recursive function
		findJoinTreeAux(headNode, dataModel.getModeH(), modesGroupedByType, 0, parameters.getIterations());
		
		return headNode;
	}

	/*
	 * Recursive auxiliary function for findJoinTree
	 */
	private static void findJoinTreeAux(JoinNode node, Mode modeForNode, Map<String, List<Mode>> modesGroupedByType, int currentIteration, int maxIterations) {
		if (currentIteration == maxIterations)
			return;
		
		// Find input attribute in mode
		int inputAttribute;
		for (inputAttribute = 0; inputAttribute < modeForNode.getArguments().size(); inputAttribute++) {
			if (modeForNode.getArguments().get(inputAttribute).getIdentifierType().equals(IdentifierType.INPUT))
				break;
		}
		
		for (int i = 0; i < modeForNode.getArguments().size(); i++) {
			String type = modeForNode.getArguments().get(i).getType();
			
			// Find other relations that join with current relation
			for (Mode mode : modesGroupedByType.get(type)) {
				for (int j = 0; j < mode.getArguments().size(); j++) {
					// Skip cases where joining same relation over same attribute
					if (modeForNode.getPredicateName().equals(mode.getPredicateName()) && inputAttribute == j)
						continue;
					
					// If same type, relations can join
					if (type.equals(mode.getArguments().get(j).getType())) {
						JoinNode newJoinNode = new JoinNode(mode.getPredicateName());
						node.getEdges().add(new JoinEdge(newJoinNode, i, j));
						
						// Recursive call
						findJoinTreeAux(newJoinNode, mode, modesGroupedByType, currentIteration+1, maxIterations);
					}
				}
			}
		}
	}
}
