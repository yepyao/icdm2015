package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import data.CheckIn;
import data.DataType;
import data.Dataset;

public class Evaluation {

	public static void evaluateUserCF(Dataset dataset,
			HashMap<Integer, HashMap<String, Double>> res) {
		System.out.println("Evaluating...");
		Set<Integer> userSet = dataset.getUserSet();

		HashMap<Integer,ArrayList<PredPoint>> cache = new HashMap<Integer, ArrayList<PredPoint>>();
		for (int userId : userSet) {
			//System.out.println("userId: " +userId );
			HashMap<String, Double> pred = res.get(userId);
			LinkedList<PredPoint> list = new LinkedList<PredPoint>();
			for (String locId : pred.keySet()) {
				list.add(new PredPoint(locId, pred.get(locId)));
			}
			Collections.sort(list);
			ArrayList<PredPoint> subList = new ArrayList<PredPoint>();
			Iterator<PredPoint> iter = list.iterator();
			for(int i=0;i<20;i++) subList.add(iter.next());
			cache.put(userId, subList);
		}

		double pre5 = 0;
		double pre10 = 0;
		double pre20 = 0;
		double rec5 = 0;
		double rec10 = 0;
		double rec20 = 0;

		int hours = dataset.getHourLength();
		int validHours = 0;
		for (int h = 0; h < hours; h++) {
			//System.out.println("Hour:" + h);
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
				ArrayList<PredPoint> list = cache.get(userId);

				LinkedList<CheckIn> checkIns = dataset.getUserCheckIns(userId,
						DataType.TEST);
				HashSet<String> posSet = dataset.getPosSet(checkIns, h);

				int pos5 = 0;
				int pos10 = 0;
				int pos20 = 0;
				for (int i = 0; i < 20; i++) {
					String locId = list.get(i).locId;
					if (posSet.contains(locId)) {
						if (i < 5)
							pos5++;
						if (i < 10)
							pos10++;
						if (i < 20)
							pos20++;
					}
				}
				tp5 += pos5;
				tp10 += pos10;
				tp20 += pos20;

				fp5 += 5 - pos5;
				fp10 += 10 - pos10;
				fp20 += 20 - pos20;

				fn5 += posSet.size() - pos5;
				fn10 += posSet.size() - pos10;
				fn20 += posSet.size() - pos20;

			}

			pre5 += tp5 / (tp5 + fp5);
			pre10 += tp10 / (tp10 + fp10);
			pre20 += tp20 / (tp20 + fp20);

			rec5 += tp5 / (tp5 + fn5);
			rec10 += tp10 / (tp10 + fn10);
			rec20 += tp20 / (tp20 + fn20);
		}

		// int userNum = userSet.size();
		System.out.println("pre 5:" + pre5 / hours);
		System.out.println("pre10:" + pre10 / hours);
		System.out.println("pre20:" + pre20 / hours);
		System.out.println("rec 5:" + rec5 / hours);
		System.out.println("rec10:" + rec10 / hours);
		System.out.println("rec20:" + rec20 / hours);
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
