package front.parse.ast;

import java.util.ArrayList;

public class Expressions extends Expression{
    ArrayList<Expression> expressions;

    public Expressions(ArrayList<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n -- more than one -- \n");
        for (Expression e : expressions) { sb.append(e).append("\n"); }
        return sb.toString();
    }
}
