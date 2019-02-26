package front.semantic;

import front.parse.ast.Attribute;

import java.util.HashMap;
import java.util.Stack;

public class SymbolTable {
    private Stack<HashMap<String, Attribute>> table = new Stack<>();

    public void enterScope() { table.push(new HashMap<>()); }
    public void exitScope() { table.pop(); }
    public boolean checkScope(String name) { return table.peek().get(name) != null; }

    public Attribute findSymbol(String name) {
        Attribute symbol = null;
        for (int i = table.size()-1; i > 0; --i) {
            symbol = table.elementAt(i).get(name);
            if (symbol != null) { break; }
        }
        return symbol;
    }
    public void addSymbol(Attribute symbol) { table.peek().put(symbol.getName(), symbol); }




}
