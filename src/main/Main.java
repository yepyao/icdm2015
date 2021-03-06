package main;

import java.io.IOException;
import java.util.HashMap;

import model.UserCF;
import model.UserCFTemp;
import data.Dataset;

public class Main {
	public static void main(String[] args) throws Exception {
		Dataset dataset = new Dataset(
				"data/dataset_tsmc2014/dataset_TSMC2014_NYC.txt");
				//"data/dataset_tsmc2014/dataset_TSMC2014_TKY.txt");
		dataset.outputUserPoints("output/userPoints.txt");
		//UserCF userCF = new UserCF(dataset);
		UserCFTemp userCF = new UserCFTemp(dataset);
		Evaluation.evaluateUserCF(dataset, userCF);

	}
}
