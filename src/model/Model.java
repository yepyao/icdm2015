package model;

import java.util.HashMap;

public interface Model {
	public HashMap<Integer, HashMap<String, Double>> predict(int hour, int totalHours);
}
