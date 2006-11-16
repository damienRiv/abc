package parser;

import beaver.Symbol;
import beaver.Scanner;
import parser.JavaParser.Terminals;
import AST.LexicalError;
import java.io.*;

%%

%public 
%final 
%class JavaScanner
%extends Scanner
%implements AST.PackageExtractor

%type Symbol 
%function nextToken 
%yylexthrow LexicalError

%unicode
%line %column

%{
  StringBuffer strbuf = new StringBuffer(128);
  int sub_line;
  int sub_column;

  private Symbol sym(short id) {
    return new Symbol(id, yyline + 1, yycolumn + 1, len(), str());
  }

  private Symbol sym(short id, String value) {
    return new Symbol(id, yyline + 1, yycolumn + 1, len(), value);
  }

  private String str() { return yytext(); }
  private int len() { return yylength(); }

  public JavaScanner() {
  }

  public String extractPackageName(String fileName) {
    StringBuffer packageName = new StringBuffer();
    try {
      Reader reader = new FileReader(fileName);
      JavaScanner scanner = new JavaScanner(new Unicode(reader));
      Symbol sym = scanner.nextToken();
      if(sym.getId() == Terminals.PACKAGE) {
        while(true) {
          sym = scanner.nextToken();
          if(sym.getId() == Terminals.SEMICOLON)
            break;
          packageName.append(sym.value);
        }
      }
      reader.close();
      if(packageName.length() != 0)
        packageName.append(".");
    } catch (java.lang.Exception e) {
    }
    return packageName.toString();
  }
%}

// 3.4 Line Terminators
LineTerminator = \n|\r|\r\n
InputCharacter = [^\r\n]

// 3.6 White Space
WhiteSpace = [ ] | \t | \f | {LineTerminator}

