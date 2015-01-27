package estimator;

import java.util.HashMap;

import championship.ChampionshipModel;
import championship.MatchModel;
import championship.TeamModel;
import edu.uci.ics.jung.graph.Graph;

public interface IRankingMetodh {
	
	public Graph<TeamModel, MatchModel> getGraph();
	public HashMap<TeamModel, Double> getRankings();

}
