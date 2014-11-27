
module Main where

import Control.Monad

import Data.List -- find
import qualified Data.Map as Map

import System.Environment
import System.Exit
import System.IO
import System.Console.GetOpt

import Text.PrettyPrint

import Frame
import FrameIntel
import LexIntel
import ParseIntel
import StateIntel
import Util -- Pretty

data Flag
  = Verbose
  | Help
    deriving (Eq, Show)

optDescrs :: [OptDescr Flag]
optDescrs =
  [ Option ['?'] ["help"]      (NoArg Help)    "show usage information"
  , Option ['v'] ["verbose"]   (NoArg Verbose) "be verbose"
  ]

usage :: IO a
usage = do
  putStr $ usageInfo "Usage: risc386 [options] <file>" optDescrs
  exitFailure

-- | Extract the input file name from command line.
--   Display usage info for malformed command lines.
parseCmdLine :: [String] -> IO String
parseCmdLine argv = do
  let (os, ns, _) = getOpt Permute optDescrs argv
  unless (length ns == 1)
    usage
  let prgFile = head ns
  when (Verbose `elem` os) $
    hPutStrLn stderr $ "Reading program from file: " ++ prgFile
  when (Help `elem` os) $
    ioError (userError "No execution with --help")
  return prgFile

{- main:
  * parse IntelFrame list
  * search "*main" function
  * split frames into blocks IBlockFrame
  * convert frame list into a map
  * execute main function
 -}
main :: IO ()
main =	do
  cmdLine <- getArgs
  prgFile <- parseCmdLine cmdLine
  input   <- readFile prgFile
  -- hPutStrLn stderr $ "Passing these args to main fct: " ++ (show args)
  -- input <- getContents
  -- print (alexScanTokens input)
  let frames = parse (alexScanTokens input)
  -- anyting that ends in "main" is considered the main function
  lmain <- maybeM "no main function found" $
             find (\ l -> drop (length l - 4) l == "main")
                  (map frameName frames)
  hPutStrLn stderr ("## Input:\n" ++ (render $ ppr frames))

  let bfs = map iBlocksFrame frames
  hPutStrLn stderr ("## Blocks:\n" ++ (render $ ppr bfs))

  let framemap =  Map.fromList $ map (\ f@(IBlockFrame l _ _) -> (l,f)) $ bfs
  output <- run framemap lmain
  mapM_ putStr output
  exitSuccess
