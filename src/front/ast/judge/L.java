package front.ast.judge;

public class L extends Judge {
    public Judge left;
    public Judge right;

    public L(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.L;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " < " + "(" + right.toString() + ")";
    }
}
