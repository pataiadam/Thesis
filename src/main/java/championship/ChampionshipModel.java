package championship;

import java.util.ArrayList;
import java.util.HashMap;

public class ChampionshipModel {

	private ArrayList<MatchModel> matches;
	private ArrayList<TeamModel> teams;

	public ChampionshipModel(ArrayList<MatchModel> matches,
			ArrayList<TeamModel> teams) {
		this.matches = matches;
		this.teams = teams;
	}

	public ArrayList<MatchModel> getMatches() {
		return matches;
	}

	public ArrayList<TeamModel> getTeams() {
		return teams;
	}

}
