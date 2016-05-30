package front.lex;

public class Token {
    public enum Type{
        //-------------------------> variable type ,assign, logical operator, arithmetic operator, relational operator, bracket, semicolon
        IF, ELSE, WHILE, ID, NUM, BOOL, STR, TYPE, ASSIGN, LOP, AOP, ROP, BRA, SEMI, PRINT
    }

    public Type type;
    public String value;
    public int line;
    public int pos;

    public Token(Type type, String value, int line, int pos) {
        if(type == Type.ID && value.equals("if")){
            this.type = Type.IF;
        }else if(type == Type.ID && value.equals("else")){
            this.type = Type.ELSE;
        }else if(type == Type.ID && value.equals("while")){
            this.type = Type.WHILE;
        }else if(type == Type.ID && (value.equals("int") || value.equals("bool") || value.equals("string"))){
            this.type = Type.TYPE;
        }else if(type == Type.ID && (value.equals("True") || value.equals("False"))){
            this.type = Type.BOOL;
        }else if(type == type.ID && value.equals("print")){
            this.type = Type.PRINT;
        } else {
            this.type = type;
        }

        this.value = value;
        this.line = line;
        this.pos = pos - value.length();
    }


    @Override
    public String toString() {
        return type + "("+ value +")" + "("+ line + ", " + pos +")";
    }
}
