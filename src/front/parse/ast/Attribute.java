package front.parse.ast;

public class Attribute {
    private String name;
    private String type;
    private Expression value;

    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public Attribute(String name, String type, Expression value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Attribute attribute = (Attribute)obj;
        return this.name.equals(attribute.getName()) && this.type.equals(attribute.getType());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ").append(name).append("=").append(value);
        return sb.toString();
    }
}
