#!/bin/zsh

DEST_DIR="./temp/";
COMPILED_SUFFIX=".s";
BINARY_SUFFIX=".out";
RUNTIME="./tree2c/runtime.c";

if [[ -f $1 ]];
then
  FILE="$(basename "$1" ".java")";
  COMPILED_FILE="${DEST_DIR}${FILE}${COMPILED_SUFFIX}";
  java -cp target:./lib/java-cup-11a.jar main.Test $1 >! ${COMPILED_FILE};
  STATUS="${?}";

  if [[ ${STATUS} -gt 0 ]];
  then
    echo "Could not compile file ${1}:" 1>&2;
    cat ${COMPILED_FILE};
    rm ${COMPILED_FILE}
    exit 1;
  fi

  echo "Compilation successful --> ${COMPILED_FILE}";
else
  echo "first argument must be a minijava program (see the minijava_examples directory)!";
fi

