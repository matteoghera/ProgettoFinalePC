#!/bin/bash

for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.SequentialBloomFilterApp 1 20560 data/sample2M.txt data/people2M.txt
done

for i in $(seq 1 9);
do
java -cp BloomFilter.jar com.unifi.pc.gc.bloom.app.ParallelBloomFilterApp ${numberOfCores} 20560 data/sample2M.txt data/people2M.txt
done


