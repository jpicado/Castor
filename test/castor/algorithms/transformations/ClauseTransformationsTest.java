package castor.algorithms.transformations;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.hypotheses.MyClause;
import castor.utils.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ClauseTransformationsTest {

    private String testDataPath = System.getProperty("user.dir")+"/test/castor/algorithms/transformations/testData/";

    public static void main(String [] args){
        ClauseTransformationsTest ct = new ClauseTransformationsTest();
        ct.testDetermineQueryContainment();
        ct.testMinimizeClauseDefinition_1();
        ct.testMinimizeClause();
        ct.testMinimizeClauseDefinition_2();
    }

    public void testDetermineQueryContainment(){
        System.out.println("*************  testDetermineQueryContainment **********");
        String filepath = testDataPath+"ClauseTransformationsTest_input1.txt";
        List<MyClause> clauseList = new ArrayList<>();
        System.out.println("------ Input clauses ------\n");
        readTests(filepath, clauseList);
        System.out.println("--------------------- Check if Q1 is contained in Q2 ------------ \n");
        minimizeClauseDefinition_test(clauseList);
    }

    public void testMinimizeClauseDefinition_1(){
        System.out.println("\n*************  testMinimizeClauseDefinition_1 **********");
        String filepath = testDataPath+"ClauseTransformationsTest_input1.txt";
        List<MyClause> clauseList = new ArrayList<>();
        System.out.println("Before minimize : \n");
        readTests(filepath, clauseList);
        ClauseTransformations.minimizeClauseDefinition(clauseList);
        System.out.println("\nAfter minimize : \n");
        printMyClause(clauseList);
    }

    public void testMinimizeClauseDefinition_2(){
        System.out.println("\n*************  testMinimizeClauseDefinition_2 **********");
        String filepath = testDataPath+"ClauseTransformationsTest_input3.txt";
        List<MyClause> clauseList = new ArrayList<>();
        System.out.println("Before minimize : \n");
        readTests(filepath, clauseList);
        ClauseTransformations.minimizeClauseDefinition(clauseList);
        System.out.println("\nAfter minimize : \n");
        printMyClause(clauseList);
    }

    public void testMinimizeClause(){
        System.out.println("\n*************  testMinimizeClause **********");
        String filepath = testDataPath+"ClauseTransformationsTest_input2.txt";
        List<MyClause> clauseList = new ArrayList<>();
        System.out.println("Before minimize : \n");
        readTests(filepath, clauseList);
        ClauseTransformations.minimizeClause(clauseList.get(0));
        System.out.println("\nAfter minimize : \n");
        printMyClause(clauseList);
    }


    public void minimizeClauseDefinition_test(List<MyClause> clauseList){
        //check if q1 is contained in q1
        Iterator<MyClause> iterator = clauseList.iterator();
        while(iterator.hasNext()){
            MyClause myClause1 = (MyClause) iterator.next();
            for(MyClause myClause2: clauseList){
                if(!myClause1.equals(myClause2)){

                    List<MyClause> temp = new ArrayList<>();
                    temp.add(myClause1);
                    temp.add(myClause2);
                    printMyClause(temp);

                    boolean isQueryContained = ClauseTransformations.determineQueryContainment(myClause1,myClause2);
                    System.out.println(" ---- "+isQueryContained);
                }
            }
        }
    }

    public void mockClause(String line, List<MyClause> clauseList){
        System.out.println(line);
        String [] lineArray = line.split(":-");
        String [] literals = lineArray[1].split("\\)");

        Set<Literal> q1Literal = new LinkedHashSet<Literal>();
        for(String relation: literals){
            String relationName = relation.substring(0, relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()));
            if (relationName.startsWith(Constants.TransformDelimeter.COMMA.getValue())) {
                relationName = relation.substring(1, relationName.length());
            }

            String attributes = (String) relation.subSequence(relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()) + 1, relation.length());
            String[] attributeArray = attributes.split(Constants.TransformDelimeter.COMMA.getValue());

            q1Literal.add(getLiteral(relationName, attributeArray));

        }
        MyClause q1 = new MyClause(q1Literal);
        clauseList.add(q1);
    }

    private  void readTests(String transformFile, List<MyClause> clauseList) {
        Stream<String> stream = null;
        try {
            stream = Files.lines(Paths.get(transformFile));
            {
                stream.filter(s -> !s.isEmpty()).forEach(s -> mockClause(s,clauseList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stream.close();
    }

    public static  void printMyClause(List<MyClause> clauseList){
        for(MyClause clause: clauseList){
            Set<Literal> literals = clause.getLiterals();
            for(Literal literal: literals){
                System.out.print(literal.toString().trim()+" ");
            }
            System.out.println();
        }
    }

    public Literal getLiteral(String pred, String... values) {
        List<Term> lsNewTerm = new ArrayList<>();
        for (String str : values) {
            if(!str.startsWith("V"))
                lsNewTerm.add(new Constant(str));
            else
                lsNewTerm.add(new Variable(str));
        }
        Predicate p = new Predicate(pred, lsNewTerm);
        return new Literal(p);
    }
}