package front.ast.judge;

public class Times extends Judge {
    public Judge left;
    public Judge right;

    public Times(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.TIMES;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " * " + "(" + right.toString() + ")";
    }
}
