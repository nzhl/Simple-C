package front;

import front.lex.LexException;
import front.lex.Lexer;
import front.parse.ParseException;
import front.parse.Parser;
import front.parse.ast.Class;
import front.semantic.Analyzer;
import front.semantic.InheritanceGraph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {

        //----------------------  part 1 ----------------------------\\
        File fh = new File("./test.1c");
        Lexer lexer = new Lexer(new FileReader(fh));
        lexer.lex();
        lexer.print();

        //----------------------  part 2 ----------------------------\\

        Parser parser = new Parser(lexer.getTokens());
        parser.parse();

        for(Class c : parser.getProgram()) {
            System.out.println(c);
        }
        //----------------------  part 3 ----------------------------\\

        Analyzer analyzer = new Analyzer(parser.getProgram());
        analyzer.analysis();
        System.out.println(InheritanceGraph.classTable.get("Object"));
    }
}
