package championship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;

public class ChampionshipService {
	private String resourcePath;
	private ArrayList<MatchModel> matches;
	private ArrayList<TeamModel> teams;
	private boolean firstLine = true;
	private int id = 0;
	private int toN = -1;
	private int fromN;
	private int teamId;

	public ChampionshipService(String resource) {
		this.resourcePath = resource;
		this.matches = new ArrayList<MatchModel>();
		this.teams = new ArrayList<TeamModel>();
		try {
			readFileToList();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public ChampionshipService(String resource, int fromNMatch, int toNMatch) {
		this.resourcePath = resource;
		this.matches = new ArrayList<MatchModel>();
		this.teams = new ArrayList<TeamModel>();
		this.fromN = fromNMatch;
		this.toN = toNMatch;
		try {
			readFileToList();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void readFileToList() throws IOException, URISyntaxException {
		URL url = new URL(resourcePath);
		URLConnection con = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String l;
		int count = 0;
		while ((l = in.readLine()) != null) {
			if (!firstLine && count >= fromN) {
				createMatch(l);
				if (count == toN)
					break;
			}
			if (firstLine) {
				firstLine = false;
			}
			count++;

		}
	}

	private void createMatch(String line) {
		String[] s = line.split(",");
		MatchModel m = new MatchModel();
		m.setId(id);
		id++;
		m.setDate(new DateTime(Integer.parseInt("20" + s[1].split("/")[2]),
				Integer.parseInt(s[1].split("/")[1]), Integer.parseInt(s[1]
						.split("/")[0]), 0, 0, 0));
		String home = s[2];
		boolean homeIsIn = false;
		String away = s[3];
		boolean awayIsIn = false;
		for (TeamModel t : teams) {
			if (t.getName().equals(home)) {
				m.setHomeTeam(t);
				homeIsIn = true;
			}
			if (t.getName().equals(away)) {
				m.setAwayTeam(t);
				awayIsIn = true;
			}
		}
		if(!homeIsIn){
			TeamModel t=new TeamModel(home, teamId);
			teams.add(t);
			m.setHomeTeam(t);
			teamId++;
		}
		if(!awayIsIn){
			TeamModel t=new TeamModel(away, teamId);
			teams.add(t);
			m.setAwayTeam(t);
			teamId++;
		}

		m.setHomeResult(Integer.parseInt(s[4]));
		m.setAwayResult(Integer.parseInt(s[5]));
		matches.add(m);
	}

	public ChampionshipModel getChampionshipModel() {
		return new ChampionshipModel(this.matches, this.teams);
	}
}
