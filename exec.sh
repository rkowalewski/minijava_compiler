#!/bin/zsh

if [[ -f $1 ]];
  then
    FILE="$(basename "$1")";
    java -cp target:/Users/kowa/Develop/projects/praktikum_compiler/lib/java-cup-11a.jar main.Test $1 >! ./temp/$FILE.c
    OUT="./temp/$FILE.out"
    gcc -m32 -w -g -O0 -o $OUT "./temp/$FILE.c" ./tree2c/runtime.c

    if [[ -f $OUT ]];
      then
        echo "Executing the binary of $1..."
        "./$OUT"
    fi
  else
    echo "first argument must be a minijava program (see the minijava_examples directory)!";
fi
