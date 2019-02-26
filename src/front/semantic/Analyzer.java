package front.semantic;

import front.parse.ast.Class;

import java.util.ArrayList;

public class Analyzer {
    ArrayList<Class> classes;

    public Analyzer(ArrayList<Class> classes) {
        this.classes = classes;
    }

    public void analysis() throws Exception {
        generateInheritanceTree();

    }

    private void generateInheritanceTree() throws Exception {
        for (Class target : classes) {
            addClass(target.getName(), null);
        }
    }

    private void addClass(String className, String origin) throws Exception {
        if (className.equals(origin)) { throw new Exception("Recursive inheritance found: " + className); }
        if (origin == null) { origin = className; }

        // find the target class
        Class target = null;
        for (Class each : classes) {
            if (each.getName().equals(className)) {
                target = each;
                break;
            }
        }
        if (target == null) { throw new Exception("Can not find the definition of " + className); }

        if (InheritanceGraph.classTable.get(target.getName()) == null) {
            // class not in table
            if (target.getParent() == null) {
                // no parent class
                InheritanceGraph.addClass(target.getName(), "Object");
            } else if (InheritanceGraph.classTable.get(target.getParent()) != null) {
                // parent class already in
                InheritanceGraph.addClass(target.getName(), target.getParent());
            } else {
                addClass(target.getParent(), origin);
                InheritanceGraph.addClass(target.getName(), target.getParent());
            }
        }
    }
}
