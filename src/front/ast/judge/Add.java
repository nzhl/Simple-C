package front.ast.judge;

public class Add extends Judge {
    public Judge left;
    public Judge right;

    public Add(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.ADD;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " + " + "(" + right.toString() + ")";
    }
}
