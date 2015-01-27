package estimator.weighted;

import java.util.HashMap;
import java.util.function.BiConsumer;

import championship.ChampionshipModel;
import championship.MatchModel;
import championship.TeamModel;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import estimator.IRankingMetodh;
import graph.WeightedDirectedGraph;

public class WeightedMethod implements IRankingMetodh {

	private WeightedDirectedGraph<TeamModel, MatchModel> graph = null;
	private HashMap<TeamModel, Double> ranks = null;
	private ChampionshipModel model;

	public WeightedMethod(ChampionshipModel model) {
		this.model = model;
		getGraph();
		getRankings();
	}

	@Override
	public Graph<TeamModel, MatchModel> getGraph() {
		if (graph == null) {
			this.graph = new WeightedDirectedGraph<TeamModel, MatchModel>();

			for (TeamModel team : model.getTeams()) {
				graph.addVertex(team);
			}

			for (MatchModel m : model.getMatches()) {
				// home win
				if (m.getDiff() < 0) {
					graph.addEdge(m, m.getHomeTeam(), m.getAwayTeam(),
							EdgeType.DIRECTED);
				}// away win
				else if (m.getDiff() > 0) {
					graph.addEdge(m, m.getAwayTeam(), m.getHomeTeam(),
							EdgeType.DIRECTED);
				}// draw
				else {
					graph.addEdge(m, m.getAwayTeam(), m.getHomeTeam(),
							EdgeType.DIRECTED);
					graph.addEdge(m.clone(m), m.getHomeTeam(), m.getAwayTeam(),
							EdgeType.DIRECTED);
				}
			}

			for (MatchModel m : graph.getEdges()) {
				graph.setEdgeWeight(m, (double) (m.getAbsDiff() + 1));
			}

		}
		return graph;
	}

	@Override
	public HashMap<TeamModel, Double> getRankings() {
		if (ranks == null) {
			ranks = new HashMap<TeamModel, Double>();
			double div=0.0;
			HashMap<TeamModel, Double> tmp = new HashMap<TeamModel, Double>();
			for (TeamModel t : graph.getVertices()) {
				double sum=0;
				double win=0;
				for(MatchModel m : graph.getOutEdges(t)){
					sum+=(double)graph.getEdgeWeight(m);
				}
				for(MatchModel m : graph.getInEdges(t)){
					sum+=(double)graph.getEdgeWeight(m);
					win+=(double)graph.getEdgeWeight(m);
				}
				tmp.put(t, (win+1)/(sum+1));
				div+=(win+1)/(sum+1);
			}
			
			//normalize
			for(TeamModel t : tmp.keySet()){
				ranks.put(t, tmp.get(t)/div);
			}
		}
		return ranks;
	}

}
