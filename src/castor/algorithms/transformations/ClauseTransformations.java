package castor.algorithms.transformations;

import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.*;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.utils.Commons;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;

import java.util.*;

public class ClauseTransformations {

    private static Unifier unifier = new Unifier();
    private static SubstVisitor substVisitor = new SubstVisitor();
    
    /*
	 * Reorder clause so that all literals are head-connected from left to right
	 * Removes non-head-connected literals
	 */
	public static MyClause reorderClauseForHeadConnected(MyClause clause) {
		MyClause newClause = new MyClause();
		Set<Term> seenTerms = new HashSet<Term>();
		
		// Add head literal
		Literal head = clause.getPositiveLiterals().get(0);
		newClause.addLiteral(head);
		seenTerms.addAll(head.getAtomicSentence().getArgs());
		
		// Add body literals
		List<Literal> notHeadConnectedLiterals = new ArrayList<Literal>(clause.getNegativeLiterals());
		List<Literal> remainingLiterals = new ArrayList<Literal>(clause.getNegativeLiterals());
		while (!notHeadConnectedLiterals.isEmpty()) {
			remainingLiterals.clear();
			remainingLiterals.addAll(notHeadConnectedLiterals);
			notHeadConnectedLiterals.clear();
			
			boolean foundOne = false;
			for (Literal literal : remainingLiterals) {
				boolean connected = false;
				for (Term term : literal.getAtomicSentence().getArgs()) {
					if (seenTerms.contains(term)) {
						connected = true;
						break;
					}
				}
				
				if (connected) {
					foundOne = true;
					newClause.addLiteral(literal);
					seenTerms.addAll(literal.getAtomicSentence().getArgs());
				} else {
					notHeadConnectedLiterals.add(literal);
				}
			}
			if (!foundOne)
				break;
		}
		
		return newClause;
	}

    /*
     * Minimize clause using theta-subsumption (uses an approximation of clause-subsumption (homomorphism) test)
     */
    public static MyClause minimize(MyClause clause) {
        TimeWatch tw = TimeWatch.start();
        for (Literal literalToRemove : clause.getNegativeLiterals()) {
            // Create new clause with head
            MyClause tempClause = new MyClause(clause.getPositiveLiterals());
            // Add all literals except literalToRemove
            for (Literal otherLiteral : clause.getNegativeLiterals()) {
                if (!literalToRemove.equals(otherLiteral)) {
                    tempClause.addLiteral(otherLiteral);
                }
            }
            // If new clause subsumes original clause, start again with new clause
            if (subsumes(clause, tempClause, literalToRemove)) {
                NumbersKeeper.minimizationTime += tw.time();
                return minimize(tempClause);
            }
        }
        NumbersKeeper.minimizationTime += tw.time();
        return clause;
    }

    /*
     * Minimize clause using theta-subsumption (uses an approximation of clause-subsumption (homomorphism) test)
     * Allow head variables to be substituted for some value. Save substitution in  headSubstitutions
     */
    public static ClauseInfo minimize(ClauseInfo clauseInfo) {
        Map<Variable, Term> headSubstitutions = new HashMap<Variable, Term>();
        return minimize(clauseInfo, headSubstitutions);
    }

    public static ClauseInfo minimize(ClauseInfo clauseInfo, Map<Variable, Term> headSubstitutions) {
        TimeWatch tw = TimeWatch.start();
        for (Literal literalToRemove : clauseInfo.getClause().getNegativeLiterals()) {
            // Create new clause with head
            MyClause tempClause = new MyClause(clauseInfo.getClause().getPositiveLiterals());
            // Add all literals except literalToRemove
            for (Literal otherLiteral : clauseInfo.getClause().getNegativeLiterals()) {
                if (!literalToRemove.equals(otherLiteral)) {
                    tempClause.addLiteral(otherLiteral);
                }
            }
            // If new clause subsumes original clause, start again with new clause
            Map<Variable, Term> newHeadSubstitutions = new HashMap<Variable, Term>();
            if (subsumes(clauseInfo.getClause(), tempClause, literalToRemove, newHeadSubstitutions)) {
                NumbersKeeper.minimizationTime += tw.time();

                clauseInfo.setMoreGeneralClause(tempClause);
                
                // Add previous substitutions to new substitutions?
                newHeadSubstitutions.putAll(headSubstitutions);
                
                return minimize(clauseInfo, newHeadSubstitutions);
            }
        }
        NumbersKeeper.minimizationTime += tw.time();

        // Save substitutions in clauseInfo
        clauseInfo.getHeadSubstitutions().putAll(headSubstitutions);

        return clauseInfo;
    }

