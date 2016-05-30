package front.ast.judge;

public class And extends Judge {
    Judge left;
    Judge right;

    public And(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.AND;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " && " + "(" + right.toString() + ")";
    }
}
