JFLEX = $(CURDIR)/lib/jflex-1.4.1/bin/jflex
JAVACUPJAR = $(CURDIR)/lib/java-cup-11a.jar
JAVACUP = java -jar $(JAVACUPJAR)

all: Parser Lexer
	javac  -cp $(JAVACUPJAR) -sourcepath src -d ./target/ src/main/Compiler.java

Parser: src/parser/Parser.cup
	cd src/parser; $(JAVACUP) -symbols Parser_sym -parser Parser Parser.cup

Lexer: src/parser/Lexer.jflex
	cd src/parser; $(JFLEX) Lexer.jflex

clean:
	rm -rf src/parser/Parser_sym.java src/parser/Parser.java src/parser/Lexer.java src/parser/Lexer.java~ target/* temp/*
