#!/bin/bash

DIR="./compilerbau/out";

if [[ -f $1 ]];
then
  NAME="$(basename "$1" ".s")";
  OUT="./${DIR}/${NAME}.txt";
  ./.cabal/bin/risc386 $1 1>${OUT} 2>/dev/null;
  STATUS="${?}";

  if [[ ${STATUS} -gt 0 ]];
  then
    echo "Error with File $1";
  else
    echo "${NAME}:" | cat - ${OUT} > temp/out && mv temp/out ${OUT}
  fi
else
  echo "first argument must be a minijava program (see the minijava_examples directory)!";
fi

