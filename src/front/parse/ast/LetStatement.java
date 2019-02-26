package front.parse.ast;

import front.parse.ast.Attribute;
import front.parse.ast.Expression;

import java.util.HashMap;

public class LetStatement extends Expression{
    private HashMap<String, Attribute> variables;
    private Expression body;

    public LetStatement(HashMap<String, Attribute> variables, Expression body) {
        this.variables = variables;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("let ");
        for (Attribute e : variables.values()) { sb.append(e).append(" "); }
        sb.append("in ").append(body);
        return sb.toString();
    }
}
