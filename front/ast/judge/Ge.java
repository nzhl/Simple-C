package front.ast.judge;

public class Ge extends Judge {
    public Judge left;
    public Judge right;

    public Ge(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.GE;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " >= " + "(" + right.toString() + ")";
    }
}
