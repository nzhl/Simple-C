package front.ast.judge;

public class Le extends Judge {
    public Judge left;
    public Judge right;

    public Le(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.LE;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " <= " + "(" + right.toString() + ")";
    }
}