    /*
     * Check if subsumerClause subsumes subsumedClause, where subsumerClause = subsumedClause U {literalRemoved}
     */
    private static boolean subsumes(MyClause subsumerClause, MyClause subsumedClause, Literal literalRemoved) {
        boolean subsumes = false;

        // Only accept definite clauses
        if (!subsumerClause.isDefiniteClause() || !subsumedClause.isDefiniteClause()) {
            throw new IllegalArgumentException(
                    "Only definite clauses (exactly one positive literal) are supported.");
        }

        Literal headLiteral = subsumerClause.getPositiveLiterals().get(0);

        // If heads are not equal, return false
        if (!headLiteral.equals(subsumedClause.getPositiveLiterals().get(0))) {
            //subsumes = false;
        } else {
            for (Literal literalOfSubsumed : subsumedClause.getNegativeLiterals()) {
                // Compute most general unifier of literal1 w.r.t. literal2
                Map<Variable, Term> theta = mostGeneralUnifierOfLiteralWrtLiteral(literalRemoved.getAtomicSentence(), literalOfSubsumed.getAtomicSentence());

                if (theta != null) {

                    // It is not a valid substitution if it is substituting a head variable
                    boolean substitutingHeadVariable = false;
                    for (Variable var : theta.keySet()) {
                        if (headLiteral.getAtomicSentence().getArgs().contains(var)) {
                            substitutingHeadVariable = true;
                            break;
                        }
                    }

                    if (!substitutingHeadVariable) {
                        // Apply substitution to all literals in c1 and check if they are in c2
                        boolean subset = true;
                        for (Literal literalOfSubsumer : subsumerClause.getNegativeLiterals()) {
                            Literal literalOfSubsumerTheta = substVisitor.subst(theta, literalOfSubsumer);
                            if (!subsumedClause.getNegativeLiterals().contains(literalOfSubsumerTheta)) {
                                subset = false;
                                break;
                            }
                        }
                        // If c1\theta subset c2, then c1 subsumes c2
                        if (subset) {
                            subsumes = true;
                            break;
                        }
                    }
                }
            }
        }
        return subsumes;
    }

    /*
     * Check if subsumerClause subsumes subsumedClause, where subsumerClause = subsumedClause U {literalRemoved}
     * Allow head variables to be substituted for some value. Save substitution in  headSubstitutions
     *
     */
    private static boolean subsumes(MyClause subsumerClause, MyClause subsumedClause, Literal literalRemoved, Map<Variable, Term> headSubstitutions) {
        boolean subsumes = false;

        // Only accept definite clauses
        if (!subsumerClause.isDefiniteClause() || !subsumedClause.isDefiniteClause()) {
            throw new IllegalArgumentException(
                    "Only definite clauses (exactly one positive literal) are supported.");
        }

        Literal headLiteral = subsumerClause.getPositiveLiterals().get(0);

        // If heads are not equal, return false
        if (!headLiteral.equals(subsumedClause.getPositiveLiterals().get(0))) {
            //subsumes = false;
        } else {
            for (Literal literalOfSubsumed : subsumedClause.getNegativeLiterals()) {
                // Compute most general unifier of literal1 w.r.t. literal2
                Map<Variable, Term> theta = mostGeneralUnifierOfLiteralWrtLiteral(literalRemoved.getAtomicSentence(), literalOfSubsumed.getAtomicSentence());

                if (theta != null) {
                    // If head variable is substituted, save substitution
                    boolean validSubstitution = true;
                    headSubstitutions.clear();
                    for (Variable var : theta.keySet()) {
                        if (headLiteral.getAtomicSentence().getArgs().contains(var)) {
                        	// Avoid head substitutions
//                        	validSubstitution = false;
//                			break;
                			
                    		if (
                    				// do not allow substitutions of head variables for other head variables
                    				headLiteral.getAtomicSentence().getArgs().contains(theta.get(var)) ||
                    				// do not allow substitutions of head variables for other variables
                    				Commons.isVariable(theta.get(var))) {
                    			// Not a valid substitution if head variable is substituted by other head variable
                    			validSubstitution = false;
                    			break;
                    		} else {
                    			headSubstitutions.put(var, theta.get(var));
                    		}
                        }
                    }

                    if (validSubstitution) {
	                    // Apply substitution to all literals in c1 and check if they are in c2
	                    boolean subset = true;
	                    for (Literal literalOfSubsumer : subsumerClause.getNegativeLiterals()) {
	                        Literal literalOfSubsumerTheta = substVisitor.subst(theta, literalOfSubsumer);
	                        if (!subsumedClause.getNegativeLiterals().contains(literalOfSubsumerTheta)) {
	                            subset = false;
	                            break;
	                        }
	                    }
	                    // If c1\theta subset c2, then c1 subsumes c2
	                    if (subset) {
	                        subsumes = true;
	                        break;
	                    }
                    }
                }
            }
        }
        return subsumes;
    }