// 3.7 Comments
Comment = {TraditionalComment}
        | {EndOfLineComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/" | "/*" "*"+ [^/*] ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

// 3.8 Identifiers
Identifier = [:jletter:][:jletterdigit:]*

// 3.10.1 Integer Literals
DecimalNumeral = 0 | {NonZeroDigit} {Digits}? 
HexNumeral = 0 [xX] [0-9a-fA-F]+
OctalNumeral = 0 [0-7]+

Digits = {Digit}+
Digit = 0 | {NonZeroDigit}
NonZeroDigit = [1-9]

// 3.10.2 Floating-Point Literals
FloatingPointLiteral = {Digits} \. {Digits}? {ExponentPart}?
                     | \. {Digits} {ExponentPart}?
                     | {Digits} {ExponentPart}
ExponentPart = [eE] [+-]? [0-9]+

// 3.10.4 Character Literals
SingleCharacter = [^\r\n\'\\]

// 3.10.5 String Literals
StringCharacter = [^\r\n\"\\]

// 3.10.6 Escape Sequences for Character and String Literals
OctalEscape = \\ {OctalDigit} 
            | \\ {OctalDigit} {OctalDigit}
            | \\  {ZeroToThree} {OctalDigit} {OctalDigit}
OctalDigit = [0-7]
ZeroToThree = [0-3]

%state STRING

%%

<YYINITIAL> {
  // 3.6 White Space
  {WhiteSpace}                   { }
  
  // 3.7 Comments
  {Comment}                      { }

  // 3.8 Identifiers located at end of current state due to rule priority disambiguation

  // 3.9 Keywords
  "assert"                       { return sym(Terminals.ASSERT); }
  "abstract"                     { return sym(Terminals.ABSTRACT); }
  "boolean"                      { return sym(Terminals.BOOLEAN); }
  "break"                        { return sym(Terminals.BREAK); }
  "byte"                         { return sym(Terminals.BYTE); }
  "case"                         { return sym(Terminals.CASE); }
  "catch"                        { return sym(Terminals.CATCH); }
  "char"                         { return sym(Terminals.CHAR); }
  "class"                        { return sym(Terminals.CLASS); }
  "const"                        { return sym(Terminals.EOF); }
  "continue"                     { return sym(Terminals.CONTINUE); }
  "default"                      { return sym(Terminals.DEFAULT); }
  "do"                           { return sym(Terminals.DO); }
  "double"                       { return sym(Terminals.DOUBLE); }
  "else"                         { return sym(Terminals.ELSE); }
  "extends"                      { return sym(Terminals.EXTENDS); }
  "final"                        { return sym(Terminals.FINAL); }
  "finally"                      { return sym(Terminals.FINALLY); }
  "float"                        { return sym(Terminals.FLOAT); }
  "for"                          { return sym(Terminals.FOR); }
  "goto"                         { return sym(Terminals.EOF); }
  "if"                           { return sym(Terminals.IF); }
  "implements"                   { return sym(Terminals.IMPLEMENTS); }
  "import"                       { return sym(Terminals.IMPORT); }
  "instanceof"                   { return sym(Terminals.INSTANCEOF); }
  "int"                          { return sym(Terminals.INT); }
  "interface"                    { return sym(Terminals.INTERFACE); }
  "long"                         { return sym(Terminals.LONG); }
  "native"                       { return sym(Terminals.NATIVE); }
  "new"                          { return sym(Terminals.NEW); }
  "package"                      { return sym(Terminals.PACKAGE); }
  "private"                      { return sym(Terminals.PRIVATE); }
  "protected"                    { return sym(Terminals.PROTECTED); }
  "public"                       { return sym(Terminals.PUBLIC); }
  "return"                       { return sym(Terminals.RETURN); }
  "short"                        { return sym(Terminals.SHORT); }
  "static"                       { return sym(Terminals.STATIC); }
  "strictfp"                     { return sym(Terminals.STRICTFP); }
  "super"                        { return sym(Terminals.SUPER); }
  "switch"                       { return sym(Terminals.SWITCH); }
  "synchronized"                 { return sym(Terminals.SYNCHRONIZED); }
  "this"                         { return sym(Terminals.THIS); }
  "throw"                        { return sym(Terminals.THROW); }
  "throws"                       { return sym(Terminals.THROWS); }
  "transient"                    { return sym(Terminals.TRANSIENT); }
  "try"                          { return sym(Terminals.TRY); }
  "void"                         { return sym(Terminals.VOID); }
  "volatile"                     { return sym(Terminals.VOLATILE); }
  "while"                        { return sym(Terminals.WHILE); }

  // 3.10 Literals
  
  // 3.10.1 Integer Literals
  {DecimalNumeral}               { return sym(Terminals.INTEGER_LITERAL); }
  {DecimalNumeral} [lL]          { return sym(Terminals.LONG_LITERAL, str().substring(0,len()-1)); }

  {HexNumeral}                   { return sym(Terminals.INTEGER_LITERAL); }
  {HexNumeral} [lL]              { return sym(Terminals.LONG_LITERAL, str().substring(0, len()-1)); }

  {OctalNumeral}                 { return sym(Terminals.INTEGER_LITERAL); }
  {OctalNumeral} [lL]            { return sym(Terminals.LONG_LITERAL, str().substring(0, len()-1)); }

  // 3.10.2 Floating-Point Literals
  {FloatingPointLiteral} [fF]    { return sym(Terminals.FLOATING_POINT_LITERAL, str().substring(0,len()-1)); }
  {FloatingPointLiteral} [dD]    { return sym(Terminals.DOUBLE_LITERAL, str().substring(0,len()-1)); }
  {FloatingPointLiteral}         { return sym(Terminals.DOUBLE_LITERAL); }
  [0-9]+ {ExponentPart}? [fF]    { return sym(Terminals.FLOATING_POINT_LITERAL, str().substring(0,len()-1)); }
  [0-9]+ {ExponentPart}? [dD]    { return sym(Terminals.DOUBLE_LITERAL, str().substring(0,len()-1)); }
  
  // 3.10.3 Boolean Literals
  "true"                         { return sym(Terminals.BOOLEAN_LITERAL); }
  "false"                        { return sym(Terminals.BOOLEAN_LITERAL); }
  
  // 3.10.4 Character Literals
  \'{SingleCharacter}\'          { return sym(Terminals.CHARACTER_LITERAL, str().substring(1, len()-1)); }
  // 3.10.6 Escape Sequences for Character Literals
  \'"\\b"\'                      { return sym(Terminals.CHARACTER_LITERAL, "\b"); }
  \'"\\t"\'                      { return sym(Terminals.CHARACTER_LITERAL, "\t"); }
  \'"\\n"\'                      { return sym(Terminals.CHARACTER_LITERAL, "\n"); }
  \'"\\f"\'                      { return sym(Terminals.CHARACTER_LITERAL, "\f"); }
  \'"\\r"\'                      { return sym(Terminals.CHARACTER_LITERAL, "\r"); }
  \'"\\\""\'                     { return sym(Terminals.CHARACTER_LITERAL, "\""); }
  \'"\\'"\'                      { return sym(Terminals.CHARACTER_LITERAL, "\'"); }
  \'"\\\\"\'                     { return sym(Terminals.CHARACTER_LITERAL, "\\"); }
  \'{OctalEscape}\'              { int val = Integer.parseInt(str().substring(2,len()-1),8);
			                             return sym(Terminals.CHARACTER_LITERAL, new Character((char)val).toString()); }
  // Character Literal errors
  \'\\.                          { throw new LexicalError("Illegal escape sequence \""+str()+"\""); }
  \'{LineTerminator}             { throw new LexicalError("Unterminated character literal at end of line"); }

  // 3.10.5 String Literals
  \"                             { yybegin(STRING); strbuf.setLength(0); }

  // 3.10.7 The Null Literal
  "null"                         { return sym(Terminals.NULL_LITERAL); }

  // 3.11 Separators
  "("                            { return sym(Terminals.LPAREN); }
  ")"                            { return sym(Terminals.RPAREN); }
  "{"                            { return sym(Terminals.LBRACE); }
  "}"                            { return sym(Terminals.RBRACE); }
  "["                            { return sym(Terminals.LBRACK); }
  "]"                            { return sym(Terminals.RBRACK); }
  ";"                            { return sym(Terminals.SEMICOLON); }
  ","                            { return sym(Terminals.COMMA); }
  "."                            { return sym(Terminals.DOT); }

  // 3.12 Operators
  "="                            { return sym(Terminals.EQ); }
  ">"                            { return sym(Terminals.GT); }
  "<"                            { return sym(Terminals.LT); }
  "!"                            { return sym(Terminals.NOT); }
  "~"                            { return sym(Terminals.COMP); }
  "?"                            { return sym(Terminals.QUESTION); }
  ":"                            { return sym(Terminals.COLON); }
  "=="                           { return sym(Terminals.EQEQ); }
  "<="                           { return sym(Terminals.LTEQ); }
  ">="                           { return sym(Terminals.GTEQ); }
  "!="                           { return sym(Terminals.NOTEQ); }
  "&&"                           { return sym(Terminals.ANDAND); }
  "||"                           { return sym(Terminals.OROR); }
  "++"                           { return sym(Terminals.PLUSPLUS); }
  "--"                           { return sym(Terminals.MINUSMINUS); }
  "+"                            { return sym(Terminals.PLUS); }
  "-"                            { return sym(Terminals.MINUS); }
  "*"                            { return sym(Terminals.MULT); }
  "/"                            { return sym(Terminals.DIV); }
  "&"                            { return sym(Terminals.AND); }
  "|"                            { return sym(Terminals.OR); }
  "^"                            { return sym(Terminals.XOR); }
  "%"                            { return sym(Terminals.MOD); }
  "<<"                           { return sym(Terminals.LSHIFT); }
  ">>"                           { return sym(Terminals.RSHIFT); }
  ">>>"                          { return sym(Terminals.URSHIFT); }
  "+="                           { return sym(Terminals.PLUSEQ); }
  "-="                           { return sym(Terminals.MINUSEQ); }
  "*="                           { return sym(Terminals.MULTEQ); }
  "/="                           { return sym(Terminals.DIVEQ); }
  "&="                           { return sym(Terminals.ANDEQ); }
  "|="                           { return sym(Terminals.OREQ); }
  "^="                           { return sym(Terminals.XOREQ); }
  "%="                           { return sym(Terminals.MODEQ); }
  "<<="                          { return sym(Terminals.LSHIFTEQ); }
  ">>="                          { return sym(Terminals.RSHIFTEQ); }
  ">>>="                         { return sym(Terminals.URSHIFTEQ); }
  
  // 3.8 Identifiers
  {Identifier}                   { return sym(Terminals.IDENTIFIER); }
}

// 3.10.5 String Literals
<STRING> {
  \"                             { yybegin(YYINITIAL); return sym(Terminals.STRING_LITERAL, strbuf.toString()); }

  {StringCharacter}+             { strbuf.append(str()); }

  // 3.10.6 Escape sequences for String Literals
  "\\b"                          { strbuf.append( '\b' ); }
  "\\t"                          { strbuf.append( '\t' ); }
  "\\n"                          { strbuf.append( '\n' ); }
  "\\f"                          { strbuf.append( '\f' ); }
  "\\r"                          { strbuf.append( '\r' ); }
  "\\\""                         { strbuf.append( '\"' ); }
  "\\'"                          { strbuf.append( '\'' ); }
  "\\\\"                         { strbuf.append( '\\' ); }
  {OctalEscape}                  { strbuf.append((char)Integer.parseInt(str().substring(1),8)); }

  // String Literal errors
  \\.                            { throw new LexicalError("Illegal escape sequence \""+str()+"\""); }
  {LineTerminator}               { throw new LexicalError("Unterminated string at end of line"); }
}

// hack to detect the SUB character as the last input character
\u001a                           { if(sub_line == 0 && sub_column == 0) {
                                     sub_line = yyline; sub_column = yycolumn;
                                   } 
                                 }
// fall through errors
.|\n                             { throw new LexicalError("Illegal character \""+str()+ "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>                          { // detect position of first SUB character
                                   if(!(sub_line == 0 && sub_column == 0) && (sub_line != yyline || sub_column != yycolumn-1))
                                     throw new LexicalError("Error");
                                   return sym(Terminals.EOF);
                                 }
