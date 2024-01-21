#!/bin/bash

echo -n "" > output

for i in *.txt; do
  cat $i >> output
  echo "" >> output
  echo $i >> output
  echo "" >> output
  echo "-------------" >> output
  echo "" >> output
done

