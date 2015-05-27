package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckIn implements Comparable<CheckIn>{
	SimpleDateFormat timeParser=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
	public CheckIn(String line) throws Exception {
		String[] arr = line.split("\\t");
		if (arr.length!=8) throw new RuntimeException("wrong input line for checkin");
		userId = Integer.parseInt(arr[0]);
		locId = arr[1];
		locCat = arr[2];
		locCatName = arr[3];
		latitude = Double.parseDouble(arr[4]);
		longitude = Double.parseDouble(arr[5]);
		time = timeParser.parse(arr[7]);
		//System.out.println(timeParser.format(time));
		hour = (int)(time.getTime()/1000/60/60);
	}
	public int userId;
	public String locId;
	String locCat;
	String locCatName;
	public double latitude;
	public double longitude;
	public Date time;
	public int hour;
	//divide train(0) test(1) and tune(2)
	DataType type = DataType.TRAIN;
	@Override
	public int compareTo(CheckIn o) {
		return this.time.compareTo(o.time);
	}
}
