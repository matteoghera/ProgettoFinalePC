package com.unifi.pc.gc.bloom.utility;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.unifi.pc.gc.bloom.utility.Tools;

public class ToolsTest {
	String filepath;
	String filename="/people.txt";

	@Before
	public void setUp() throws Exception {
		File f = new File (System.getProperty ("user.dir"));
		File dir = f.getAbsoluteFile ();
		
		filepath=dir.toString();		
	}

	@Test
	public void testReadFile() {
		List<String> rows=Tools.readFile(filepath+filename);
		assertNotEquals(0, rows.size());
		assertEquals(10000, rows.size());
	}
	
	@Test
	public void testGeneratorSamples(){
		List<String> rows=Tools.readFile(filepath+filename);
		List<String> samples=Tools.generatorSamples(rows, 500);
		assertNotEquals(0, samples.size());
		assertTrue(samples.size()-20>=0);
	}
	
	@Test
	public void testWriteSampleFile() {
		List<String> rows=Tools.readFile(filepath+filename);
		List<String> samples=Tools.generatorSamples(rows, 500);
		assertEquals(samples.size(), Tools.writeFile(filepath+"/sample.txt", samples));
	}

}
