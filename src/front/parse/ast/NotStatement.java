package front.parse.ast;

public class NotStatement extends Expression{
    private Expression expression;

    public NotStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "not " + expression;
    }
}
