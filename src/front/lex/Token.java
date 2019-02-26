package front.lex;

import java.util.Arrays;
import java.util.HashSet;

public class Token {
    public enum Type{
        /**
         *  enumerate the type of tokens
         */
        ID, KEYWORD, NUMBER, STRING, OPERATOR, PUNCTUATION, EOF;
    }

    public static final Token EOF = new Token(Type.EOF, "",  0, 0);

    private static final HashSet<String> KEYWORD_LIST
        = new HashSet<>(Arrays.asList(
        "class", "inherits",
        "if", "then", "else", "fi",
        "while", "loop", "pool",
        "let", "in",
        "case", "of", "esac",
        "new", "isvoid",
        "not", "true", "false"
    ));


    private Type type;
    private String value;
    private int number;
    private int row;
    private int column;

    Token(Type type, String value, int row, int column) {
        this.type = type;
        if(type == Type.ID && KEYWORD_LIST.contains(value)){
            this.type = Type.KEYWORD;
        }

        this.value = value;
        this.row = row;
        this.column = column - value.length();
    }

    Token(int number, int row, int column) {
        this.type = Type.NUMBER;
        this.number = number;
        this.row = row;
        this.column = column - String.valueOf(number).length();
    }

    @Override
    public String toString() {
        return type + "("+ (type == Type.NUMBER ? number:value) +")" + "("+ row + ", " + column +")";
    }

    public boolean sameAs(Token token) {
        if (token == null) {
            return false;
        }
        return this.type == token.type && this.value == token.value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }
}
