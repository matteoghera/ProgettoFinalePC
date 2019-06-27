#!/bin/bash

#ANALISI DELLE PRESTAZIONI AL VARIARE DEL NUMERO DI FILE
#la dimensione del vettore di bit è 2.5641025641 volte quella del campione

#Dimensione file 10K
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 100 data/sample10K.txt data/people10K.txt
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 100 data/sample10K.txt data/people10K.txt
done

#Dimensione fdonbile 100K
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 1050 data/sample100K.txt data/people100K.txt
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 1050 data/sample100K.txt data/people100K.txt
done

#Dimensione file 500K
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 5160 data/sample500K.txt data/people500K.txt
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 5160 data/sample500K.txt data/people500K.txt
done

#Dimensione file 1M
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 10200 data/sample1M.txt data/people1M.txt
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 10200 data/sample1M.txt data/people1M.txt
done

#Dimensione file 2M
for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 20560 data/sample2M.txt data/people2M.txt
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp 2 20560 data/sample2M.txt data/people2M.txt
done
