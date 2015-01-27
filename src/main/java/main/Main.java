package main;

import java.util.ArrayList;

import championship.ChampionshipService;
import championship.MatchModel;
import championship.TeamModel;
import edu.uci.ics.jung.graph.Graph;
import estimator.EstimateModel;
import estimator.Estimator;
import estimator.IRankingMetodh;
import estimator.pagerank.PageRank2Way;
import estimator.pagerank.PageRankWDraw;
import estimator.report.ContentContainer;
import estimator.report.EstimatorReport;
import estimator.weighted.WeightedMethod;

public class Main {

	static ArrayList<ContentContainer> ic = new ArrayList<ContentContainer>();

	public static void main(String[] args) {
		String data;
		if(args.length>0)
			data = args[0];
		else
			data = "http://www.football-data.co.uk/mmz4281/1314/E0.csv";
		EstimatorReport r = new EstimatorReport();
		//TODO: turns from args
		int matchesPerTurn = 10;
		int sumOfTurns = 38;
		int turns = 1;

		while (sumOfTurns >= turns) {
			ChampionshipService allMatches = new ChampionshipService(data, 0,
					turns * matchesPerTurn);
			IRankingMetodh ranking = new PageRank2Way(
					allMatches.getChampionshipModel());

			ChampionshipService nextMatches = new ChampionshipService(data,
					turns * matchesPerTurn + 1, (turns + 1) * matchesPerTurn);

			Estimator estimator = new Estimator(ranking,
					nextMatches.getChampionshipModel());
			
			//TODO: report from args
			r.printReportToConsole(estimator, turns+"");
			r.addToPdfDocument(estimator, "Turn "+turns+":");
			turns++;
		}
		//TODO: name from args
		r.writeToPDF("riport");
	}

}
