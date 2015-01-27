package estimator.pagerank;

import java.util.HashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;

import championship.ChampionshipModel;
import championship.MatchModel;
import championship.TeamModel;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.scoring.PageRankWithPriors;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import estimator.IRankingMetodh;

public class PageRank2Way implements IRankingMetodh {

	private Graph<TeamModel, MatchModel> graph = null;
	private HashMap<TeamModel, Double> ranks = null;
	private ChampionshipModel model;
	private HashMap<MatchModel, Double> edgesMap = new HashMap<MatchModel, Double>();

	public PageRank2Way(ChampionshipModel model) {
		this.model = model;
		getGraph();
		getRankings();
	}

	@Override
	public Graph<TeamModel, MatchModel> getGraph() {
		if (graph == null) {
			this.graph = new DirectedSparseGraph<TeamModel, MatchModel>();

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

				}

			}
			for (MatchModel m : graph.getEdges()) {
				double div = 0;
				for (MatchModel ma : graph.getOutEdges(m.getLoserTeamModel())) {
					div += ma.getAbsDiff();
				}
				// System.out.println(((double)m.getAbsDiff()));
				edgesMap.put(m, ((double) m.getAbsDiff()) / div);

			}
		}
		return graph;
	}

	@Override
	public HashMap<TeamModel, Double> getRankings() {
		if (ranks == null) {
			Transformer edge_weights = MapTransformer.getInstance(edgesMap);
			// System.out.println(edgesMap);
			PageRankWithPriors<TeamModel, MatchModel> ranker = new PageRank<TeamModel, MatchModel>(
					graph, edge_weights, 0.33);
			ranker.setTolerance(0.00001);
			ranker.setMaxIterations(1000);
			ranker.evaluate();

			this.ranks = new HashMap<TeamModel, Double>();
			for (TeamModel v : graph.getVertices()) {
				this.ranks.put(v, ranker.getVertexScore(v));
			}
		}
		return this.ranks;
	}

}
