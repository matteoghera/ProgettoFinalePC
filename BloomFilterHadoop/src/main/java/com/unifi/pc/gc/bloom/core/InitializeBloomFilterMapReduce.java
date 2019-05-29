package com.unifi.pc.gc.bloom.core;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.lib.NullOutputFormat;
import org.apache.hadoop.util.Tool;

import com.unifi.pc.gc.bloom.utility.HashFunction;

public class InitializeBloomFilterMapReduce extends Configured implements Tool {

	private static int n;
	private static List<HashFunction<Object>> myHashFunctionList;
	private static BloomFilter myBloomFilter;

	public static void setN(int n) {
		InitializeBloomFilterMapReduce.n = n;
	}

	public static void setMyHashFunctionList(List<HashFunction<Object>> myHashFunctionList) {
		InitializeBloomFilterMapReduce.myHashFunctionList = myHashFunctionList;
	}
	
	public static void createBloomFilter() {
		InitializeBloomFilterMapReduce.myBloomFilter=new BloomFilter(n, myHashFunctionList);
	}

	public static BloomFilter getMyBloomFilter() {
		return myBloomFilter;
	}

	public static class MapClass extends MapReduceBase implements Mapper<Text, Text, Text, BloomFilter> {
		BloomFilter<String> bf = new BloomFilter(n, myHashFunctionList);
		OutputCollector<Text, BloomFilter> oc = null;

		@Override
		public void map(Text key, Text value, OutputCollector<Text, BloomFilter> output, Reporter reporter)
				throws IOException {
			if (oc == null)
				oc = output;
			bf.initializeMap(key.toString());

		}

		public void close() throws IOException {
			oc.collect(new Text("testkey"), bf);
		}

	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, BloomFilter, Text, Text> {
		JobConf job = null;

		public void configure(JobConf job) {
			this.job = job;
		}

		@Override
		public void reduce(Text key, Iterator<BloomFilter> values, OutputCollector<Text, Text> output,
				Reporter reporter) throws IOException {
			while (values.hasNext()) {
				myBloomFilter.union((BloomFilter)values.next());
			}
		}

		public void close() throws IOException {
			Path file = new Path(job.get("mapred.output.dir") + "/bloomfilter_log");
			FSDataOutputStream out = file.getFileSystem(job).create(file);
			myBloomFilter.write(out);
			out.close();
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		JobConf job = new JobConf(conf, InitializeBloomFilterMapReduce.class);

		Path in = new Path(args[0]); // path of sample file
		Path out = new Path(args[1]); // log file
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);

		job.setJobName("Initialize Bloom Filter");
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(1);
		
		job.setInputFormat(KeyValueTextInputFormat.class);
		job.setOutputFormat(NullOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BloomFilter.class);
		job.set("key.value.separator.in.input.line", ",");

		JobClient.runJob(job).waitForCompletion();

		return 1;
	}

}
