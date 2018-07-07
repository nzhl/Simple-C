package front.ast.value;

import front.ast.Type;

public class Str extends Value{
    public String value;

    public Str(String value) {
        this.value = value;
        type = Type.Str;
    }

    @Override
    public String toString() {
        return value;
    }
}
