package front.ast.stmt;

public class Stmt {
    public enum Kind{
        DefStmt, AssignStmt, IfStmt, WhileStmt,PrintStmt
    }
    public Stmt next;
    public Kind kind;

    public void add(Stmt newStmt){
        Stmt temp = this;
        while(temp.next != null){
            temp = temp.next;
        }
        temp.next = newStmt;
    }
}
