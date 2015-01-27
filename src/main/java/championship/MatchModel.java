package championship;

import org.joda.time.DateTime;

public class MatchModel {
	private int id;
	private DateTime date;
	private TeamModel homeTeam;
	private TeamModel awayTeam;
	private int homeResult;
	private int awayResult;

	public MatchModel(TeamModel homeTeam, TeamModel awayTeam, int homeResult,
			int awayResult) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeResult = homeResult;
		this.awayResult = awayResult;
	}
	
	public MatchModel clone(MatchModel m){
		MatchModel n = new MatchModel();
		n.setAwayResult(m.getAwayResult());
		n.setAwayTeam(m.getAwayTeam());
		n.setHomeResult(m.getHomeResult());
		n.setHomeTeam(m.getHomeTeam());
		return n;
	}
	
	public MatchModel() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public TeamModel getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(TeamModel homeTeam) {
		this.homeTeam = homeTeam;
	}

	public TeamModel getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(TeamModel awayTeam) {
		this.awayTeam = awayTeam;
	}

	public int getHomeResult() {
		return homeResult;
	}

	public void setHomeResult(int homeResult) {
		this.homeResult = homeResult;
	}

	public int getAwayResult() {
		return awayResult;
	}

	public void setAwayResult(int awayResult) {
		this.awayResult = awayResult;
	}

	public int getDiff() {
		return this.homeResult-this.awayResult;
	}
	
	public int getAbsDiff() {
		return Math.abs(this.homeResult-this.awayResult);
	}

	public String getLoser() {
		if(homeResult==awayResult){
			return "Draw";
		}
		return homeResult<awayResult?homeTeam.getName():awayTeam.getName();
	}
	
	public TeamModel getLoserTeamModel() {
		if(homeResult==awayResult){
			return null;
		}
		return homeResult<awayResult?homeTeam:awayTeam;
	}
	
	public String getWinner() {
		if(homeResult==awayResult){
			return "Draw";
		}
		return homeResult>awayResult?homeTeam.getName():awayTeam.getName();
	}
	
	public String toString(){
		String ret = "";
		ret += this.homeTeam.getName();
		for (int i = this.homeTeam.getName().length(); i < 15; i++) {
			ret += " ";
		}
		ret += this.homeResult + " - " + this.awayResult;
		for (int i = this.awayTeam.getName().length(); i < 15; i++) {
			ret += " ";
		}
		ret += this.awayTeam.getName();/* +"  Result: " + this.getWinner();
		for (int i = this.getWinner().length(); i < 15; i++) {
			ret += " ";
		}*/
		
		return ret;
		//return getAbsDiff()+"";
	}
}
