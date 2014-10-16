#!/bin/zsh

if [ -z "$1" ]
  then
    echo "first argument must be a minijava program (see the minijava_examples directory)!";
  else
    java -cp .:/Users/kowa/Develop/projects/praktikum_compiler/lib/java-cup-11a.jar straightline.Test $1
fi
