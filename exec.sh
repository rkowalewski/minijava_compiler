#!/bin/zsh

if [ -z "$1" ]
  then
    echo "first argument must be a minijava program (see the minijava_examples directory)!";
  else
    FILE="$(basename "$1")";
    java -cp target:/Users/kowa/Develop/projects/praktikum_compiler/lib/java-cup-11a.jar main.Test $1 >! ./temp/$FILE.c
fi
