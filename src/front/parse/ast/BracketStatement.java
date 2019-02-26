package front.parse.ast;

public class BracketStatement extends Expression{
    private Expression expression;

    public BracketStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "(" + expression + ")";
    }
}
