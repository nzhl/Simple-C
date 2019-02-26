package front.semantic;

import java.util.HashMap;

public class InheritanceGraph {
    public static HashMap<String, InheritanceGraph> classTable = new HashMap<>();
    static {
         classTable.put("Object", new InheritanceGraph("Object", null));
        classTable.put("IO", new InheritanceGraph("IO", "Object"));
        classTable.get("Object").children.put("IO", classTable.get("IO"));
    }

    public static void addClass(String className, String parentName) {
        classTable.put(className, new InheritanceGraph(className, parentName));
        classTable.get(parentName).children.put(className, classTable.get(className));
    }

    // least upper bound
    public static String findLUB(String name1, String name2) {
        if (name1.equals(name2)) { return name1; }

        InheritanceGraph node1 = classTable.get(name1);
        InheritanceGraph node2 = classTable.get(name2);
        int depth1 = node1.getDepth();
        int depth2 = node2.getDepth();

        if (depth1 > depth2) {
            while (! node2.isAncestorOf(node1)) {
                node2 = classTable.get(node2.parent);
            }
            return node2.className;
        } else {
            while (! node1.isAncestorOf(node2)) {
                node1 = classTable.get(node1.parent);
            }
            return node1.className;
        }
    }


    private String className;
    private String parent;
    private HashMap<String, InheritanceGraph> children = new HashMap<>();

    private InheritanceGraph(String className, String parent) {
        this.className = className;
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(className).append("{");
        for (String each : children.keySet())  { sb.append(each).append(" "); }
        sb.append("}(");
        for (InheritanceGraph each : children.values())  { sb.append("\n\t").append(each).append("\n"); }
        sb.append(")");
        return sb.toString();
    }

    private int getDepth() {
        if ("Object".equals(className)) {
            return 0;
        } else {
            return classTable.get(parent).getDepth() + 1;
        }
    }

    private boolean isAncestorOf(InheritanceGraph node) {
        while (node.parent != null) {
             if (node.parent.equals(this.className)) {
                 return true;
            }
            node = classTable.get(node.parent);
        }
        return false;
    }
}
