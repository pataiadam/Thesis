package estimator.pagerank;

import java.util.HashMap;
import java.util.function.Consumer;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;

import championship.ChampionshipModel;
import championship.MatchModel;
import championship.TeamModel;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import estimator.IRankingMetodh;
import graph.WeightedDirectedGraph;

public class PageRankWDraw implements IRankingMetodh {

	private WeightedDirectedGraph<TeamModel, MatchModel> graph = null;
	private HashMap<TeamModel, Double> ranks = null;
	private ChampionshipModel model;

	public PageRankWDraw(ChampionshipModel model) {
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
				double weight = 0;
				double ret = 0;
				for (MatchModel ma : graph.getOutEdges(graph.getSource(m))) {
					if (ma.getAbsDiff() == 0) {
						double thisWins = graph.getInEdges(graph.getSource(ma))
								.size();
						double thatWins = graph.getInEdges(graph.getDest(ma))
								.size();
						ret += thisWins / (thisWins + thatWins);
					} else {
						ret += ma.getAbsDiff();
					}
				}
				if (m.getAbsDiff() == 0) {
					double thisWins = graph.getInEdges(graph.getSource(m))
							.size();
					double thatWins = graph.getInEdges(graph.getDest(m)).size();
					double k = thisWins / (thisWins + thatWins);

					weight = (k) / ret;
				} else {
					weight = (m.getAbsDiff()) / ret;
				}
				graph.setEdgeWeight(m, weight);
			}

		}
		return graph;
	}

	@Override
	public HashMap<TeamModel, Double> getRankings() {
		if (ranks == null) {
			Transformer edge_weights = MapTransformer.getInstance(graph.getEdgeWeights());
			PageRank<TeamModel, MatchModel> ranker = new PageRank<TeamModel, MatchModel>(
					graph, edge_weights, 0.33);
			ranker.setTolerance(0.0001);
			ranker.setMaxIterations(100);
			ranker.evaluate();

			this.ranks = new HashMap<TeamModel, Double>();
			for (TeamModel v : graph.getVertices()) {
				this.ranks.put(v, ranker.getVertexScore(v));
			}
		}
		return this.ranks;
	}

	private class MatchWeightTransformer implements
			Transformer<MatchModel, Number> {
		public Number transform(MatchModel m) {
			return graph.getEdgeWeight(m);
		}
	}
}
