package com.unifi.pc.gc.bloom.core;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

public class AnalyzeBloomFilterMapReduce extends Configured implements Tool {
	private static BloomFilter myBloomFilter;
	

	public static void setMyBloomFilter(BloomFilter myBloomFilter) {
		AnalyzeBloomFilterMapReduce.myBloomFilter = myBloomFilter;
	}

	
	public static class BloomFilterCounterMapper extends Mapper<Object, Text, Text, IntWritable>{
		private Text currentText=new Text();
		private final static IntWritable one=new IntWritable(1);
		
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String word=value.toString();			
			
			if(myBloomFilter.check(word)) {
				context.write(new Text("Number of elements admitted: "), one); //add one if element is admitted
			} else {
				context.write(new Text("Number of elements rejected: "), one); //add zero if element is rejected
			}
		}
	}
	
	public static class BloomFilterCounterReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int total=0;
			for(IntWritable val: values) {
				total+=val.get();
			}
			context.write(key, new IntWritable(total));
		}
		
		public void setup(Context context) throws IOException, InterruptedException {
			context.write(new Text(myBloomFilter.toString()), new IntWritable(0));
		}
	}



	@Override
	public int run(String[] arg0) throws Exception {
		//creo un oggetto Configuration che viene utilizzato per impostare le opzioni
		Configuration conf=getConf();
		
		//ottiene argomenti da riga di comando
		String[] args = new GenericOptionsParser(conf, arg0).getRemainingArgs();
		
		//creo l'oggetto che rappresenta il job
		Job job=Job.getInstance(conf);
		
		job.setJobName("Analyze Bloom Filter");
		//imposto il nome della classe principale nel file JAR del Job
		job.setJarByClass(AnalyzeBloomFilterMapReduce.class);
		//imposto la classe Mapper
		job.setMapperClass(BloomFilterCounterMapper.class);
		//imposto la classe Reducer
		job.setReducerClass(BloomFilterCounterReducer.class);
		
		//imposto i tipi per l'output finale chiave/valore
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		//imposto i percorsi dei file di input e di output
		FileInputFormat.addInputPath(job, new Path(args[0])); //path of data-flow file
		FileOutputFormat.setOutputPath(job, new Path(args[1])); //path of result file
		
		//Esegue il job e aspetta che sia completato
		job.waitForCompletion(true);
		return 1;
	}

}
