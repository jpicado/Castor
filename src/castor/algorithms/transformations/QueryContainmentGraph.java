package castor.algorithms.transformations;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryContainmentGraph {

    private List<Edge> allEdges;
    private List<Vertex> allVertex;
    private boolean isDirected = false;

    QueryContainmentGraph(boolean isDirected){
        allEdges = new ArrayList<Edge>();
        allVertex = new ArrayList<>();
        this.isDirected = isDirected;
    }

    public void addEdge(Vertex vertex1, Vertex vertex2){
        Edge edge = new Edge(vertex1,vertex2,isDirected);
        allEdges.add(edge);
        vertex1.addAdjacentVertex(edge, vertex2);

        if(!isDirected){
            vertex2.addAdjacentVertex(edge, vertex1);
        }
    }

    public Vertex createNewVertex(String name, Literal literal, List<Literal> literalSet, Map<Integer,Constant> constMap){
        Vertex vertex =  new Vertex(name,literal,literalSet,constMap);
        allVertex.add(vertex);
        return vertex;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void setAllEdges(List<Edge> allEdges) {
        this.allEdges = allEdges;
    }

    public List<Vertex> getAllVertex() {
        return allVertex;
    }

    public void setAllVertex(List<Vertex> allVertex) {
        this.allVertex = allVertex;
    }
}

class Vertex{
    private String name;
    private Literal literal;
    private List<Literal> literalList;
    private List<Edge> edges;
    private Vertex adjacentVertex;
    private Map<Variable, Term> thetaMap;
    private Map<Integer,Constant> constantMap;

    Vertex(String name, Literal literal, List<Literal> literalList, Map<Integer,Constant> constMap){
        this.name = name;
        this.literal = literal;
        this.literalList = literalList;
        this.edges = new ArrayList<>();
        this.thetaMap = new HashMap<>();
        this.constantMap = constMap;
    }

    public Map<Integer, Constant> getConstantMap() {
        return constantMap;
    }

    public void setConstantMap(Map<Integer, Constant> constantMap) {
        this.constantMap = constantMap;
    }

    public Map<Variable, Term> getThetaMap() {
        return thetaMap;
    }

    public void setThetaMap(Map<Variable, Term> thetaMap) {
        this.thetaMap = thetaMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Literal getLiteral() {
        return literal;
    }

    public void setLiteral(Literal literal) {
        this.literal = literal;
    }

    public List<Literal> getLiteralList() {
        return literalList;
    }

    public void setLiteralList(List<Literal> literalList) {
        this.literalList = literalList;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Vertex getAdjacentVertex() {
        return adjacentVertex;
    }

    public void setAdjacentVertex(Vertex adjacentVertex) {
        this.adjacentVertex = adjacentVertex;
    }

    public void addAdjacentVertex(Edge e, Vertex v){
        this.edges.add(e);
        this.setAdjacentVertex(v);
    }
}


class Edge{
    private Vertex vertex1;
    private Vertex vertex2;

    Edge(Vertex vertex1, Vertex vertex2,boolean isDirected){
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public Vertex getVertex1(){
        return vertex1;
    }

    public Vertex getVertex2(){
        return vertex2;
    }
}


