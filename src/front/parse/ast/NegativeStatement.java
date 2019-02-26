package front.parse.ast;

public class NegativeStatement extends Expression{
    private Expression expression;

    public NegativeStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "-" + expression;
    }
}
