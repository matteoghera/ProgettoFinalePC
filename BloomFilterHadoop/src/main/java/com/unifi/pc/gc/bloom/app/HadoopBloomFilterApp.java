package com.unifi.pc.gc.bloom.app;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import com.unifi.pc.gc.bloom.core.AnalyzeBloomFilterMapReduce;
import com.unifi.pc.gc.bloom.core.InitializeBloomFilterMapReduce;
import com.unifi.pc.gc.bloom.utility.HashFunction;
import com.unifi.pc.gc.bloom.utility.StringHashFunction;

public class HadoopBloomFilterApp {

	public static void main(String[] args) throws Exception {
		//args[]={n, pathSample, pathDataFlow, pathOutputDirectoryHdfs, LocalPathOutputDirectory}
		
		if(args.length<5) {
			System.err.println("args must be in the form: args[]={numberOfThreads, n, fileSampleName, fileDataFlowName}");
			System.exit(-1);
		}
		
		//Bloom filter creation
		
		int n=Integer.parseInt(args[0]);
		InitializeBloomFilterMapReduce.setN(n);
		
		List<HashFunction<Object>> myHashFunctionList = new ArrayList<HashFunction<Object>>();
		myHashFunctionList.add(new StringHashFunction<Object>(n));
		InitializeBloomFilterMapReduce.setMyHashFunctionList(myHashFunctionList);
		InitializeBloomFilterMapReduce.createBloomFilter();
		
		long beginExecution=System.currentTimeMillis();
		
		//Initialization phase
		
		int res=0;
		String[] initializationArgs= {args[1], args[3]}; 
		res=ToolRunner.run(new Configuration(),
					new InitializeBloomFilterMapReduce(),
					initializationArgs);
		while(res==0);
		
		res=0;
		
		//Analysis phase
		
		AnalyzeBloomFilterMapReduce.setMyBloomFilter(InitializeBloomFilterMapReduce.getMyBloomFilter());
		
		String[] analysisArgs= {args[2], args[3]+"/bloomfilter"};
		res=ToolRunner.run(new Configuration(),
				new AnalyzeBloomFilterMapReduce(),
				analysisArgs);
		while(res==0);
		
		long endExecution=System.currentTimeMillis();
		long executionTime=endExecution-beginExecution;
		
		//Report the time of execution
		
		try {
			FileWriter fw=new FileWriter(args[4]+"/bloomfilter_time", true);
			PrintWriter out=new PrintWriter(fw);
			String row=new String(new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(Calendar.getInstance().getTime())+" Execution Time: "+executionTime);
			out.println(row);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("File writing error");
			System.exit(-1);
		}
	
	}
}
