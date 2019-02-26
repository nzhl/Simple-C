package front.parse.ast;

public class WhileStatement extends Expression{
    private Expression condition;
    private Expression body;

    public WhileStatement(Expression condition, Expression body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[while ").append(condition).append(" do ").append(body) .append("]");
        return sb.toString();
    }
}
