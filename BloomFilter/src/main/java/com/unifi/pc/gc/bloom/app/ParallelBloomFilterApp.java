package com.unifi.pc.gc.bloom.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unifi.pc.gc.bloom.core.BloomFilter;
import com.unifi.pc.gc.bloom.core.ParallelBloomFilter;
import com.unifi.pc.gc.bloom.utility.HashFunction;
import com.unifi.pc.gc.bloom.utility.StringHashFunction;
import com.unifi.pc.gc.bloom.utility.Tools;

public class ParallelBloomFilterApp {

	private static String fileDataFlowName;
	private static String fileSampleName;
	private static int n;
	private static int numberOfThreads;

	private static String filepath;
	private static List<String> sample, dataFlow;
	private static List<HashFunction<String>> myHashFunctionList;

	private static void initializeApp() {
		File f = new File(System.getProperty("user.dir"));
		File dir = f.getAbsoluteFile();
		filepath = dir.toString();
		filepath=filepath+"/";

		dataFlow = Tools.readFile(filepath + fileDataFlowName);
		sample = Tools.readFile(filepath+fileSampleName);

		myHashFunctionList = new ArrayList<HashFunction<String>>();
		myHashFunctionList.add(new StringHashFunction<String>(n));

	}

	public static void main(String[] args) {
		//args[]={numberOfThreads, n, fileSampleName, fileDataFlowName}
		//example: 10 100 sample.txt people.txt
		
		if(args.length<4) {
			System.err.println("args must be in the form: args[]={numberOfThreads, n, fileSampleName, fileDataFlowName}");
			System.exit(-1);
		}
		numberOfThreads=Integer.parseInt(args[0]);
		n=Integer.parseInt(args[1]);
		fileSampleName=args[2];
		fileDataFlowName=args[3];
		
		initializeApp();
		BloomFilter<String> filter = new BloomFilter<>(n, myHashFunctionList);
		ParallelBloomFilter pfilter = new ParallelBloomFilter(filter, sample, dataFlow, numberOfThreads);
		Thread myThread=new Thread(pfilter);
		
		
		
		long beginExecution=System.currentTimeMillis();
		
		
		
		
		myThread.start();
		
		while(myThread.isAlive());
		
		
		
		
		
		long endExecution=System.currentTimeMillis();
		

		
		
		int dimensionOfMap = n;
		int numberOfFunctions = myHashFunctionList.size();
		int dimensionOfSample = sample.size();
		int numberOfElementsAnalyzed = dataFlow.size();

		int numberOfOnes = filter.getNumberOfOnes();
		int numberOfElementsRejected=pfilter.getRejectedNumber();
		int numberOfElementsAdmitted=pfilter.getAdmittedNumber();
		long executionTime=endExecution-beginExecution;
		

		String result = "Parallel execution Results:\n";
		result = result
				+ "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
		result=result+"Number of threads: "+ numberOfThreads+"\n";
		result = result + "Number of functions utilized: " + numberOfFunctions + "\t" + "Dimension of sample: "
				+ dimensionOfSample + "\t" + "Dimension of vector of bits: " + dimensionOfMap + "\n";
		result = result + "Number of elements analyzed: " + numberOfElementsAnalyzed + "\t" + "Number of ones: "
				+ numberOfOnes + "\n\n";
		result = result + "Number of elements admitted: " + numberOfElementsAdmitted + "\t"
				+ "Number of elements rejected: " + numberOfElementsRejected + "\n\n\n";
		
		result=result+"Execution time: "+ executionTime+" ns\n\n";

		System.out.println(result);
	}

}
