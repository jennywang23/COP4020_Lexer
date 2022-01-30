package edu.ufl.cise.plc;
import java.util.ArrayList;

public class Lexer implements ILexer {
    private int pos;
    final char[] chars;
    private ArrayList<Token> tokenList;
    private enum State {
        START, IN_IDENT, HAVE_ZERO, HAVE_DOT,
        IN_FLOAT, IN_NUM, HAVE_EQ, HAVE_BANG,
        HAVE_LT, HAVE_GT, HAVE_DASH
    }

    Lexer(String input) {
        pos = 0;
        chars = input.toCharArray();
        boolean notEndState;
        boolean notEOF = true;
        State state;
        int tokenPos;
        String tokenStr;
        while (notEOF) {
            tokenStr = "";
            state = State.START;
            tokenPos = pos;
            notEndState = true;
            while (notEndState) {
                // loop over chars–assumes we have added a sentinel that will cause loop to terminate
                //      each iteration increments pos (
                char ch = chars[pos]; // get current char
                switch (state) {
                    case START -> {
                        tokenPos = pos;  //save position of first char in token
                        switch (ch) {
                            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                                    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
                                    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_',
                                    '$' -> {
                                tokenStr += ch;
                                state = State.IN_IDENT;
                                pos++;
                            }
                            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                state = State.IN_NUM;
                                tokenStr += ch;
                                pos++;
                            }
                            case '0' -> {
                                state = State.HAVE_ZERO;
                                tokenStr += ch;
                                pos++;
                            }
                            case ' ', '\n', '\r', '\t' -> {
                                pos++;
                            }
                            case '&' -> {
                                Token temp = new Token(IToken.Kind.AND, "&", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '=' -> {
                                state = State.HAVE_EQ;
                                tokenStr += ch;
                                pos++;
                            }
                            case ',' -> {
                                Token temp = new Token(IToken.Kind.COMMA, ",", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '/' -> {
                                Token temp = new Token(IToken.Kind.DIV, "/", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '>' -> {
                                state = State.HAVE_GT;
                                tokenStr += ch;
                                pos++;
                            }
                            case '<' -> {
                                state = State.HAVE_LT;
                                tokenStr += ch;
                                pos++;
                            }
                            case '(' -> {
                                Token temp = new Token(IToken.Kind.LPAREN, "(", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '[' -> {
                                Token temp = new Token(IToken.Kind.LSQUARE, "[", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '-' -> {
                                state = State.HAVE_DASH;
                                tokenStr += ch;
                                pos++;
                            }
                            case '%' -> {
                                Token temp = new Token(IToken.Kind.MOD, "%", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '!' -> {
                                state = State.HAVE_BANG;
                                tokenStr += ch;
                                pos++;
                            }
                            case '|' -> {
                                Token temp = new Token(IToken.Kind.OR, "|", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '+' -> { //handle all single char tokens like this
                                Token temp = new Token(IToken.Kind.PLUS, "+", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '^' -> {
                                Token temp = new Token(IToken.Kind.RETURN, "^", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case ')' -> {
                                Token temp = new Token(IToken.Kind.RPAREN, ")", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case ']' -> {
                                Token temp = new Token(IToken.Kind.RSQUARE, "]", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case ';' -> {
                                Token temp = new Token(IToken.Kind.SEMI, ";", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case '*' -> {
                                Token temp = new Token(IToken.Kind.TIMES, "*", tokenPos, 1);
                                tokenList.add(temp);
                                pos++;
                            }
                            case 0 -> {
                                //this is the end of the input, add an EOF token and return;
                                Token temp = new Token(IToken.Kind.EOF, tokenPos, 1);
                                tokenList.add(temp);
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
                                tokenStr += ch;
                                pos++;
                            }
                            default -> {
                                Token temp;
                                switch(tokenStr) {
                                    case "string", "int", "float", "boolean", "color", "void" -> {
                                        temp = new Token(Token.Kind.TYPE, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "getWidth", "getHeight" -> {
                                        temp = new Token(Token.Kind.IMAGE_OP, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "BLACK", "BLUE", "CYAN", "DARK_GRAY", "GRAY",
                                            "GREEN", "LIGHT_GRAY", "MAGENTA", "ORANGE", "PINK",
                                            "RED", "WHITE", "YELLOW" -> {
                                        temp = new Token(Token.Kind.COLOR_CONST, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "getRed", "getGreen", "getBlue" -> {
                                        temp = new Token(Token.Kind.COLOR_OP, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "true", "false" -> {
                                        temp = new Token(Token.Kind.BOOLEAN_LIT, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "if" -> {
                                        temp = new Token(Token.Kind.KW_IF, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "else" -> {
                                        temp = new Token(Token.Kind.KW_ELSE, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "fi" -> {
                                        temp = new Token(Token.Kind.KW_FI, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "write" -> {
                                        temp = new Token(Token.Kind.KW_WRITE, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    case "console" -> {
                                        temp = new Token(Token.Kind.KW_ELSE, tokenStr, tokenPos, tokenStr.length());
                                    }
                                    default -> {
                                        temp = new Token(Token.Kind.IDENT, tokenStr, tokenPos, tokenStr.length());
                                    }
                                }
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_ZERO -> {
                        switch (ch) {
                            case '.' -> {
                                state = State.IN_FLOAT;
                                tokenStr += ch;
                                pos++;
                            }
                            default -> {
                                Token temp = new Token(IToken.Kind.INT_LIT, "0", pos, 1);
                                tokenList.add(temp);
                                notEndState = false;
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
                                Token temp = new Token(IToken.Kind.INT_LIT, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_EQ -> {
                        switch (ch) {
                            case '=' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.EQUALS, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            default -> {
                                Token temp = new Token(Token.Kind.ASSIGN, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_BANG -> {
                        switch (ch) {
                            case '=' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.NOT_EQUALS, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            default -> {
                                Token temp = new Token(Token.Kind.BANG, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_LT -> {
                        switch (ch) {
                            case '-' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.LARROW, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            case '<' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.LANGLE, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            case '=' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.LE, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            default -> {
                                Token temp = new Token(Token.Kind.LT, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_GT -> {
                        switch (ch) {
                            case '>' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.RANGLE, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            case '=' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.GE, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            default -> {
                                Token temp = new Token(Token.Kind.GT, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
                    }
                    case HAVE_DASH -> {
                        switch (ch) {
                            case '>' -> {
                                tokenStr += ch;
                                Token temp = new Token(Token.Kind.RARROW, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                pos++;
                                notEndState = false;
                            }
                            default -> {
                                Token temp = new Token(Token.Kind.MINUS, tokenStr, tokenPos, tokenStr.length());
                                tokenList.add(temp);
                                notEndState = false;
                            }
                        }
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
