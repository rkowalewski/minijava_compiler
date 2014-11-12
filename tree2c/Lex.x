{
module Lex where

import qualified Tokens
}

%wrapper "posn"

$digit = 0-9        -- digits
$alpha = [a-zA-Z]   -- alphabetic characters
$letter = $alpha 

tokens :-

  $white+;
  "//".* ;
  "{ \ p s -> Tokens.Quote p }
  "("{ \ p s -> Tokens.LPar p }
  ")"{ \ p s -> Tokens.RPar p }
  "{"{ \ p s -> Tokens.LBrace p }
  "}"{ \ p s -> Tokens.RBrace p }
  "["{ \ p s -> Tokens.LBrack p }
  "]"{ \ p s -> Tokens.RBrack p }
  ","{ \ p s -> Tokens.Comma p }
  "-"{ \ p s -> Tokens.Minus p }
  MOVE{\ p s -> Tokens.KwMOVE p }
  NAME{\ p s -> Tokens.KwNAME p }
  LABEL{\ p s -> Tokens.KwLABEL p }
  TEMP{\ p s -> Tokens.KwTEMP p }
  CONST{\ p s -> Tokens.KwCONST p }
  EXP{\ p s -> Tokens.KwEXP p }
  ESEQ{\ p s -> Tokens.KwESEQ p }
  MEM{\ p s -> Tokens.KwMem p }
  BINOP{\ p s -> Tokens.KwBinop p }
  SEQ{\ p s -> Tokens.KwSeq p }
  CALL{\ p s -> Tokens.KwCall p }
  NOP{\ p s -> Tokens.KwNop p }
  JUMP{\ p s -> Tokens.KwJump p }
  CJUMP{\ p s -> Tokens.KwCJump p }
  MUL{\ p s -> Tokens.KwMul p }
  PLUS{\ p s -> Tokens.KwPlus p }
  MINUS{\ p s -> Tokens.KwMinus p }
  DIV{\ p s -> Tokens.KwDiv p }
  AND{\ p s -> Tokens.KwAnd p }
  OR{\ p s -> Tokens.KwOr p }
  XOR{\ p s -> Tokens.KwXor p }
  LSHIFT{\ p s -> Tokens.KwLShift p }
  RSHIFT{\ p s -> Tokens.KwRShift p }
  ARSHIFT{\ p s -> Tokens.KwARShift p }
  EQ{\ p s -> Tokens.KwEQ p }
  NE{\ p s -> Tokens.KwNE p }
  LT{\ p s -> Tokens.KwLT p }
  GT{\ p s -> Tokens.KwGT p }
  LE{\ p s -> Tokens.KwLE p }
  GE{\ p s -> Tokens.KwGE p }
  ULT{\ p s -> Tokens.KwULT p }
  UGT{\ p s -> Tokens.KwUGT p }
  ULE{\ p s -> Tokens.KwULE p }
  UGE{\ p s -> Tokens.KwUGE p }
  int{\ p s -> Tokens.KwInt p }
  return{\ p s -> Tokens.KwReturn p }
  $digit+{ \ p s -> Tokens.Const (read s) p }
  $letter [$letter $digit \_ \' \$]*{ \ p s -> Tokens.Id s p }
{
-- Each action has type :: String -> Token

-- The token type is in Tokens.hs
{-
main = do
  s <- getContents
  print (alexScanTokens s)
-}
}
