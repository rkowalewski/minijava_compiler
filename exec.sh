#!/bin/zsh

DEST_DIR="./temp/";
COMPILED_SUFFIX=".c";
BINARY_SUFFIX=".out";
RUNTIME="./tree2c/runtime.c";

if [[ -f $1 ]];
  then
    FILE="$(basename "$1")";
    COMPILED_FILE="${DEST_DIR}${FILE}${COMPILED_SUFFIX}";
    java -cp target:./lib/java-cup-11a.jar main.Test $1 >! ${COMPILED_FILE};
    STATUS="${?}";

    if [[ ${STATUS} > 0 ]];
    then
      cat ${COMPILED_FILE};
      rm ${COMPILED_FILE}
      exit 1;
    fi

    BINARY_FILE="${DEST_DIR}${FILE}${BINARY_SUFFIX}";

    echo "Compiling the file ${COMPILED_FILE}";

    gcc -m32 -w -g -O0 -o ${BINARY_FILE} ${COMPILED_FILE} ${RUNTIME};

    if [[ -f ${BINARY_FILE} ]];
      then
        echo "Executing the binary...";
        "./${BINARY_FILE}";
    fi
  else
    echo "first argument must be a minijava program (see the minijava_examples directory)!";
fi
