package front.ast;

import front.ast.stmt.Stmt;

public class Fun {
    public Fun next;
    public Type type;
    public String id;
    public Stmt stmt;

    public Fun(Type type, String id, Stmt stmt) {
        this.type = type;
        this.id = id;
        this.stmt = stmt;
    }

    public void add(Fun newFun){
        Fun temp = this;
        while(temp.next != null){
            temp = temp.next;
        }

        temp.next = newFun;
    }
}
