package front.ast.judge;

public class Division extends Judge {
    public Judge left;
    public Judge right;

    public Division(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Kind.DIVISION;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " / " + "(" + right.toString() + ")";
    }
}
