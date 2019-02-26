package front.parse.ast;

import front.parse.ast.Attribute;
import front.parse.ast.Expression;

import java.util.HashMap;

public class CaseStatement extends Expression {
    private Expression expression;
    private HashMap<Attribute, Expression> patterns;

    public CaseStatement(Expression expression, HashMap<Attribute, Expression> patterns) {
        this.expression = expression;
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("case ").append(expression).append(" of\n");
        for (Attribute attribute : patterns.keySet())
            { sb.append(attribute).append("=>").append(patterns.get(attribute)).append("\n"); }
        return sb.toString();
    }
}
