package edu.ufl.cise.plc;

public class Lexer implements ILexer {
    IToken next();   //return next token and advance internal position
    IToken peek();  //return next token but do not advance internal position
}
