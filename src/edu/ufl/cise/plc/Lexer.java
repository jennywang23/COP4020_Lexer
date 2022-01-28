package edu.ufl.cise.plc;

public class Lexer implements ILexer {
    private int pos;
    final char[] chars;

    Lexer(String input) {
        this.pos = 0;
        this.chars = input.toCharArray();
    }

    private enum State {
        START, IN_IDENT, HAVE_ZERO, HAVE_DOT,
        IN_FLOAT, IN_NUM, HAVE_EQ, HAVE_MINUS
    }

    @Override
    public Token next() {
        State state = State.START;
        int startPos = 0;
        while (true) {
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
                        // string_lit
                        // reserved
                        case '&' -> {
                            Token temp = new Token(IToken.Kind.AND, startPos, 1);
                            pos++;
                        }
                        case '=' -> {
                            state = State.HAVE_EQ;
                            pos++;
                        }
                        case '!' -> {
                            Token temp = new Token(IToken.Kind.BANG, startPos, 1);
                            pos++;
                        }
                        case ',' -> {
                            Token temp = new Token(IToken.Kind.COMMA, startPos, 1);
                            pos++;
                        }
                        case '/' -> {
                            Token temp = new Token(IToken.Kind.DIV, startPos, 1);
                            pos++;
                        }
                        // >=, >, <<, <-, <=
                        // (, [, <, -, %, !=
                        // |
                        case '+' -> { //handle all single char tokens like this
                            Token temp = new Token(IToken.Kind.PLUS, startPos, 1);
                            pos++;
                        }
                        // >>, ->, ^, ), ]
                        // ;, *
                        case 0 -> {
                            //this is the end of the input, add an EOF token and return;
                            return new Token(IToken.Kind.EOF, startPos, 0);
                        }
                    }
                }
                case IN_IDENT-> {
                    // do something if current identifier is a reserved keyword
                    switch(ch) {
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
                    switch(ch) {
                        case '.' -> {
                            state = State.HAVE_DOT;
                            pos++;
                        }
                        default -> {
                            return new Token(IToken.Kind.INT_LIT,pos, 1);
                        }
                    }
                }
                case HAVE_DOT -> {
                }
                case IN_FLOAT -> {
                }
                case IN_NUM -> {
                    switch(ch) {
                        case '0','1','2','3','4','5','6','7','8','9' -> {
                            pos++;  //still in number,
                            //increment pos to read next char
                        }
                        default -> {
                            //Token temp = new Token(IToken.Kind.INT_LIT, pos = tokenPos,pos-tokenPos);
                            //next char is not part of this token,
                            //so do not increment pos
                            state = State.START;
                        }
                    }
                }
                case HAVE_EQ -> {
                    switch(ch) {
                        case '=' -> {
                            Token temp = new Token(IToken.Kind.EQUALS, startPos, 2);
                            pos++;
                        }
                        default -> {
                            //handle error
                        }
                    }
                }
                case HAVE_MINUS -> {
                }
                    default -> throw new IllegalStateException("lexer bug");
            }//end of swith on state
        }//end of while
        //Loop
            //Switch statement
            //pos++
            //line++
        //return Token
    }   //return next token and advance internal position
    @Override
    public Token peek() {}  //return next token but do not advance internal position
}
