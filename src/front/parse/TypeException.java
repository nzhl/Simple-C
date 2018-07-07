package front.parse;

import front.ast.stmt.Stmt;

public class TypeException extends Exception{
    public TypeException(String message, Stmt stmt) {
        super(message);
        Parser.printStmt(stmt);
    }

    public TypeException(String message) {
        super(message);
    }
}
