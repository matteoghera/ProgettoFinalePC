package com.unifi.pc.gc.bloom.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tools {

	public static List<String> readFile(String filepath) {
		List<String> rows=new ArrayList<String>();
		try {
			FileReader fr =new FileReader(filepath);
			BufferedReader in =new BufferedReader(fr);
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

}
