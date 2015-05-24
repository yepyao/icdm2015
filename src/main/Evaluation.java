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
		for(int userId: userSet){
			double pos5 = 0;
			double pos10 = 0;
			double pos20 = 0;
			LinkedList<CheckIn> checkIns = dataset.getUserCheckIns(userId, DataType.TEST);
			HashSet<String> posSet = new HashSet<String>();
			for(CheckIn check: checkIns) posSet.add(check.locId);
			
			HashMap<String, Double> pred = res.get(userId);
			ArrayList<PredPoint> list = new ArrayList<PredPoint>();
			for(String locId:pred.keySet()){
				list.add(new PredPoint(locId, pred.get(locId)));
			}
			Collections.sort(list);
			for(int i=0;i<20;i++){
				String locId = list.get(i).locId;
				if (posSet.contains(locId)){
					if (i<5) pos5++;
					if (i<10) pos10++;
					if (i<20) pos20++;
				}
			}
			pre5 += pos5/5;
			pre10 += pos10/10;
			pre20 += pos20/20;
			
			rec5 += pos5/posSet.size();
			rec10 += pos10/posSet.size();
			rec20 += pos20/posSet.size();
		}
		int userNum = userSet.size();
		System.out.println("pre 5:"+pre5/userNum);
		System.out.println("pre10:"+pre10/userNum);
		System.out.println("pre20:"+pre20/userNum);
		System.out.println("rec 5:"+rec5/userNum);
		System.out.println("rec10:"+rec10/userNum);
		System.out.println("rec20:"+rec20/userNum);
	}

}
class PredPoint implements Comparable<PredPoint>{
	String locId;
	double res;
	public PredPoint(String locId, double res) {
		this.locId = locId;
		this.res = res;
	}
	@Override
	public int compareTo(PredPoint o) {
		if (res == o.res) return 0;
		return (res>o.res)?-1:1;
	}
	
}
