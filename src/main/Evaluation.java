package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import data.CheckIn;
import data.DataType;
import data.Dataset;

public class Evaluation {

	public static void evaluateUserCF(Dataset dataset,
			HashMap<Integer, HashMap<String, Double>> res) {
		Set<Integer> userSet = dataset.getUserSet();
		double pre5 = 0;
		double pre10 = 0;
		double pre20 = 0;
		double rec5 = 0;
		double rec10 = 0;
		double rec20 = 0;

		int hours = dataset.getHourLength();
		int validHours = 0;
		for (int h = 0; h < hours; h++) {
			double tp5 = 0;
			double tp10 = 0;
			double tp20 = 0;

			double fp5 = 0;
			double fp10 = 0;
			double fp20 = 0;

			double fn5 = 0;
			double fn10 = 0;
			double fn20 = 0;
			for (int userId : userSet) {
				HashMap<String, Double> pred = res.get(userId);
				ArrayList<PredPoint> list = new ArrayList<PredPoint>();
				for (String locId : pred.keySet()) {
					list.add(new PredPoint(locId, pred.get(locId)));
				}
				Collections.sort(list);

				LinkedList<CheckIn> checkIns = dataset.getUserCheckIns(userId,
						DataType.TEST);
				HashSet<String> posSet = dataset.getPosSet(checkIns, h);
				if (posSet.size() == 0)
					continue;
				validHours++;

				for (int i = 0; i < 20; i++) {
					String locId = list.get(i).locId;
					if (posSet.contains(locId)) {
						if (i < 5)
							tp5++;
						if (i < 10)
							tp10++;
						if (i < 20)
							tp20++;
					}
				}
				fp5 += 5 - tp5;
				fp10 += 10 - tp10;
				fp20 += 20 - tp20;

				fn5 += posSet.size() - tp5;
				fn10 += posSet.size() - tp10;
				fn20 += posSet.size() - tp20;

			}

			pre5 += tp5 / (tp5 + fp5);
			pre10 += tp10 / (tp10 + fp10);
			pre20 += tp20 / (tp20 + fp20);

			rec5 += tp5 / (tp5 + fn5);
			rec10 += tp10 / (tp10 + fn10);
			rec20 += tp20 / (tp20 + fn20);
		}

		// int userNum = userSet.size();
		System.out.println("pre 5:" + pre5 / validHours);
		System.out.println("pre10:" + pre10 / validHours);
		System.out.println("pre20:" + pre20 / validHours);
		System.out.println("rec 5:" + rec5 / validHours);
		System.out.println("rec10:" + rec10 / validHours);
		System.out.println("rec20:" + rec20 / validHours);
	}

}

class PredPoint implements Comparable<PredPoint> {
	String locId;
	double res;

	public PredPoint(String locId, double res) {
		this.locId = locId;
		this.res = res;
	}

	@Override
	public int compareTo(PredPoint o) {
		if (res == o.res)
			return 0;
		return (res > o.res) ? -1 : 1;
	}

}
