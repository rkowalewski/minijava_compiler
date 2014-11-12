-- Time-stamp: <Mon Dec 17 2007 14:21:34 Stardate: [-29]8672.78 hwloidl>
-- Interpreter for the intermediate language used in
-- Appel's "Modern Compiler Implementation in Java"
---------------------------------------------------------------------------

module Main where

import System.Environment
import System.Console.GetOpt
import Text.PrettyPrint

import Lex
import Parse
import Names
import Tree
import Cmm

usage :: [String] -> IO a
usage errs = ioError (userError header)
    where header = "Usage: tree2c <file>"

parseCmdLine :: [String] -> IO String
parseCmdLine argv = do
  let (os, ns, errs) = getOpt Permute [] argv
  case ns of
    prgFile : _ -> return prgFile
    _ -> usage [""]

main =	do
  cmdLine  <- getArgs
  prgFile <- parseCmdLine cmdLine
  input <- readFile prgFile
  let defs = parse (alexScanTokens input)
  let cmm = cmmDoc defs
  putStrLn (render cmm)