    /*
     * Unifies two atoms and returns valid substitution only if substituted variables are in atom1 (cannot substitute from atom2)
     * If return value != null, then atom1 theta-subsumes atom2
     */
    public static Map<Variable, Term> mostGeneralUnifierOfLiteralWrtLiteral(AtomicSentence atom1, AtomicSentence atom2) {
        Map<Variable, Term> theta = unifier.unify(atom1, atom2);

        if (theta != null) {
            boolean isValid = true;
            for (Map.Entry<Variable, Term> entry : theta.entrySet()) {
                if (!atom1.getArgs().contains(entry.getKey())) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) {
                return null;
            }
        }
        return theta;
    }

    /*
     * Reorder clause by patterns in descending order of number of shared variables in the pattern
     * A pattern is a set of literals that share local variables
     */
    public static MyClause reorder(MyClause clause) {
        // Get head variables
        List<Term> headVariables = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs();

        // Organize patterns in a map so that a pattern is in an entry where the key is the maximum number of shared variables in the pattern
        Map<Integer, List<Literal>> orderedPatternsMap = new TreeMap<Integer, List<Literal>>(Collections.reverseOrder());
        boolean[] addedToPattern = new boolean[clause.getNumberNegativeLiterals()];
        for (int i = 0; i < clause.getNumberNegativeLiterals(); i++) {
            Literal literal = clause.getNegativeLiterals().get(i);
            Set<Term> patternTerms = new HashSet<Term>();
            // Get other literals that share local variables with current literal, that have not been assigned
            List<Literal> sharingLiterals = new LinkedList<Literal>();
            sharingLiterals.add(literal);
            patternTerms.addAll(literal.getAtomicSentence().getArgs());
            int maxSharedVars = 0;
            for (int j = i + 1; j < clause.getNumberNegativeLiterals(); j++) {
                Literal otherLiteral = clause.getNegativeLiterals().get(j);
                if (!addedToPattern[j]) {
                    int sharedVars = getNumberLocalVariablesInSet(otherLiteral, patternTerms, headVariables);
                    if (sharedVars > 0) {
                        sharingLiterals.add(otherLiteral);
                        patternTerms.addAll(otherLiteral.getAtomicSentence().getArgs());
                        maxSharedVars = Math.max(maxSharedVars, sharedVars);
                        addedToPattern[j] = true;
                    }
                }
            }
            // Add to map with key being the number of maximum shared variables
            if (!orderedPatternsMap.containsKey(maxSharedVars)) {
                orderedPatternsMap.put(maxSharedVars, new LinkedList<Literal>());
            }
            orderedPatternsMap.get(maxSharedVars).addAll(sharingLiterals);
        }

        // Compile all body literals into single list, patterns ordered by number of shared variables
        List<Literal> newClauseLiterals = new LinkedList<Literal>();
        for (Map.Entry<Integer, List<Literal>> entry : orderedPatternsMap.entrySet()) {
            newClauseLiterals.addAll(entry.getValue());
        }

        // Create new clause with head and ordered body literals
        MyClause newClause = new MyClause(newClauseLiterals);
        newClause.addLiteral(clause.getPositiveLiterals().get(0));
        return newClause;
    }

