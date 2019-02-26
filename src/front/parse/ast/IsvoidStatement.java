package front.parse.ast;

public class IsvoidStatement extends Expression{
    private Expression expression;

    public IsvoidStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "isvoid " + expression;
    }
}
