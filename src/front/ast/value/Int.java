package front.ast.value;

import front.ast.Type;

public class Int extends Value{
    public int value;

    public Int(String value) {
        this.value = Integer.parseInt(value);
        type = Type.Int;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
