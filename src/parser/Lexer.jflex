/* Copyright (c) 1997 Andrew W. Appel.  Licensed software: see LICENSE file */
package parser;

import java_cup.runtime.Symbol;

%% 

%public
%class Lexer
%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol
%yylexthrow ParseException

%char
%line
%column

%{

private void error(String s) throws ParseException {
  throw new ParseException("At line " + yyline + ", column " + yycolumn + ": " + s);
}

private Symbol symbol(int kind) {
    return new Symbol(kind, yyline, yycolumn);
}

private Symbol symbol(int kind, Object value) {
    return new Symbol(kind, yyline, yycolumn, value);
}

private int commentDepth = 0;

%}

%eofval{
  {
    if (commentDepth > 0) {
       error("runaway comment");
    }
    return symbol(Parser_sym.EOF);
  }
%eofval}       

%state SingleLineComment MultiLineComment
idchars=[A-Za-z_0-9]
id=[A-Za-z]{idchars}*
ws=[\r\n\t\ ]*
%%
<YYINITIAL> {
   {ws}     { }
   ","      { return symbol(Parser_sym.COMMA); }
   "."      { return symbol(Parser_sym.DOT); }
   ";"      { return symbol(Parser_sym.SEMICOLON); }
   "!"      { return symbol(Parser_sym.NOT); }
   "&&"     { return symbol(Parser_sym.AND); }
   "("      { return symbol(Parser_sym.LPAREN); }
   ")"      { return symbol(Parser_sym.RPAREN); }
   "["      { return symbol(Parser_sym.LSQUARE); }
   "]"      { return symbol(Parser_sym.RSQUARE); }
   "{"      { return symbol(Parser_sym.LCURLY); }
   "}"      { return symbol(Parser_sym.RCURLY); }
   "+"      { return symbol(Parser_sym.PLUS); }
   "-"      { return symbol(Parser_sym.MINUS); }
   "*"      { return symbol(Parser_sym.TIMES); }
   "/"      { return symbol(Parser_sym.DIVIDE); }
   "="      { return symbol(Parser_sym.BECOMES); }
   "<"      { return symbol(Parser_sym.LT); }
   "class"  { return symbol(Parser_sym.CLASS); }
   "public" { return symbol(Parser_sym.PUBLIC); }
   "static" { return symbol(Parser_sym.STATIC); }
   "void"   { return symbol(Parser_sym.VOID); }
   "main"   { return symbol(Parser_sym.MAIN); }
   "extends" { return symbol(Parser_sym.EXTENDS); }
   "return" { return symbol(Parser_sym.RETURN); }
   "if"     { return symbol(Parser_sym.IF); }
   "else"   { return symbol(Parser_sym.ELSE); }
   "while"  { return symbol(Parser_sym.WHILE); }
   "this"   { return symbol(Parser_sym.THIS); }
   "new"    { return symbol(Parser_sym.NEW); }
   "String" { return symbol(Parser_sym.TYPE_STRING); }
   "int"    { return symbol(Parser_sym.TYPE_INT); }
   "boolean" { return symbol(Parser_sym.TYPE_BOOLEAN); }
   "char"   { return symbol(Parser_sym.TYPE_CHAR); }
   "System.out.print"   { return symbol(Parser_sym.SYSTEM_OUT_PRINT); }
   "System.out.println" { return symbol(Parser_sym.SYSTEM_OUT_PRINTLN); }
   "length" { return symbol(Parser_sym.ARRLEN); }
   "true"   { return symbol(Parser_sym.TRUE); }
   "false"  { return symbol(Parser_sym.FALSE); }
   {id}     { return symbol(Parser_sym.IDENTIFIER, yytext()); }
   [0-9]+   { return symbol(Parser_sym.INTEGER_LITERAL, Integer.parseInt(yytext())); }
   "/*"     { commentDepth = 1; yybegin(MultiLineComment); }
   "*/"     { error("unexpected */"); }
   "//"     { yybegin(SingleLineComment); }
   .        { error("illegal token"); }
}
<SingleLineComment> {
   [^\n]+   { /* ignore */ }
   \n       { yybegin(YYINITIAL); }
}
<MultiLineComment> {
    "/*"    { commentDepth++; }
    "*/"    { commentDepth--; if (commentDepth==0) yybegin(YYINITIAL); }
    .|\n    { }
}


