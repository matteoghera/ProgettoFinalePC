# Implementation of *Bloom Filter* using Apache Hadoop


## Prerequisites

To run this project using Ubuntu, it is necessary to have previously installed this software:

 *  Apache Maven version 3.6.0
 *  Apache Hadoop version 2.9


## Compiling

First of all it is necessary compile the Java files. You can compile the Java files using the following Maven command:

  $ mvn clean install


## Set Hadoop environment

Now you can start Hadoop hdfs using the following command:

  $ sbin/start-dfs.sh
  

After create a new directory with name *output* in the project *BloomFilterHadoop* folder and copy the file in the *output* and *input* directories using the following command (insert the complete paths):

  $ bin/hdfs dfs -copyFromLocal /BloomFilterHadoop/input
  $ bin/hdfs dfs -copyFromLocal /BloomFilterHadoop/output


Run the following command to start the Hadoop execution:


  $ bin/hadoop jar target/BloomFilterHadoop-0.0.1-SNAPSHOT.jar com.unifi.pc.gc.bloom.app.HadoopBloomFilterApp 100 input/sample.txt input/people.txt output


In the previus command, the arguments `com.unifi.pc.gc.bloom.app.HadoopBloomFilterApp 100 input/sample.txt input/people.txt output` are respectively the main class, the dimension of filter map, the path of sample file and the path of data-flow file.


## Using

At the end of the execution, it will be possible to see the result of the execution of Bloom Filter on the files in input. The file with the execution time is in the *output* folder.


You can see the result using the command:

  $ bin/hdfs dfs -cat output/bloomfilter/*


Don't forget to stop the hdfs using the command:

  $ sbin/stop-dfs.sh


If you prefer, you can run the file bash `run.sh` executing the command `./run.sh` (change paths!).
