package front.parse.ast;

import java.util.ArrayList;

public class Invocation extends Expression{
    private Expression self;
    private String asType;
    private String name;
    private ArrayList<Expression> arguments;

    public Invocation(Expression self, String asType, String name, ArrayList<Expression> arguments) {
        this.self = self;
        this.asType = asType;
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(self).append("@").append(asType).append(".").append(name).append("(");
        for (Expression e : arguments) { sb.append(e).append(", "); }
        sb.append(")");
        return sb.toString();
    }
}
