package front.parse.ast;

public class Operator extends Expression{
    public enum OP{
        /**
         *  Base Priority
         */
        LT, LE, GT, GE, EQ,
        /**
         *  Middle Priority
         */
        TIMES, DIVISION,
        ADD, MINUS
    }
    private Expression left;
    private Expression right;
    private OP op;

    public Operator(OP op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " " + op + " " + right;
    }
}
