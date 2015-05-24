package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import data.CheckIn;
import data.DataType;
import data.Dataset;

public class UserCF {
	Dataset dataset;
	UserSim userSim;

	public UserCF(Dataset dataset) {
		this.dataset = dataset;
		userSim = new UserSim(dataset);
	}

	HashMap<Integer, HashMap<String, Double>> res = new HashMap<Integer, HashMap<String, Double>>();

	public void predict() {
		Set<Integer> userSet = dataset.getUserSet();
		for (int userId : userSet)
			res.put(userId, new HashMap<String, Double>());
		for (int userId : userSet) {
			LinkedList<CheckIn> list = dataset.getUserCheckIns(userId,
					DataType.TRAIN);
			HashSet<String> loc = new HashSet<String>();
			for (CheckIn check : list)
				loc.add(check.locId);
			for (String locId : loc) {
				for (int userId2 : userSet) {
					HashMap<String, Double> r = res.get(userId2);
					if (!r.containsKey(locId))
						r.put(locId, 0.0);
					r.put(locId, r.get(locId) + userSim.sim[userId][userId2]);
				}
			}
		}
	}
}
