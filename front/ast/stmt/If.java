package front.ast.stmt;

import front.ast.judge.Judge;

public class If extends Stmt{
    private Judge condition;
    private Stmt then;
    private Stmt otherwise;

    public If(Judge condition, Stmt then, Stmt otherwise) {
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
        kind = Kind.IfStmt;
    }

    public Judge getCondition() {
        return condition;
    }

    public Stmt getThen() {
        return then;
    }

    public Stmt getOtherwise() {
        return otherwise;
    }
}
