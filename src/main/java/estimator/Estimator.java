package estimator;

import java.util.ArrayList;
import java.util.HashMap;

import championship.ChampionshipModel;
import championship.MatchModel;
import championship.TeamModel;

public class Estimator {

	private IRankingMetodh ranking;
	private ChampionshipModel nextMatches;
	private double correctionRateWDraw;
	private double correctionRateWODraw;
	private ArrayList<EstimateModel> ests;

	public Estimator(IRankingMetodh ranking, ChampionshipModel nextMatches) {
		this.ranking = ranking;
		this.nextMatches = nextMatches;
	}

	public ArrayList<EstimateModel> getEstimations() {
		if (ests == null) {
			ests = new ArrayList<EstimateModel>();
			double n = 0;
			double sumwd = 0;
			double sumwod = 0;
			for (MatchModel m : nextMatches.getMatches()) {
				EstimateModel e = new EstimateModel();
				String home = m.getHomeTeam().getName();
				String away = m.getAwayTeam().getName();
				double probHomeWin = probabilityAbeatsB(home, away);
				double probAwayWin = probabilityAbeatsB(away, home);

				e.setHome(home);
				e.setAway(away);
				e.setProbHomeWin(probHomeWin);
				e.setProbAwayWin(probAwayWin);
				e.setEntropy(getEntropy(probHomeWin, probAwayWin));
				e.setMatchModel(m);
				e.setWinner(m.getWinner());

				sumwd += e.isItCorrectEstimation(true) ? 1 : 0;
				sumwod += e.isItCorrectEstimation(false) ? 1 : 0;
				n++;

				ests.add(e);
			}

			setCorrectionRateWDraw(sumwd / n);
			setCorrectionRateWODraw(sumwod / n);
		}
		return ests;
	}

	private double probabilityAbeatsB(String A, String B) {
		HashMap<TeamModel, Double> rankings = ranking.getRankings();
		double rA = 1;
		double rB = 1;
		for (TeamModel t : rankings.keySet()) {
			if (t.getName().equals(A)) {
				rA = rankings.get(t);
			} else if (t.getName().equals(B)) {
				rB = rankings.get(t);
			}
		}
		return rA / (rA + rB);
	}

	public double getEntropy() {
		double result = 0;
		for (double r : ranking.getRankings().values()) {
			result -= r * (Math.log(r) / Math.log(2));
		}
		return result;
	}

	public double getEntropy(double a, double b) {
		double result = 0;
		result -= a * (Math.log(a) / Math.log(2));
		result -= b * (Math.log(b) / Math.log(2));
		return result;
	}
	
	public double getCorrectionRateWDraw() {
		return correctionRateWDraw;
	}

	public void setCorrectionRateWDraw(double correctionRateWDraw) {
		this.correctionRateWDraw = correctionRateWDraw;
	}

	public IRankingMetodh getRanking() {
		return ranking;
	}

	public double getCorrectionRateWODraw() {
		return correctionRateWODraw;
	}

	public void setCorrectionRateWODraw(double correctionRateWODraw) {
		this.correctionRateWODraw = correctionRateWODraw;
	}

}
