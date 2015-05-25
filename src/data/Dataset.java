package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Dataset {
	LinkedList<CheckIn> list = new LinkedList<CheckIn>();
	HashMap<Integer, LinkedList<CheckIn>> userList = new HashMap<Integer, LinkedList<CheckIn>>();
	HashMap<String, LinkedList<CheckIn>> locList = new HashMap<String, LinkedList<CheckIn>>();
	int firstHour;
	int lastHour;

	public Dataset(String filename) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line = null;
		while ((line = in.readLine()) != null) {
			CheckIn check = new CheckIn(line);
			list.add(check);
		}
		in.close();
		Collections.sort(list);
		System.out.println("first time: " + list.getFirst().time.getTime()
				/ 1000 / 60 / 60);
		System.out.println("last time: " + list.getLast().time.getTime() / 1000
				/ 60 / 60);
		firstHour = (int) (list.getFirst().time.getTime() / 1000 / 60 / 60);
		lastHour = (int) (list.getLast().time.getTime() / 1000 / 60 / 60);
		for (CheckIn check : list) {
			if (!userList.containsKey(check.userId))
				userList.put(check.userId, new LinkedList<CheckIn>());
			userList.get(check.userId).add(check);
			if (!locList.containsKey(check.locId))
				locList.put(check.locId, new LinkedList<CheckIn>());
			locList.get(check.locId).add(check);
		}
		Set<Integer> userSet = new HashSet<Integer>(userList.keySet());
		for (Integer userId : userSet) {
			if (userList.get(userId).size() < 5)
				userList.remove(userId);
		}
		Set<String> locSet = new HashSet<String>(locList.keySet());
		for (String locId : locSet) {
			if (locList.get(locId).size() < 5)
				locList.remove(locId);
		}
		Iterator<CheckIn> iter = list.iterator();
		while (iter.hasNext()) {
			CheckIn check = iter.next();
			if (!userList.containsKey(check.userId)
					|| !locList.containsKey(check.locId))
				iter.remove();
		}
		System.out.println("#checkin: " + list.size());
		System.out.println("#User: " + userList.keySet().size());
		System.out.println("#Loc: " + locList.keySet().size());
		// divide
		Random rand = new Random(0);
		for (CheckIn check : list) {
			int r = rand.nextInt(8);
			if (r == 6 || r == 7)
				check.type = DataType.TEST;
			if (r == 5)
				check.type = DataType.TUNE;
		}
	}

	public void outputUserPoints(String filename) throws IOException {
		PrintStream out = new PrintStream(filename);
		for (Integer userId : userList.keySet()) {
			out.println(userId);
			for (CheckIn check : userList.get(userId))
				out.println(check.latitude + ", " + check.longitude);
			out.println("------------------");
		}
		out.close();
		System.out.println("outputUserPoints ok!");
	}

	// get
	public Set<Integer> getUserSet() {
		return userList.keySet();
	}

	public LinkedList<CheckIn> getUserCheckIns(int userId, DataType type) {
		LinkedList<CheckIn> res = new LinkedList<CheckIn>();
		for (CheckIn check : userList.get(userId)) {
			if (check.type == type)
				res.add(check);
		}
		return res;
	}

	public HashSet<String> getPosSet(LinkedList<CheckIn> checkIns, int hour) {
		HashSet<String> res = new HashSet<String>();
		for (CheckIn check : checkIns) {
			if (check.time.getTime() / 1000 / 60 / 60 %24 == hour)
				res.add(check.locId);
		}
		return res;
	}

	public int getHourLength() {
		return 24;
		//return lastHour - firstHour + 1;
	}
}