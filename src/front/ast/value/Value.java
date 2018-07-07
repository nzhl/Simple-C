package front.ast.value;

import front.ast.judge.Judge;
import front.ast.Type;

public class Value extends Judge {
    public Type type;

    public Value() {
        kind = Kind.VAL;
    }
}
