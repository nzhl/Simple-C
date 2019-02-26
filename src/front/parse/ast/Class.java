package front.parse.ast;

import java.util.HashMap;

public class Class {
    private String name;
    private String parent;
    private HashMap<String, Attribute> attributes;
    private HashMap<String, Method> methods;

    public Class(String name, String parent, HashMap<String, Attribute> attributes, HashMap<String, Method> methods) {
        this.name = name;
        this.parent = parent;
        this.attributes = attributes;
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(HashMap<String, Method> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("class ").append(name).append(" from ").append(parent).append("{\n");
        for (Attribute a : attributes.values()) { str.append(a).append("\n"); }
        for (Method m : methods.values()) { str.append(m).append("\n"); }
        return str.toString();
    }
}
