package front.parse.ast;

public class Literal extends Expression{
    // 0 : number, 1: string, 2: boolean, 3: variable
    private int type;
    private String value;
    private int number;

    public Literal(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public Literal(int number) {
        this.type = 0;
        this.number = number;
    }

    @Override
    public String toString() {
        return type == 0 ? "" + number : value;
    }
}
