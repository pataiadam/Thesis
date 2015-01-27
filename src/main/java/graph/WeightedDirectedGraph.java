package graph;

import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class WeightedDirectedGraph<V, E> extends DirectedSparseGraph<V, E> {

	private Map<E, Number> edgesWeights;
	
	public WeightedDirectedGraph(){
		super();
		this.edgesWeights=new HashMap<E, Number>();
	}

	public boolean addEdge(E edge, Pair<? extends V> endpoints,
			EdgeType edgeType, Number weight) {
		edgesWeights.put(edge, weight);
		return super.addEdge(edge, endpoints, edgeType);
	}
	
	public Number getEdgeWeight(E edge){
		return edgesWeights.get(edge);
	}
	
	public void setEdgeWeight(E edge, Number weight){
		edgesWeights.put(edge, weight);
	}

	public Map getEdgeWeights() {
		return edgesWeights;
	}

}
