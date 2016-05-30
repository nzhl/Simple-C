package front.ast.judge;

public class Equal extends Judge {
    public Judge left;
    public Judge right;

    public Equal(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.EQUAL;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " == " + "(" + right.toString() + ")";
    }
}
