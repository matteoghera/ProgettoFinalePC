package com.unifi.pc.gc.bloom.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tools {

private static BufferedReader in;

	// read the file
	public static List<String> readFile(String filepath) {
		List<String> rows=new ArrayList<String>();
		try {
			FileReader fr =new FileReader(filepath);
			in = new BufferedReader(fr);
			String row =in .readLine();
			while(row!=null) {
				rows.add(row);
				row=in.readLine();
			}
		} catch(FileNotFoundException e) {
			System.out.println("File reading error");
			System.exit(-1);
		} catch(IOException e) {
			System.out.println("File reading error");
			System.exit(-1);
		}
		return rows;
	}

// create the sample
	public static List<String> generatorSamples(List<String> data, int limit) {
		List<String> samples=new ArrayList<String>();
		
		Iterator<String> dataIterator=data.iterator();
		int chosenIndex=(int) (Math.random()*limit);
		int i=0;
		while(dataIterator.hasNext()) {
			String currentData=dataIterator.next();
			if(i==chosenIndex) {
				samples.add(currentData);
				chosenIndex+=(int) (Math.random()*limit)+1;
			}
			i++;
		}
		return samples;
	}
	
	public static int writeFile(String filepath, List<String> data) {
		int rows=0;
		try {
			FileWriter fw=new FileWriter(filepath);
			PrintWriter out=new PrintWriter(fw);
			Iterator<String> iteratorData=data.iterator();
			while(iteratorData.hasNext()) {
				String row=iteratorData.next();
				out.println(row);
				rows++;
			}
			out.close();
		} catch (IOException e) {
			System.out.println("File writing error");
			System.exit(-1);
		}
		return rows;
		
	}

}
