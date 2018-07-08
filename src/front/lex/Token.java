package front.lex;

import java.util.Arrays;
import java.util.HashSet;

public class Token {
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

    public enum Type{
        /**
         *  enumerate the type of tokens
         */
        ID, KEYWORD, NUMBER, STRING, OPERATOR, PUNCTUATION;
    }

    public Type type;
    public String value;
    public int number;
    public int row;
    public int column;

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
}
