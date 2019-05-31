#!/bin/bash

#cambiare i paths della cartella hadoop-2.9.0

#avvio sessione
/home/matteoinformatica/Scrivania/hadoop-2.9.0/sbin/start-dfs.sh

#visualizzo componenti in esecuzione: l'output del comando deve essere composto da quattro righe
#altrimenti esegui il comando: /home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hdfs namenode -format e riesegui il seguente comando
jps

#copio i file nel hdfs
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hadoop dfsadmin -safemode leave #eventualmente lascia la safemode
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hdfs dfs -copyFromLocal /home/matteoinformatica/Documenti/secondoProgettoPC/progetto/BloomFilterHadoop/input #cambiare path
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hadoop dfsadmin -safemode leave
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hdfs dfs -copyFromLocal /home/matteoinformatica/Documenti/secondoProgettoPC/progetto/BloomFilterHadoop/output #cambiare path

#eseguo file .jar
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hadoop jar target/BloomFilterHadoop-0.0.1-SNAPSHOT.jar com.unifi.pc.gc.bloom.app.HadoopBloomFilterApp 100 input/sample.txt input/people.txt output output

#visualizzo risultati
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hdfs dfs -cat output/bloomfilter/*


#ripulisco hdfs
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hadoop fs -rm -R input
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hadoop fs -rm -R output
/home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hdfs dfs -ls

#chiudo sessione
/home/matteoinformatica/Scrivania/hadoop-2.9.0/sbin/stop-dfs.sh

