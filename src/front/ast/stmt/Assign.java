package front.ast.stmt;

import front.ast.judge.Id;
import front.ast.judge.Judge;

public class Assign extends Stmt {
    private Id id;
    private Judge judge;

    public Assign(Id id, Judge judge) {
        this.id = id;
        this.judge = judge;
        kind = Kind.AssignStmt;
    }

    public Id getId() {
        return id;
    }

    public Judge getJudge() {
        return judge;
    }
}
