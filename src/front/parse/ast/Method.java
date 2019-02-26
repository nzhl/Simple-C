package front.parse.ast;

import java.util.ArrayList;

public class Method {
    private String name;
    private ArrayList<Attribute> params;
    private String returnType;
    private Expression body;

    public Method(String name, ArrayList<Attribute> params, String returnType, Expression body) {
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(returnType).append(" ").append(name).append("(");
        for (Attribute para : params) { sb.append(para).append(", "); }
        sb.append(")").append("{").append(body).append("}");
        return sb.toString();
    }
}
