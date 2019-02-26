package front.parse.ast;

import front.parse.ast.Expression;

public class NewStatement extends Expression {
    private String type;

    public NewStatement(String type) {
        this.type = type;
    }

    @Override
    public String toString() { return "new " + type; }
}
