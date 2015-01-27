package estimator;

import championship.MatchModel;

public class EstimateModel {

	private String home;
	private String away;
	private double probHomeWin;
	private double probAwayWin;
	// null if we dont know it
	private String winner;
	private MatchModel match;
	private double entropy;

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getAway() {
		return away;
	}

	public void setAway(String away) {
		this.away = away;
	}

	public double getProbHomeWin() {
		return probHomeWin;
	}

	public void setProbHomeWin(double probHomeWin) {
		this.probHomeWin = probHomeWin;
	}

	public double getProbAwayWin() {
		return probAwayWin;
	}

	public void setProbAwayWin(double probAwayWin) {
		this.probAwayWin = probAwayWin;
	}

	public String getEstimate() {
		return (probHomeWin >= probAwayWin ? home : away);
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	/**
	 * 
	 * @param drawIsCorrect
	 *            - if true, then draw result returns true
	 * @return
	 */
	public boolean isItCorrectEstimation(boolean drawIsCorrect){
		if(getWinner() == null){
			return drawIsCorrect;
		}else{
			if(getWinner().equals(home) || getWinner().equals(away)){
				return getWinner().equals(getEstimate())?true:false;
			}else{
				return drawIsCorrect;
			}
		}
	}

	public String toString() {
		return match+" Estimate: "+(probHomeWin >= probAwayWin ? home+" | "+probHomeWin : away+" | "+probAwayWin);
	}

	public void setMatchModel(MatchModel m) {
		this.match=m;
	}

	public void setEntropy(double entropy) {
		this.entropy=entropy;
		
	}
	
	public double getEntropy(){
		return this.entropy;
	}
}
