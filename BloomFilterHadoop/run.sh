#!/bin/bash

# SPOSTARE NELLA CARTELLA DI hadoop-2.9.0
# Impostare la variabile d'ambiente secondProjectPCPath con il path del progetto. Ad esempio:
export secondProjectPCPath=/home/matteoinformatica/Documenti/Github/secondoProgettoPC/ProgettoFinalePC

#avvio sessione
sbin/start-dfs.sh

#visualizzo componenti in esecuzione: l'output del comando deve essere composto da quattro righe
#altrimenti esegui il comando: /home/matteoinformatica/Scrivania/hadoop-2.9.0/bin/hdfs namenode -format e riesegui il seguente comando
jps

#copio i file nel hdfs
bin/hadoop dfsadmin -safemode leave #eventualmente lascia la safemode
bin/hdfs dfs -copyFromLocal ${secondProjectPCPath}/BloomFilterHadoop/input #cambiare path
bin/hadoop dfsadmin -safemode leave
bin/hdfs dfs -copyFromLocal ${secondProjectPCPath}/BloomFilterHadoop/output #cambiare path

#eseguo file .jar
bin/hadoop jar ${secondProjectPCPath}/BloomFilterHadoop/target/BloomFilterHadoop-0.0.1-SNAPSHOT.jar com.unifi.pc.gc.bloom.app.HadoopBloomFilterApp 100 input/sample.txt input/people.txt ${secondProjectPCPath}/BloomFilterHadoop/output

#visualizzo risultati
bin/hdfs dfs -cat output/bloomfilter/*


#ripulisco hdfs
bin/hadoop fs -rm -R input
bin/hadoop fs -rm -R output
bin/hdfs dfs -ls

#chiudo sessione
sbin/stop-dfs.sh

