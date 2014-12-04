#!/bin/zsh

DEST_DIR="./temp/";
COMPILED_SUFFIX=".c";
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

  BINARY_FILE="${DEST_DIR}${FILE}${BINARY_SUFFIX}";

  echo "Compiling the file ${COMPILED_FILE}";

  gcc -m32 -w -g -O0 -o ${BINARY_FILE} ${COMPILED_FILE} ${RUNTIME};

  if [[ -f ${BINARY_FILE} ]];
  then
    BINARY_OUT="${BINARY_FILE}.out";
    JAVA_OUT="${DEST_DIR}${FILE}.java.out";
    javac "${1}" -d ${DEST_DIR};
    echo "Executing the binary...";
    "./${BINARY_FILE}" > ${BINARY_OUT};
    java -classpath ${DEST_DIR} ${FILE};

:<<'END'
    java -classpath ${DEST_DIR} ${FILE} > ${JAVA_OUT};

    diff ${BINARY_OUT} ${JAVA_OUT} > /dev/null 2>&1;
    if [[ ${?} -eq 0 ]];
    then
      echo "compiled and run file ${1}: SUCCESS";
      rm ${BINARY_OUT} ${JAVA_OUT};
    elif [[ ${?} -eq 1 ]];
    then
      echo "File ${BINARY_OUT} ${JAVA_OUT} differ from ${JAVA_OUT}" 1>&2;
    else
      echo "diff command failed" 1>&2;
    fi
END
  fi
else
  echo "first argument must be a minijava program (see the minijava_examples directory)!";
fi

