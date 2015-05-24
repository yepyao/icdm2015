package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Dataset {
	LinkedList<CheckIn> list = new LinkedList<CheckIn>();
	HashMap<Integer, LinkedList<CheckIn>> userList = new HashMap<Integer, LinkedList<CheckIn>>();
	HashMap<String, LinkedList<CheckIn>> locList = new HashMap<String, LinkedList<CheckIn>>();
	public Dataset(String filename) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line = null;
		while ((line = in.readLine())!=null){
			CheckIn check = new CheckIn(line);
			list.add(check);
		}
		in.close();
		Collections.sort(list);
		for(CheckIn check:list){
			if (!userList.containsKey(check.userId))
				userList.put(check.userId, new LinkedList<CheckIn>());
			userList.get(check.userId).add(check);
			if (!locList.containsKey(check.locId))
				locList.put(check.locId, new LinkedList<CheckIn>());
			locList.get(check.locId).add(check);
		}
		Set<Integer> userSet = new HashSet<Integer>(userList.keySet());
		for(Integer userId:userSet){
			if (userList.get(userId).size()<5)
				userList.remove(userId);
		}
		Set<String> locSet = new HashSet<String>(locList.keySet());
		for(String locId:locSet){
			if (locList.get(locId).size()<5)
				locList.remove(locId);
		}
		Iterator<CheckIn> iter = list.iterator();
		while(iter.hasNext()){
			CheckIn check = iter.next();
			if (!userList.containsKey(check.userId)||!locList.containsKey(check.locId))
				iter.remove();
		}
		System.out.println("#checkin: "+list.size());
		System.out.println("#User: "+userList.keySet().size());
		System.out.println("#Loc: "+locList.keySet().size());
		
	}
}
