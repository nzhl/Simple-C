package front.ast.stmt;

import front.ast.judge.Judge;

public class While extends Stmt{
    private Judge condition;
    private Stmt then;

    public While(Judge condition, Stmt then) {
        this.condition = condition;
        this.then = then;
        kind = Kind.WhileStmt;
    }

    public Judge getCondition() {
        return condition;
    }

    public Stmt getThen() {
        return then;
    }
}
