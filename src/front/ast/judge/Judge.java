package front.ast.judge;

public class Judge {
    public enum Kind{
        ADD, MINUS, TIMES, DIVISION,NEGATIVE, NOT, OR, AND, EQUAL, GE, LE, G, L, ID, VAL
    }
    public Kind kind;
}
