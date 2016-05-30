package front.ast.judge;

public class Or extends Judge {
    Judge left;
    Judge right;

    public Or(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.OR;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " || " + "(" + right.toString() + ")";
    }
}