    private static int getNumberLocalVariablesInSet(Literal literal, Set<Term> terms, List<Term> headVariables) {
        int count = 0;
        for (Term term : literal.getAtomicSentence().getArgs()) {
            if (terms.contains(term) && !headVariables.contains(term)) {
                count++;
            }
        }
        return count;
    }

    /*
     * Minimizes single clause by removing its literals
     */
    public static void  minimizeClause(MyClause q1){
        //System.out.println(" Original Query :: "+ q1);
        Set<Literal> literalSet = q1.getLiterals();
        for(Literal literal : literalSet){
            MyClause clause = createNewClause(literal,literalSet);
            boolean contained = determineQueryContainment(clause,q1);
            if(contained){
                q1 = clause;
            }
        }
    }

    /*
    * Helper method to minimizeClause to return a new clause
    */
    private static MyClause createNewClause(Literal literalRemoved, Set<Literal> literalSet){
        Set<Literal> tempLitSet = new HashSet<>();
        for(Literal tempLit: literalSet){
            if(!tempLit.equals(literalRemoved)){
                tempLitSet.add(tempLit);
            }
        }
        return new MyClause(tempLitSet);
    }

    /*
     * Minimize clause definition, a set of queries
     */
    public static void minimizeClauseDefinition(List<MyClause> clauseList){
        //check if q1 is contained in q1
        Iterator<MyClause> iterator = clauseList.iterator();
        while(iterator.hasNext()){
            MyClause myClause1 = (MyClause) iterator.next();
            for(MyClause myClause2: clauseList){
                if(!myClause1.equals(myClause2)){
                    boolean isQueryContained = determineQueryContainment(myClause1,myClause2);
                    if(isQueryContained){
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }
    
    /*
     * Minimize clause definition, a set of queries
     */
    public static void minimizeDefinition(List<ClauseInfo> definition){
        //check if q1 is contained in q1
        Iterator<ClauseInfo> iterator = definition.iterator();
        while(iterator.hasNext()){
        		ClauseInfo clauseInfo1 = (ClauseInfo) iterator.next();
            for(ClauseInfo clauseInfo2: definition){
                if(!clauseInfo1.getClause().equals(clauseInfo2.getClause())){
                    boolean isQueryContained = determineQueryContainment(clauseInfo1.getClause(),clauseInfo2.getClause());
                    if(isQueryContained){
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    /*
     * This method is entry point for queryContaiment algorithm. It Determines if query q1 is contained in q2
     * In other words algorithms find out homomorphism q2 -> q1. If homomorphism exists then q1 is contained in q2.
     * 1) Create a tree/DAG of q2 literals.
     * 2) Each vertex in graph stores matching literal list from q1, thetaMap for variables and constantMap to store constants
     * 3) Construct graph
     * 4) Now run DFS on this graph to find the containment
     */
    public static boolean determineQueryContainment(MyClause q1, MyClause q2) {
        List<Literal> q1Literal = new ArrayList<>(q1.getLiterals());
        List<Literal> q2Literal = new ArrayList<>(q2.getLiterals());

        //predicatesToLiteralMap contains the q2 predicates and the corresponding matched literals in q1
        Map<String, List<Literal>> predicatesToLiteralMap = new HashMap<>();

        //If q2 predicate is not found in q1 then createLiteralMapping returns false
        boolean allPredicatesFound =   createLiteralMapping(predicatesToLiteralMap, q2Literal, q1Literal);

        //If all the predicates from q2 found in q1 and predicatesToLiteralMap is not empty then construct graph
        if (allPredicatesFound && !predicatesToLiteralMap.isEmpty()) {
            QueryContainmentGraph graph = constructGraph(q2Literal, predicatesToLiteralMap);
            return runAlgorithm(graph);
        }
        return false;
    }

    private static boolean runAlgorithm(QueryContainmentGraph graph) {
        Map<Variable, Term> masterThetaMap = new HashMap<>();
        List<Vertex> vertexList = graph.getAllVertex();
        return dfs(vertexList.get(0), true, masterThetaMap);
    }

    //DFS Run for Query containment
    private static boolean dfs(Vertex vertex, boolean matchFound, Map<Variable, Term> masterThetaMap) {
        List<Literal> literalList = vertex.getLiteralList();
        Literal q2Literal = vertex.getLiteral();

        for (int i = 0; i < literalList.size(); i++) {
            //This code block ensures there is consistency in the MasterThetaMap variables
            if (!vertex.getThetaMap().isEmpty())
                removeVertexThetaMap(vertex.getThetaMap(), masterThetaMap);

            //If masterThetaMap is empty then unify the q2 literal with next q1 literal
            if (masterThetaMap.isEmpty()) {
                matchFound = unifyLiterls(vertex, literalList.get(i), masterThetaMap);
                if(!matchFound){
                    continue;
                }
            } else {
                //Update q2 literal with variables from masterThetaMap
                Literal subq2Literal = substVisitor.subst(masterThetaMap, q2Literal);

                //Get the substitution map happened in previous step
                Map<Integer, Term> subMap = findSubstitution(q2Literal.getAtomicSentence().getArgs(), subq2Literal.getAtomicSentence().getArgs(), masterThetaMap);
                Literal q1Literal = literalList.get(i);

                //Find out whether q2 literal matches with q1, unify literal if match found else go to next literal in list. If all items in list visited then backtrack
                matchFound = isQ2LiteralMatched(vertex.getConstantMap(), subMap, q1Literal);
                if (matchFound) {
                    matchFound = unifyLiterls(vertex, q1Literal, masterThetaMap);
                    if(!matchFound){
                        continue;
                    }
                }
            }
            //If matching successful then go to next vertex
            if (matchFound) {
                //Check to ensure its not the sink node
                if (vertex.getAdjacentVertex() != null) {
                    matchFound = dfs(vertex.getAdjacentVertex(), true, masterThetaMap);
                    if (matchFound) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        //This code block ensures there is consistency in the MasterThetaMap variables
        if (!vertex.getThetaMap().isEmpty())
            removeVertexThetaMap(vertex.getThetaMap(), masterThetaMap);

        return matchFound;
    }

    /*
     * Clean up masterThetaMap on backtrack to remove previous stored results
     */
    private static void removeVertexThetaMap(Map<Variable, Term> vertexThetaMap, Map<Variable, Term> masterThetaMap) {
        for (Map.Entry<Variable, Term> entry : vertexThetaMap.entrySet()) {
            if (masterThetaMap.containsKey(entry.getKey())) {
                masterThetaMap.remove(entry.getKey());
            }
        }
        vertexThetaMap.clear();
    }

    /*
     * Verify if the q2 literal is matched to q1 literal
     */
    private static boolean isQ2LiteralMatched(Map<Integer,Constant> constMap, Map<Integer, Term> subMap, Literal q1Literal) {
        //Verify variables
        for (Map.Entry<Integer, Term> entry : subMap.entrySet()) {
            List<Term> q2Args = q1Literal.getAtomicSentence().getArgs();
            if (!q2Args.get((int) entry.getKey()).equals((Term) entry.getValue())) {
                return false;
            }
        }
        //Verify constants
        for (Map.Entry<Integer,Constant> entry : constMap.entrySet()) {
            List<Term> q2Args = q1Literal.getAtomicSentence().getArgs();
            if (!q2Args.get((int) entry.getKey()).equals((Term) entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /*
     * Find out recent substitution with the help of masterThetaMap
     */
    private static Map<Integer, Term> findSubstitution(List<Term> q2Args, List<Term> q2SubArgs, Map<Variable, Term> masterThetaMap) {
        Map<Integer, Term> subMap = new HashMap<>();
        int index = 0;
        for (Term term : q2Args) {
            if (!term.equals(q2SubArgs.get(index)) || masterThetaMap.containsKey(term)) {
                subMap.put(index, q2SubArgs.get(index));
            }
            index++;
        }
        return subMap;
    }

    /*
     * Unify two literals and update the masterThetaMap with the
     */
    private static boolean unifyLiterls(Vertex vertex, Literal q1Literal, Map<Variable, Term> masterThetaMap) {
        Map<Variable, Term> thetaMap;
        Literal q2Literal = vertex.getLiteral();
        thetaMap = mostGeneralUnifierOfLiteralWrtLiteral(q2Literal.getAtomicSentence(), q1Literal.getAtomicSentence());

        //If thetaMap is null then it means unification failed, mostly in case of constants
        if(thetaMap == null)
            return false;

        //thetaMap is empty when both the source and target terms are same, therefore
        if (thetaMap.isEmpty()) {
            List<Term> terms = q1Literal.getAtomicSentence().getArgs();
            for (Term term : terms) {
                if(term instanceof Variable)
                    thetaMap.put((Variable) term, term);
            }
        }
        //Update masterThetaMap with new unification result
        thetaMap = addThetaToMasterThetaMap(thetaMap, masterThetaMap);
        //Update the vertex thetaMap with unification new result
        vertex.setThetaMap(thetaMap);
        return true;
    }

    /*
     * Update the masterThetaMap with the new unification result
     */
    private static Map<Variable, Term> addThetaToMasterThetaMap(Map<Variable, Term> thetaMap, Map<Variable, Term> masterThetaMap) {
        Map<Variable, Term> thetaMapNew = new HashMap<>();
        for (Map.Entry<Variable, Term> entry : thetaMap.entrySet()) {
            if (!masterThetaMap.containsKey(entry.getKey())) {
                masterThetaMap.put((Variable) entry.getKey(), (Term) entry.getValue());
                thetaMapNew.put((Variable) entry.getKey(), (Term) entry.getValue());
            }
        }
        return thetaMapNew;
    }

    /*
     * Construct graph using q2 literals as vertex
     */
    private static QueryContainmentGraph constructGraph(List<Literal> q2Literal, Map<String, List<Literal>> predicatesToLiteralMap) {
        QueryContainmentGraph graph = new QueryContainmentGraph(true);
        List<Vertex> vertexList = new ArrayList<>();
        int vertex2Index = 0;
        for (Literal l2 : q2Literal) {
            //Set constants Map here while constructing graph itself
            Map<Integer,Constant> constMap = getLiteralConstantsMap(l2);
            Predicate l2Predicate = (Predicate) l2.getAtomicSentence();
            String l2Name = l2Predicate.getPredicateName();
            Vertex vertex2 = graph.createNewVertex(l2Name, l2, predicatesToLiteralMap.get(l2Name),constMap);
            vertexList.add(vertex2);
            if (vertexList.size() >= 2) {
                graph.addEdge(vertexList.get(vertex2Index), vertex2);
                vertex2Index++;
            }
        }
        return graph;
    }

    /*
     * Return constant location and value in literal
     */
    private static  Map<Integer,Constant> getLiteralConstantsMap(Literal l2){
        Map<Integer,Constant> constMap = new HashMap<>();
        List<Term> terms = l2.getAtomicSentence().getArgs();
        int index = 0;
        for(Term term : terms){
            if (term instanceof Constant){
                constMap.put(index,(Constant)term);
            }
            index++;
        }
        return constMap;
    }

    /*
     * Update predicateMap with q2 predicates name as key and q1 literals with same predicate name as values. This helps in constructing graph
     */
    private static boolean createLiteralMapping(Map<String, List<Literal>> predicateMap, List<Literal> q2Literal, List<Literal> q1Literal) {
        for (Literal l2 : q2Literal) {
            Predicate l2Predicate = (Predicate) l2.getAtomicSentence();
            if (!predicateMap.containsKey(l2Predicate.getPredicateName())) {
                List<Literal> literalList = new ArrayList<>();
                boolean literalFound = false;
                for (Literal l1 : q1Literal) {
                    Predicate l1Predicate = (Predicate) l1.getAtomicSentence();
                    if (l2Predicate.getPredicateName().equals(l1Predicate.getPredicateName())) {
                        literalFound = true;
                        literalList.add(l1);
                    }
                }
                if(!literalFound)
                    return false;
                if (!literalList.isEmpty())
                    predicateMap.put(l2Predicate.getPredicateName(), literalList);
            }
        }
        return true;
    }

}
