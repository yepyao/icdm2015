package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import data.CheckIn;
import data.DataType;
import data.Dataset;

public class UserTempSim {
	double[][] sim;

	public UserTempSim(Dataset dataset) {
		int totalHours = dataset.getHourLength();
		Set<Integer> userSet = dataset.getUserSet();
		int userNum = userSet.size();
		sim = new double[userNum + 1][userNum + 1];

		for (int user1 : userSet) {
			LinkedList<CheckIn> list1 = dataset.getUserCheckIns(user1,
					DataType.TRAIN);
			int size1 = list1.size();
			for (int user2 : userSet) {
				if (user1 < user2) {
					LinkedList<CheckIn> list2 = dataset.getUserCheckIns(user2,
							DataType.TRAIN);
					
					int size2 = list2.size();
					int com = 0;
					for (int h = 0; h < totalHours; h++) {
						HashSet<String> loc1 = new HashSet<String>();
						for (CheckIn check : list1)
							if (check.hour % totalHours == h)
								loc1.add(check.locId);

						HashSet<String> loc2 = new HashSet<String>();
						for (CheckIn check : list2)
							if (check.hour % totalHours == h)
								loc2.add(check.locId);
						
						
						for (String locId : loc2) {
							if (loc1.contains(locId))
								com++;
						}
					}
					double sim12 = com / (Math.sqrt(size1) * Math.sqrt(size2));
					sim[user1][user2] = sim12;
					sim[user2][user1] = sim12;

				}
			}
		}
		System.out.println("user sim ok!");
	}
}
