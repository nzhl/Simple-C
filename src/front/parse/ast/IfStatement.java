package front.parse.ast;

public class IfStatement extends Expression{
    private Expression condition;
    private Expression thenBody;
    private Expression elseBody;

    public IfStatement(Expression condition, Expression thenBody, Expression elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[if ").append(condition).append(" then ").append(thenBody)
                .append(" else ").append(elseBody).append("]");
        return sb.toString();
    }
}
