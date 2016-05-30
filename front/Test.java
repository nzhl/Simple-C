package front;

import front.ast.Fun;
import front.lex.LexException;
import front.lex.Lexer;
import front.parse.ParseException;
import front.parse.Parser;
import front.parse.TypeCheck;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, LexException, ParseException {

        //----------------------  part 1 ----------------------------\\
        File fh = new File("/Users/zhangzhimin/test.c");
        Lexer lexer = new Lexer(new FileReader(fh));
        lexer.lex();

        //----------------------  part 2 ----------------------------\\

        Parser parser = new Parser(lexer.getTokens());
        Fun fun = parser.parse();
        parser.printFun();

        System.out.println("\n----------------------------------------------------\n");

        //----------------------  part 3 ----------------------------\\

        TypeCheck typeCheck = new TypeCheck(fun);

    }
}
