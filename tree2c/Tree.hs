module Tree where

import Prelude hiding (EQ,GT,LT)
import Data.List
import Names

data Exp
  = CONST Int 
  | NAME Label 
  | TEMP Temp 
  | BINOP BinOp Exp Exp 
  | MEM  Exp
  | CALL Exp [Exp]
  | ESEQ Stm Exp
 deriving Eq

data Stm
  = MOVE Exp Exp
  | EXP Exp
  | JUMP Exp [Label]
  | CJUMP RelOp Exp Exp Label Label
  | SEQ Stm Stm
  | LABEL Label 
  | NOP
 deriving Eq

data BinOp
  = PLUS | MINUS | MUL | DIV | AND | OR | LSHIFT | RSHIFT | ARSHIFT | XOR
 deriving (Eq,Show)

data RelOp
  = EQ | NE | LT | GT | LE | GE | ULT | ULE | UGT | UGE
 deriving (Eq,Show)

data Proc = 
   Proc { procname :: String
        , params :: [Temp]
        , body :: [Stm]
        , returnTemp :: Temp
        }
    deriving Show

instance Show Exp where
   show (CONST i) = "CONST(" ++ show i ++ ")"
   show (NAME l) = "NAME(" ++ show l ++ ")"
   show (TEMP t) = "TEMP(" ++ show t ++ ")"
   show (BINOP o e1 e2) = "BINOP(" ++ show o ++ ", " 
                                   ++ show e1 ++ ", " ++ show e2 ++ ")"
   show (MEM  e) = "MEM(" ++ show e ++ ")"
   show (CALL e es) = "CALL(" ++ (intercalate ", " (map show (e:es))) ++ ")"
   show (ESEQ s e) = "ESEQ(" ++ show s ++ ", " ++ show e ++ ")"

instance Show Stm where
   show (MOVE e1 e2) = "MOVE(" ++ show e1 ++ ", " ++ show e2 ++ ")"
   show (EXP e) = "EXP(" ++ show e ++ ")"
   show (JUMP e ls) = "JUMP(" ++ show e ++ ", " ++ show ls ++ ")"
   show (CJUMP r e1 e2 l1 l2) = "CJUMP(" ++ show r ++ ", " 
                                         ++ show e1 ++ ", " ++ show e2 ++ ", "
                                         ++ show l1 ++ ", " ++ show l2 ++ ")"
   show (SEQ s1 s2) = "SEQ(" ++ show s1 ++ ", " ++ show s2 ++ ")"
   show (LABEL l1) = "LABEL(" ++ show l1 ++ ")"
   show NOP = "NOP"
