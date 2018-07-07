package front.ast.stmt;

import front.ast.judge.Id;
import front.ast.judge.Judge;

public class Def extends Stmt{
    private Id id;
    private Judge judge;

    public Def(Id id, Judge judge) {
        this.id = id;
        this.judge = judge;
        kind = Kind.DefStmt;
    }

    public Judge getJudge() {
        return judge;
    }

    public Id getId() {
        return id;
    }

}
