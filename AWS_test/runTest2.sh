#!/bin/bash

#ANALISI DELLE PRESTAZIONI AL VARIARE DEL NUMERO DI THREAD

#Dimensione file 2M
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 1050 data/sample100K.txt data/people100K.txt
done

#2 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 1050 data/sample100K.txt data/people100K.txt
done

#3 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 3 1050 data/sample100K.txt data/people100K.txt
done

#5 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 5 1050 data/sample100K.txt data/people100K.txt
done

#10 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 10 1050 data/sample100K.txt data/people100K.txt
done

#15 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 15 1050 data/sample100K.txt data/people100K.txt
done
