module Tokens where

data Token a = 
   Quote a
 | LPar a
 | RPar a
 | LBrack a
 | RBrack a
 | LBrace a
 | RBrace a
 | Comma a 
 | Minus a 
 | KwMOVE a
 | KwNAME a
 | KwLABEL a
 | KwTEMP a
 | KwCONST a
 | KwEXP a
 | KwESEQ a
 | KwMem a
 | KwBinop a
 | KwSeq a
 | KwCall a
 | KwNop a
 | KwJump a
 | KwCJump a
 | KwMul a
 | KwPlus a
 | KwMinus a
 | KwDiv a
 | KwAnd a
 | KwOr a
 | KwXor a
 | KwLShift a
 | KwRShift a
 | KwARShift a
 | KwEQ a
 | KwNE a
 | KwLT a
 | KwGT a
 | KwLE a
 | KwGE a
 | KwULT a
 | KwUGT a
 | KwULE a
 | KwUGE a
 | KwInt a
 | KwReturn a
 | Const Integer a
 | Label String a
 | Register String a
 | Id String a
 deriving (Eq,Read,Show)

token_pos (Quote a) = a
token_pos (LPar a) = a
token_pos (RPar a) = a
token_pos (LBrack a) = a
token_pos (RBrack a) = a
token_pos (LBrace a) = a
token_pos (RBrace a) = a
token_pos (Comma a ) = a
token_pos (KwMOVE a) = a
token_pos (KwNAME a) = a
token_pos (KwLABEL a) = a
token_pos (KwTEMP a) = a
token_pos (KwCONST a) = a
token_pos (KwEXP a) = a
token_pos (KwMem a) = a
token_pos (KwBinop a) = a
token_pos (KwSeq a) = a
token_pos (KwCall a) = a
token_pos (KwNop a) = a
token_pos (KwJump a) = a
token_pos (KwCJump a) = a
token_pos (KwMul a) = a
token_pos (KwPlus a) = a
token_pos (KwMinus a) = a
token_pos (KwDiv a) = a
token_pos (KwAnd a) = a
token_pos (KwOr a) = a
token_pos (KwXor a) = a
token_pos (KwLShift a) = a
token_pos (KwRShift a) = a
token_pos (KwARShift a) = a
token_pos (KwEQ a) = a
token_pos (KwNE a) = a
token_pos (KwLT a) = a
token_pos (KwGT a) = a
token_pos (KwLE a) = a
token_pos (KwGE a) = a
token_pos (KwULT a) = a
token_pos (KwUGT a) = a
token_pos (KwULE a) = a
token_pos (KwUGE a) = a
token_pos (KwInt a) = a
token_pos (KwReturn a) = a
token_pos (Const _ a) = a
token_pos (Label _ a) = a
token_pos (Register _ a) = a
token_pos (Id _ a) = a
