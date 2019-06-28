#!/bin/bash

#ANALISI DELLE PRESTAZIONI AL VARIARE DEL NUMERO DI THREAD

#Dimensione file 2M
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 100 data/sample10K.txt data/people10K.txt
done

#2 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 100 data/sample10K.txt data/people10K.txt
done

#3 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 3 100 data/sample10K.txt data/people10K.txt
done

#5 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 5 100 data/sample10K.txt data/people10K.txt
done

#10 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 10 100 data/sample10K.txt data/people10K.txt
done

#15 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 15 100 data/sample10K.txt data/people10K.txt
done
