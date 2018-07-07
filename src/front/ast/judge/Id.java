package front.ast.judge;

import front.ast.Type;

public class Id extends Judge{
    private Type type;
    private String name;

    public Id(Type type, String name) {
        this.type = type;
        this.name = name;
        kind = Kind.ID;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if(type != null) {
            return  type + " " + name;
        }else {
            return name;
        }
    }
}
