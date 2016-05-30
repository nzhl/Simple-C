package front.ast.judge;

public class Minus extends Judge {
    public Judge left;
    public Judge right;

    public Minus(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.MINUS;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " - " + "(" + right.toString() + ")";
    }
}
