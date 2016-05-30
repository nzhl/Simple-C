package front.ast.judge;

public class Negative extends Judge {
    Judge follow;

    public Negative(Judge follow) {
        this.follow = follow;
        kind = Kind.NEGATIVE;
    }

    @Override
    public String toString() {
        return "-(" + follow.toString() + ")";
    }
}
