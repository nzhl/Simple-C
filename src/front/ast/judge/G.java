package front.ast.judge;

public class G extends Judge {
    public Judge left;
    public Judge right;

    public G(Judge left, Judge right) {
        this.left = left;
        this.right = right;
        kind = Judge.Kind.G;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + " > " + "(" + right.toString() + ")";
    }
}
