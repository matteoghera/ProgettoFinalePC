#!/bin/bash

gpw 1 30 > people.txt
for i in $(seq 1 9999);
do
gpw 1 30 >> people.txt
done


