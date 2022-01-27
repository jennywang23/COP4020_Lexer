package edu.ufl.cise.plc;

class Token implements IToken {
    final Kind kind;
    final String input;
    final int pos;
    final int length;
    public record SourceLocation(int line, int column) {}

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
    public int getIntValue();

    //returns the float value represented by the characters of this token if kind is FLOAT_LIT
    @Override
    public float getFloatValue();

    //returns the boolean value represented by the characters of this token if kind is BOOLEAN_LIT
    @Override
    public boolean getBooleanValue();

    //returns the String represented by the characters of this token if kind is STRING_LIT
    //The delimiters should be removed and escape sequences replaced by the characters they represent.
    @Override
    public String getStringValue();
}