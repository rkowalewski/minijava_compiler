JFLEX = /soft/IFI/lang/jflex-1.4.1/bin/jflex
JAVACUPJAR = /soft/IFI/lang/java-cup/java-cup-v11a.jar
JAVACUP = java -jar $(JAVACUPJAR)

all: straightline/Parser.java straightline/Lexer.java 
	javac  -cp .:$(JAVACUPJAR) straightline/Test.java

straightline/Parser.java: straightline/Parser.cup
	cd straightline; $(JAVACUP) -symbols Parser_sym -parser Parser Parser.cup

straightline/Lexer.java: straightline/Lexer.jflex
	cd straightline; $(JFLEX) Lexer.jflex

clean:
	rm -f straightline/Parser_sym.java straightline/Parser.java straightline/Lexer.java straightline/Lexer.java~ straightline/*.class
