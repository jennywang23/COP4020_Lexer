package edu.ufl.cise.plc;
import java.util.ArrayList;


public class Lexer implements ILexer {
    private int pos;
    final char[] chars;
    private ArrayList<Token> tokenList;
    private enum State {
        START, IN_IDENT, HAVE_ZERO, HAVE_DOT,
        IN_FLOAT, IN_NUM, HAVE_EQ, HAVE_MINUS,
        HAVE_BANG, HAVE_LT, HAVE_GT, HAVE_DASH
    }

    Lexer(String input) {
        this.pos = 0;
        this.chars = input.toCharArray();
        boolean notEndState = true;
        boolean notEOF = true;
        State state = State.START;
        int tokenPos = 0;
        String tokenStr = "";
        while (notEOF) {
            tokenStr = "";
            state = State.START;
            tokenPos = pos;
            while (notEndState) {
                // loop over chars–assumes we have added a sentinel that will cause loop to terminate
                //      each iteration increments pos (
                char ch = chars[pos]; // get current char
                switch (state) {
                    case START -> {
                        startPos = pos;  //save position of first char in token
                        switch (ch) {
                            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                                    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
                                    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_',
                                    '$' -> {
                                state = State.IN_IDENT;
                                pos++;
                            }
                            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                state = State.IN_NUM;
                                pos++;
                            }
                            case '0' -> {
                                state = State.HAVE_ZERO;
                                pos++;
                            }
                            case ' ', '\n', '\r', '\t' -> {
                                pos++;
                            }
                            case '&' -> {
                                Token temp = new Token(IToken.Kind.AND, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '=' -> {
                                state = State.HAVE_EQ;
                                pos++;
                            }
                            case ',' -> {
                                Token temp = new Token(IToken.Kind.COMMA, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '/' -> {
                                Token temp = new Token(IToken.Kind.DIV, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '>' -> {
                                state = State.HAVE_GT;
                                pos++;
                            }
                            case '<' -> {
                                state = State.HAVE_LT;
                                pos++;
                            }
                            case '(' -> {
                                Token temp = new Token(IToken.Kind.LPAREN, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '[' -> {
                                Token temp = new Token(IToken.Kind.LSQUARE, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '-' -> {
                                state = State.HAVE_DASH;
                                pos++;
                            }
                            case '%' -> {
                                Token temp = new Token(IToken.Kind.MOD, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '!' -> {
                                state = State.HAVE_BANG;
                                pos++;
                            }
                            case '|' -> {
                                Token temp = new Token(IToken.Kind.OR, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '+' -> { //handle all single char tokens like this
                                Token temp = new Token(IToken.Kind.PLUS, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '^' -> {
                                Token temp = new Token(IToken.Kind.RETURN, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case ')' -> {
                                Token temp = new Token(IToken.Kind.RPAREN, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case ']' -> {
                                Token temp = new Token(IToken.Kind.RSQUARE, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case ';' -> {
                                Token temp = new Token(IToken.Kind.SEMI, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '*' -> {
                                Token temp = new Token(IToken.Kind.TIMES, startPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case 0 -> {
                                //this is the end of the input, add an EOF token and return;
                                notEOF = false;
                            }
                        }
                    }
                    case IN_IDENT -> {
                        // do something if current identifier is a reserved keyword
                        switch (ch) {
                            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                                    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
                                    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_',
                                    '$', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                state = State.IN_IDENT;
                                pos++;
                            }
                        }
                    }
                    case HAVE_ZERO -> {
                        switch (ch) {
                            case '.' -> {
                                state = State.IN_FLOAT;
                                tokenStr += "0.";
                                pos++;
                            }
                            default -> {
                                Token temp = new Token(IToken.Kind.INT_LIT, "0", pos, 1);
                                tokenList.add(temp);
                            }
                        }
                    }
                    case IN_FLOAT -> {
                        switch (ch) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                tokenStr += ch;
                                pos++;
                            }
                            default -> {
                                Token temp = new Token(IToken.Kind.FLOAT_LIT, tokenStr, pos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case IN_NUM -> {
                        switch (ch) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                tokenStr += ch;
                                pos++;
                            }
                            default -> {
                                Token temp = new Token(IToken.Kind.INT_LIT, tokenStr, tokenPos,pos-tokenPos);
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_EQ -> {
                        switch (ch) {
                            case '=' -> {
                                Token temp = new Token(IToken.Kind.EQUALS, "==", tokenPos, 2);
                                tokenList.add(temp);
                                pos++;
                            }
                            default -> {
                                //how to make an assignment token '='
                                //handle error
                            }
                        }
                    }
                    case HAVE_MINUS -> {
                    }
                    case HAVE_BANG -> {
                    }
                    case HAVE_LT -> {
                    }
                    case HAVE_GT -> {
                    }
                    case HAVE_DASH -> {
                    }
                    default -> throw new IllegalStateException("lexer bug");
                }//end of swith on state
            }//end of while
        }
        //Loop
        //Switch statement
        //pos++
        //line++
        //return Token
    }   //return next token and advance internal position
    }

    @Override
    public Token next() {

    }
    @Override
    public Token peek() {}  //return next token but do not advance internal position
}
