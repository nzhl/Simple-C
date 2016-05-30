package front.ast.stmt;

public class Print extends Stmt{
    public enum InnerType{
        Pid, Pvalue
    }

    private InnerType innerType;
    private String name;

    public Print(InnerType innerType, String name) {
        this.innerType = innerType;
        this.name = name;
        kind = Kind.PrintStmt;
    }

    public InnerType getInnerType() {
        return innerType;
    }

    public String getName() {
        return name;
    }
}
