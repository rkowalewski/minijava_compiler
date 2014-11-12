{
module Parse  where

import Lex
import qualified Tokens
import Names
import Tree
}

%tokentype { (Tokens.Token AlexPosn) }

%name parse

%token
  	'"'	     	 { Tokens.Quote _ }
  	'['	     	 { Tokens.LBrack _ }
  	']'	       { Tokens.RBrack _ }
  	'('	       { Tokens.LPar _ }
  	')'	       { Tokens.RPar _ }
  	'{'	       { Tokens.LBrace _ }
  	'}'	       { Tokens.RBrace _ }
  	',' 	       { Tokens.Comma _ }
  	'-' 	       { Tokens.Minus _ }
  	MOVE         { Tokens.KwMOVE _ }
  	NAME         { Tokens.KwNAME _ }
  	LABEL        { Tokens.KwLABEL _ }
  	CONST        { Tokens.KwCONST _ }
  	EXP          { Tokens.KwEXP _ }
  	ESEQ         { Tokens.KwESEQ _ }
  	TEMP         { Tokens.KwTEMP _ }
  	MEM          { Tokens.KwMem _ }
  	BINOP        { Tokens.KwBinop _ }
  	SEQ          { Tokens.KwSeq _ }
  	CALL         { Tokens.KwCall _ }
  	NOP          { Tokens.KwNop _ }
  	JUMP         { Tokens.KwJump _ }
  	CJUMP        { Tokens.KwCJump _ }
   MUL          { Tokens.KwMul _ }
   PLUS         { Tokens.KwPlus _ }
   MINUS        { Tokens.KwMinus _ }
   DIV          { Tokens.KwDiv _ }
   AND          { Tokens.KwAnd _ }
   OR           { Tokens.KwOr _ }
   XOR          { Tokens.KwXor _ }
   LSHIFT       { Tokens.KwLShift _ }
   RSHIFT       { Tokens.KwRShift _ }
   ARSHIFT      { Tokens.KwARShift _ }
   EQ           { Tokens.KwEQ _ }
   NE           { Tokens.KwNE _ }
   LT           { Tokens.KwLT _ }
   GT           { Tokens.KwGT _ }
   LE           { Tokens.KwLE _ }
   GE           { Tokens.KwGE _ }
   ULT          { Tokens.KwULT _ }
   UGT          { Tokens.KwUGT _ }
   ULE          { Tokens.KwULE _ }
   UGE          { Tokens.KwUGE _ }
  	int          { Tokens.KwInt _ }
  	return       { Tokens.KwReturn _ }
   integer      { Tokens.Const $$ _ }
   identifier   { Tokens.Id $$ _ }

%%

Prg :: { ([Proc]) }
Prg:  FctList           { $1 }

FctList :: { [ Proc ] }
FctList: {- empty -} {[]}
  | Fct FctList { $1:$2 }

Fct :: { Proc }
Fct: int identifier '(' ParamList ')' '{' Stms return identifier '}' 
      { Proc $2 $4 $7 (mkNamedTemp $9) }

Stms :: { [Tree.Stm] }
Stms:     { [] }
  | Stm Stms       { $1:$2 }

Stm :: { Tree.Stm }
Stm:  
   MOVE '(' Exp ',' Exp ')'       { Tree.MOVE $3 $5 }
|  EXP '(' Exp ')'                { Tree.EXP $3 }
|  JUMP '(' Exp ',' LabelList ')' { Tree.JUMP $3 $5 }
|  CJUMP '(' Rel ',' Exp ',' Exp ',' identifier ',' identifier ')' 
                                  { Tree.CJUMP $3 $5 $7 $9 $11 }
|  SEQ '(' Stm ',' Stm ')'        { Tree.SEQ $3 $5 }
|  LABEL '(' identifier ')'       { Tree.LABEL $3 }
|  NOP                            { Tree.NOP }
| '(' Stm ')'                     { $2 }

Exp :: { Tree.Exp }
Exp: 
  NAME '(' Label ')'     { Tree.NAME $3 }
| TEMP '(' Temp ')'      { Tree.TEMP (mkNamedTemp $3) }
| CONST '(' Integer ')'  { Tree.CONST (fromInteger $3) }
| BINOP '(' Op ',' Exp ',' Exp ')'  { Tree.BINOP $3 $5 $7 }
| MEM  '(' Exp ')'       { Tree.MEM $3 }
| CALL '(' Exp ')'       { Tree.CALL $3 [] }
| CALL '(' Exp ',' Exps ')'       { Tree.CALL $3 $5 }
| ESEQ '(' StmList ',' Exp ')' { Tree.ESEQ (mkSeq $3) $5 }
| '(' Exp ')'            { $2 }

Integer:
     integer { $1 }
  | '-' integer { 0-$2 }
  | '(' Integer ')' { $2 } 

Temp:
     identifier { $1 }
  | '(' Temp ')' { $2 } 

Label:
     identifier { $1 }
  | '"' identifier '"' { $2 }
  | '(' Label ')' { $2 } 

StmList :: { [Tree.Stm] }
StmList: '[' ']'    { [] }
  | '[' Stm StmList0       { $2:$3 }

StmList0 :: { [Tree.Stm] }
StmList0: ']'   { [] }
  | ',' Stm StmList0   { $2:$3 }

Exps :: { [Tree.Exp] }
Exps: Exp        { [$1] }
  |  Exp ',' Exps     { $1:$3 }

LabelList :: { [Label] }
LabelList:  '[' ']'    { [] }
  | '[' identifier LabelList0       { $2:$3 }
  | '(' LabelList ')' { $2 }

LabelList0 :: { [Label] }
LabelList0:  ']'   { [] }
  | ',' identifier LabelList0   { $2:$3 }

Param: 
   int identifier         { mkNamedTemp $2 }

ParamList :: { [Temp] }
ParamList: {- empty -}    { [] }
  | Param ParamList0       { $1:$2 }

ParamList0 :: { [Temp] }
ParamList0: {- empty -}   { [] }
  | ',' Param ParamList0   { $2:$3 }

Op :: { Tree.BinOp }
Op: 
   MUL     { Tree.MUL }
 | PLUS    { Tree.PLUS }  
 | MINUS   { Tree.MINUS } 
 | DIV     { Tree.DIV }
 | AND     { Tree.AND }  
 | OR      { Tree.OR }  
 | XOR     { Tree.XOR }   
 | LSHIFT  { Tree.LSHIFT }
 | RSHIFT  { Tree.RSHIFT }
 | ARSHIFT { Tree.ARSHIFT }
 | '(' Op ')' { $2 }

Rel :: { Tree.RelOp }
Rel:
        EQ  { Tree.EQ }
     |  NE  { Tree.NE }
     |  LT  { Tree.LT }
     |  GT  { Tree.GT }
     |  LE  { Tree.LE }
     |  GE  { Tree.GE }
     |  ULT { Tree.ULT }
     |  UGT { Tree.UGT }
     |  ULE { Tree.ULE }
     |  UGE { Tree.UGE }
     | '(' Rel ')' { $2 }


{

mkSeq :: [Stm] -> Stm
mkSeq [] = error "mkSeq: empty list"
mkSeq [x] = x
mkSeq (x:xs) = Tree.SEQ x (mkSeq xs)

happyError :: [Tokens.Token AlexPosn] -> a
happyError tks = error ("Parse error at " ++ lcn ++ "\n")
	where
	lcn = 	case tks of
		  [] -> "end of file"
		  (tk:_) -> "line " ++ show l ++ ", column " ++ show c 
			where AlexPn _ l c = Tokens.token_pos tk
}
