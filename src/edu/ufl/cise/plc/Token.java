package edu.ufl.cise.plc;

class Token implements IToken {
    final IToken.Kind kind;
    final String input;
    final int pos;
    final int length;

    Token(Kind kind, String input, int pos, int length) {
        this.kind = kind;
        this.input = input;
        this.pos = pos;
        this.length = length;
    }

    //returns the token kind
    @Override
    public Kind getKind() {return kind;};

    //returns the characters in the source code that correspond to this token
    //if the token is a STRING_LIT, this returns the raw characters, including delimiting "s and unhandled escape sequences.
    @Override
    public String getText() {return input;};

    //returns the location in the source code of the first character of the token.
    @Override
    public SourceLocation getSourceLocation() {
        SourceLocation position = new SourceLocation(pos, length);
        return position;
    };

    //returns the int value represented by the characters of this token if kind is INT_LIT
    @Override
    public int getIntValue() {
        return Integer.parseInt(input);
    };

    //returns the float value represented by the characters of this token if kind is FLOAT_LIT
    @Override
    public float getFloatValue() {
        return Float.parseFloat(input);
    };

    //returns the boolean value represented by the characters of this token if kind is BOOLEAN_LIT
    @Override
    public boolean getBooleanValue() {
        return Boolean.parseBoolean(input);
    };

    //returns the String represented by the characters of this token if kind is STRING_LIT
    //The delimiters should be removed and escape sequences replaced by the characters they represent.
    @Override
    public String getStringValue() {
        return input;
    };
}