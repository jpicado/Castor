/*
 * CastorLearnerBatchGeneralization: This class is extended version of the base class: "CastorLearner"
 * In the CastorLearner, during generalization, the score of a clause is computed using all
 * the examples in the training set.
 *
 * In CastorLearnerBatchGeneralization, instead of computing score on all the examples,
 * the score will be calculated on a mini batch.
 *
 * Modifications:
 * learnUsingCovering and learnClause methods are modified to accept new parameter: estimationSample
 * -estimationSample - New parameter is introduced to specify batch size
 * learnClause - It is modified to call computeBatchScore method. It is also modified to create mini batch
 * of random examples for score computation and the selected examples are removed from the bucket(Sampling without replacement)
 */

package castor.algorithms;

import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.algorithms.clauseevaluation.BottomUpEvaluator;
import castor.algorithms.clauseevaluation.EvaluationFunctions;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.algorithms.transformations.CastorReducer;
import castor.algorithms.transformations.ClauseTransformations;
import castor.algorithms.transformations.ReductionMethods;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Formatter;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CastorLearnerBatchGeneralization extends CastorLearner {

    private static Logger logger = Logger.getLogger(CastorLearnerBatchGeneralization.class);

    public CastorLearnerBatchGeneralization(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseContructionDAO, BottomClauseGenerator saturator,
                         CoverageEngine coverageEngine, CoverageEngine coverageEngineForCoveringApproach, Parameters parameters, Schema schema) {
        this.parameters = parameters;
        this.genericDAO = genericDAO;
        this.bottomClauseConstructionDAO = bottomClauseContructionDAO;
        this.coverageEngine = coverageEngine;
        this.coverageEngineForCoveringApproach = coverageEngineForCoveringApproach;
        this.randomGenerator = new Random(parameters.getRandomSeed());
        this.evaluator = new BottomUpEvaluator();
        this.saturator = saturator;
    }

    /*
     * Run learning algorithm
     */
    public List<ClauseInfo> learn(Schema schema, DataModel dataModel, Relation posExamplesRelation,
                                  Relation negExamplesRelation, String spNameTemplate, boolean globalDefinition) {
        TimeWatch tw = TimeWatch.start();

        logger.info("Training positive examples in table " + posExamplesRelation.getName() + ": "
                + this.coverageEngine.getAllPosExamples().size());
        logger.info("Training negative examples in table " + negExamplesRelation.getName() + ": "
                + this.coverageEngine.getAllNegExamples().size());

        List<ClauseInfo> definition = new LinkedList<ClauseInfo>();

        // Call covering approach
        definition.addAll(this.learnUsingCovering(schema, dataModel, posExamplesRelation, negExamplesRelation,
                spNameTemplate, parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms(),
                parameters.getSample(), parameters.getBeam(), parameters.getReductionMethod(), globalDefinition,
                parameters.getEstimationSample()));

//		ClauseTransformations.minimizeDefinition(definition);

        // Get string representation of definition
        StringBuilder sb = new StringBuilder();
        sb.append("Definition learned:\n");
        for (ClauseInfo clauseInfo : definition) {
            // Count covered examples and include them in string representation
            int posCoveredCount = 0;
            int negCoveredCount = 0;
            for (int i = 0; i < clauseInfo.getPosExamplesCovered().length; i++) {
                if (clauseInfo.getPosExamplesCovered()[i])
                    posCoveredCount++;
            }
            for (int i = 0; i < clauseInfo.getNegExamplesCovered().length; i++) {
                if (clauseInfo.getNegExamplesCovered()[i])
                    negCoveredCount++;
            }

            sb.append(Formatter.prettyPrint(clauseInfo) + "\t(Pos cover=" + posCoveredCount + ", Neg cover="
                    + negCoveredCount + ")\n");
        }
        logger.info(sb.toString());

        NumbersKeeper.learningTime += tw.time(TimeUnit.MILLISECONDS);

        return definition;
    }

    /*
     * Learn a clause using covering approach
     */
    private List<ClauseInfo> learnUsingCovering(Schema schema, DataModel dataModel, Relation posExamplesRelation,
                                                Relation negExamplesRelation, String spNameTemplate, int iterations, int maxRecall, int maxterms,
                                                int sampleSize, int beamWidth, String reductionMethod, boolean globalDefinition, double estimationSample) {
        List<ClauseInfo> definition = new LinkedList<ClauseInfo>();

        // Get all positive examples from database and keep them in memory
        List<Tuple> remainingPosExamples = new LinkedList<Tuple>(coverageEngine.getAllPosExamples());
        List<Tuple> uncoveredPosExamples = new LinkedList<Tuple>(coverageEngine.getAllPosExamples());

        while (remainingPosExamples.size() > 0) {
            logger.info("Remaining uncovered examples: " + remainingPosExamples.size());

            // Compute best ARMG
            ClauseInfo clauseInfo = learnClause(schema, dataModel, remainingPosExamples, uncoveredPosExamples, posExamplesRelation,
                    negExamplesRelation, spNameTemplate, iterations, maxRecall, maxterms, sampleSize, beamWidth, estimationSample);

            // Get new positive examples covered
            // Adding 1 to count seed example
            int newPosTotal = uncoveredPosExamples.size();
            int newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo,
                    uncoveredPosExamples, posExamplesRelation, true);

            // Get total positive examples covered
            int allPosTotal = coverageEngine.getAllPosExamples().size();
            int allPosCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo,
                    posExamplesRelation, true);

            // Get total negative examples covered
            int allNegTotal = coverageEngine.getAllNegExamples().size();
            int allNegCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo,
                    negExamplesRelation, false);

            // Compute statistics
            // For remaining positive examples
            int truePositive = newPosCoveredCount;
            int falsePositive = allNegCoveredCount;
            int trueNegative = allNegTotal - allNegCoveredCount;
            int falseNegative = newPosTotal - newPosCoveredCount;
            // For all examples
            int truePositiveAll = allPosCoveredCount;
            int falsePositiveAll = allNegCoveredCount;// totalPos - posCoveredCount;
            int trueNegativeAll = allNegTotal - allNegCoveredCount;
            int falseNegativeAll = allPosTotal - allPosCoveredCount;

            // Precision and F1 over new (uncovered) examples
            double precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive,
                    falsePositive, trueNegative, falseNegative);
            double f1 = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.F1, truePositive, falsePositive,
                    trueNegative, falseNegative);
            // Recall over all examples
            double recall = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.RECALL, truePositiveAll,
                    falsePositiveAll, trueNegativeAll, falseNegativeAll);

            logger.info("Stats before reduction: Precision(new)=" + precision + ", F1(new)=" + f1 + ", Recall(all)="
                    + recall);

            // Reduce clause only if it satisfies conditions
            if (satisfiesConditions(truePositive, falsePositive, trueNegative, falseNegative, newPosCoveredCount,
                    precision, recall)) {

                if (!reductionMethod.equals(ReductionMethods.NEGATIVE_REDUCTION_NONE)) {
                    // Compute negative based reduction
                    // Add 1 to scores to count seed example, which is not in remainingPosExamples
                    double beforeReduceScore = this.computeScore(schema, uncoveredPosExamples, posExamplesRelation,
                            negExamplesRelation, clauseInfo);
                    logger.info("Before reduction - NumLits:" + clauseInfo.getClause().getNumberLiterals() + ", Score:"
                            + beforeReduceScore);
                    logger.debug("Before reduction:\n" + Formatter.prettyPrint(clauseInfo.getClause()));

                    if (reductionMethod.equals(ReductionMethods.NEGATIVE_REDUCTION_CONSISTENCY)) {
                        clauseInfo.setMoreGeneralClause(CastorReducer.negativeReduce(genericDAO, coverageEngine,
                                clauseInfo.getClause(), schema, uncoveredPosExamples, posExamplesRelation,
                                negExamplesRelation, CastorReducer.MEASURE.CONSISTENCY, evaluator));
                    } else if (reductionMethod.equals(ReductionMethods.NEGATIVE_REDUCTION_PRECISION)) {
                        clauseInfo.setMoreGeneralClause(CastorReducer.negativeReduce(genericDAO, coverageEngine,
                                clauseInfo.getClause(), schema, uncoveredPosExamples, posExamplesRelation,
                                negExamplesRelation, CastorReducer.MEASURE.PRECISION, evaluator));
                    }

                    double afterReduceScore = this.computeScore(schema, uncoveredPosExamples, posExamplesRelation,
                            negExamplesRelation, clauseInfo);
                    logger.info("After reduction - NumLits:" + clauseInfo.getClause().getNumberLiterals() + ", Score:"
                            + afterReduceScore);
                    logger.debug("After reduction:\n" + Formatter.prettyPrint(clauseInfo.getClause()));
                }

                // Final minimization to remove redundant literals
//				 clauseInfo.setMoreGeneralClause(this.transform(schema, clauseInfo.getClause()));
                clauseInfo = this.transform(schema, clauseInfo);

                logger.info("After minimization - NumLits:" + clauseInfo.getClause().getNumberLiterals());
                logger.debug("After minimization:\n" + Formatter.prettyPrint(clauseInfo));

                // Get new positive examples covered
                // Adding 1 to count seed example
                newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo,
                        uncoveredPosExamples, posExamplesRelation, true);
                allPosCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo,
                        posExamplesRelation, true);

                // Compute statistics
                // For remaining positive examples
                truePositive = newPosCoveredCount;
                falsePositive = allNegCoveredCount;
                trueNegative = allNegTotal - allNegCoveredCount;
                falseNegative = newPosTotal - newPosCoveredCount;
                // For all examples
                truePositiveAll = allPosCoveredCount;
                falsePositiveAll = allNegCoveredCount;// totalPos - posCoveredCount;
                trueNegativeAll = allNegTotal - allNegCoveredCount;
                falseNegativeAll = allPosTotal - allPosCoveredCount;

                double score = this.computeScore(schema, uncoveredPosExamples, posExamplesRelation, negExamplesRelation,
                        clauseInfo);

                // Precision and F1 over new (uncovered) examples
                precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive,
                        falsePositive, trueNegative, falseNegative);
                f1 = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.F1, truePositive, falsePositive,
                        trueNegative, falseNegative);
                // Recall over all examples
                recall = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.RECALL, truePositiveAll,
                        falsePositiveAll, trueNegativeAll, falseNegativeAll);

                logger.info("Stats: Score=" + score + ", Precision(new)=" + precision + ", F1(new)=" + f1
                        + ", Recall(all)=" + recall);

                if (satisfiesConditions(truePositive, falsePositive, trueNegative, falseNegative, newPosCoveredCount,
                        precision, recall)) {
                    // Reorder so that it is more readable
                    clauseInfo.setMoreGeneralClause(ClauseTransformations.reorderClauseForHeadConnected(clauseInfo.getClause()));

                    // Add clause to definition
                    definition.add(clauseInfo);
                    logger.info("New clause added to theory:\n" + Formatter.prettyPrint(clauseInfo));
                    logger.info("New pos cover = " + newPosCoveredCount + ", Total pos cover = " + allPosCoveredCount
                            + ", Total neg cover = " + allNegCoveredCount);

                    // Remove covered positive examples
                    if (!globalDefinition) {
                        ClauseInfo cleanClauseInfo = new ClauseInfo(clauseInfo.getClause(), this.coverageEngineForCoveringApproach.getAllPosExamples().size(),
                                this.coverageEngineForCoveringApproach.getAllNegExamples().size());
                        List<Tuple> coveredExamples = this.coverageEngineForCoveringApproach.coveredExamplesTuplesFromList(genericDAO,
                                schema, cleanClauseInfo, uncoveredPosExamples, posExamplesRelation, true);
                        remainingPosExamples.removeAll(coveredExamples);
                        uncoveredPosExamples.removeAll(coveredExamples);
                    }
                }
            } // end condition
        }

        return definition;
    }

    /*
     * Perform generalization using beam search + ARMG
     */
    private ClauseInfo learnClause(Schema schema, DataModel dataModel, List<Tuple> remainingPosExamples, List<Tuple> uncoveredPosExamples,
                                   Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations,
                                   int recall, int maxterms, int sampleSize, int beamWidth, Double estimationSample) {
        TimeWatch tw = TimeWatch.start();

        // First unseen positive example (pop)
        Tuple exampleTuple = remainingPosExamples.remove(0);

        // Generate bottom clause
        logger.info("Generating bottom clause for " + exampleTuple.getValues().toString() + "...");
        TimeWatch twSaturation = TimeWatch.start();
        MyClause bottomClause = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple,
                schema, dataModel, parameters);
        long saturationTime = twSaturation.time(TimeUnit.MILLISECONDS);
        NumbersKeeper.bottomClauseConstructionTime += saturationTime;

        logger.debug("Bottom clause: \n" + Formatter.prettyPrint(bottomClause));
        logger.info("Literals: " + bottomClause.getNumberLiterals());
        logger.info("Saturation time: " + saturationTime + " milliseconds.");

        // MINIMIZATION CAN BE USEFUL WHEN THERE ARE NO ISSUES IF LITERALS WITH
        // VARIABLES ARE REMOVED BECAUSE THERE ARE LITERALS WITH CONSTANTS.
        // IF LITERALS WITH VARIABLES ARE IMPORTANT (E.G. TO BE MORE GENERAL),
        // MINIMIZATION SHOULD BE TURNED OFF.
        // Minimize bottom clause
        if (parameters.isMinimizeBottomClause()) {
            logger.info("Transforming bottom clause...");
            bottomClause = this.transform(schema, bottomClause);
            logger.info("Literals of transformed clause: " + bottomClause.getNumberLiterals());
            logger.debug("Transformed bottom clause:\n" + Formatter.prettyPrint(bottomClause));
        }

        // Reorder bottom clause
        // NOTE: this is needed for some datasets (e.g. HIV-small, fold 2)
        logger.info("Reordering bottom clause...");
        bottomClause = this.reorderAccordingToINDs(schema, bottomClause);

        logger.info("Generalizing clause...");

        List<ClauseInfo> bestARMGs = new LinkedList<ClauseInfo>();
        bestARMGs.add(new ClauseInfo(bottomClause, this.coverageEngine.getAllPosExamples().size(),
                this.coverageEngine.getAllNegExamples().size()));

        //List<Tuple> samplePosExamples = selectRandomExamples((int)Math.abs(estimationSample*uncoveredPosExamples.size()),uncoveredPosExamples);

        boolean createdNewARMGS = true;
        int iters = 0;
        // Add 1 to scores to count seed example, which is not in remainingPosExamples
        double clauseScore = this.computeScore(schema, uncoveredPosExamples, posExamplesRelation, negExamplesRelation,
                bestARMGs.get(0));

        logger.info("Best armg at iter " + iters + " - NumLits:" + bestARMGs.get(0).getClause().getNumberLiterals()
                + ", Score:" + clauseScore);

        //Use uncoveredPosExamples for batch testing
        List<Tuple> posExamplesBucket = uncoveredPosExamples;
        int batchSize = (int)Math.abs(estimationSample*posExamplesBucket.size());
        // Generalize only if there are more examples
        if (remainingPosExamples.size() > 0) {
            while (createdNewARMGS) {
                iters++;
                createdNewARMGS = false;

                // Select K random examples
                List<Tuple> sample = selectRandomExamples(sampleSize);

                // Select random examples from posExamplesBucket and remove the selected examples
                Set<Integer> randomPositions = generateListOfRandomNumbers(posExamplesBucket.size(), batchSize);
                List<Tuple> samplePosExamples = selectExamplesAtPosition(posExamplesBucket, randomPositions);
                posExamplesBucket = removeExamplesAtPosition(posExamplesBucket, randomPositions);

                // Compute best score of clauses in bestARMGs
                double bestScore = Double.NEGATIVE_INFINITY;
                for (ClauseInfo clauseInfo : bestARMGs) {
                    double score = this.computeBatchScore(schema, samplePosExamples, posExamplesRelation,
                            negExamplesRelation, clauseInfo);
                    bestScore = Math.max(bestScore, score);
                }

                // Generalize each clause in ARMG using examples in sample
                List<ClauseInfo> newARMGs = new LinkedList<ClauseInfo>();
                for (ClauseInfo clauseInfo : bestARMGs) {
                    for (Tuple tuple : sample) {
                        // Perform ARMG
                        ClauseInfo newClauseInfo = armg(schema, clauseInfo, tuple, posExamplesRelation);

                        if (isSafeClause(newClauseInfo.getClause())) {
                            // Keep clause only if its score is better than current best score
                            double score = this.computeBatchScore(schema, samplePosExamples, posExamplesRelation,
                                    negExamplesRelation, newClauseInfo);
                            if (score > bestScore) {
                                newARMGs.add(newClauseInfo);
                            }
                        }
                    }
                }

                if (!newARMGs.isEmpty()) {
                    // Keep highest scoring N clauses from newARMGs
                    bestARMGs.clear();
                    bestARMGs.addAll(this.getHighestScoring(newARMGs, beamWidth, schema, uncoveredPosExamples,
                            posExamplesRelation, negExamplesRelation));
                    createdNewARMGS = true;
                }

                clauseScore = this.computeBatchScore(schema, samplePosExamples, posExamplesRelation, negExamplesRelation,
                        bestARMGs.get(0));
                logger.info("Best armg at iter " + iters + " - NumLits:"
                        + bestARMGs.get(0).getClause().getNumberLiterals() + ", Score:" + clauseScore);
            }
        }

        // Get highest scoring clause from bestARMGs
        ClauseInfo bestClauseInfo = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (ClauseInfo clauseInfo : bestARMGs) {
            double score = this.computeScore(schema, uncoveredPosExamples, posExamplesRelation, negExamplesRelation,
                    clauseInfo);
            if (score > bestScore) {
                bestClauseInfo = clauseInfo;
                bestScore = score;
            }
        }

        NumbersKeeper.learnClauseTime += tw.time();

        return bestClauseInfo;
    }

	/*
     * Select examples at given positions in exmapleSet
    */
    private List<Tuple> selectExamplesAtPosition(List<Tuple> exampleSet, Set<Integer> randomPositions) {
        List<Tuple> examples = new LinkedList<>();
        for (Integer pos : randomPositions) {
            examples.add(exampleSet.get(pos));
        }
        return examples;
    }

    /*
     * Remove examples at given positions in exmapleSet
    */
    private List<Tuple> removeExamplesAtPosition(List<Tuple> exampleSet, Set<Integer> randomPositions) {
        List<Tuple> examples = new LinkedList<>();
        int index = 0;
        for (Tuple tuple : exampleSet) {
            if (!randomPositions.contains(index++)) {
                examples.add(tuple);
            }
        }
        return examples;
    }

    /*
    * Compute score of a clause for batch
    */
    private double computeBatchScore(Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation,
                                Relation negExamplesRelation, ClauseInfo clauseInfo) {
        double score;
        if (clauseInfo.getScore() == null) {
            score = evaluator.computeBatchScore(genericDAO, coverageEngine, schema, remainingPosExamples,
                    posExamplesRelation, negExamplesRelation, clauseInfo, EvaluationFunctions.FUNCTION.COVERAGE);
        } else {
            score = clauseInfo.getScore();
        }
        return score;
    }

    /*
    * Compute score of a clause
    */
    private double computeScore(Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation,
                                Relation negExamplesRelation, ClauseInfo clauseInfo) {
        double score;
        if (clauseInfo.getScore() == null) {
            score = evaluator.computeScore(genericDAO, coverageEngine, schema, remainingPosExamples,
                    posExamplesRelation, negExamplesRelation, clauseInfo, EvaluationFunctions.FUNCTION.COVERAGE);
        } else {
            score = clauseInfo.getScore();
        }
        return score;
    }

}
