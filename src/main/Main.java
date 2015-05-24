package main;

import java.io.IOException;

import model.UserCF;
import data.Dataset;

public class Main {
	public static void main(String[] args) throws Exception {
		Dataset dataset = new Dataset("data/dataset_tsmc2014/dataset_TSMC2014_NYC.txt");
		dataset.outputUserPoints("output/userPoints.txt");
		UserCF userCF = new UserCF(dataset);
		userCF.predict();
		
	}
}