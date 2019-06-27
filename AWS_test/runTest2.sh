#!/bin/bash

#ANALISI DELLE PRESTAZIONI AL VARIARE DEL NUMERO DI THREAD

#Dimensione file 2M
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 20560 data/sample2M.txt data/people2M.txt
done

#2 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 20560 data/sample2M.txt data/people2M.txt
done

#3 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 3 20560 data/sample2M.txt data/people2M.txt
done

#5 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 5 20560 data/sample2M.txt data/people2M.txt
done

#10 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 10 20560 data/sample2M.txt data/people2M.txt
done

#15 Threads
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 15 20560 data/sample2M.txt data/people2M.txt
done
