package front.parse.ast;

public class Assignment extends Expression{
    private String name;
    private Expression value;

    public Assignment(String name, Expression value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(name).append("<-").append(value).append("]");
        return sb.toString();
    }
}
